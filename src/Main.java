import java.util.Map;
import java.util.Map.Entry;

public class Main {

  static boolean verboseCall = false;
  static String verboseCallArg = null;

  static boolean verboseInt = false;
  static String verboseIntArg = null;

  static boolean isJar = false;
  static String jar = null;

  private static Map<Long, byte[]> mem = new java.util.HashMap<>();
  private static Long next = 1L;

  public static void main(String[] args) {
    String classpath = ".";
    String mainClass = null;

    // 解析参数
    int programArgIdx = 0;
    for (int i = 0; i < args.length; i++) {
      if (args[i].startsWith("-verbose:")) {
        final String[] split = args[i].split(":");
        switch (split[1]) {
          case "call":
            verboseCall = true;
            if (split.length > 2) {
              verboseCallArg = split[2];
            }
            continue;
          case "int":
            verboseInt = true;
            if (split.length > 2) {
              verboseIntArg = split[2];
            }
            continue;
          default:
            continue;
        }
      }
      if (args[i].equals("-cp")) {
        classpath = args[i + 1];
        i++;
        continue;
      }
      if (args[i].equals("-jar")) {
        isJar = true;
        jar = args[i + 1];
        classpath = classpath + ":" + jar;
        i++;
        programArgIdx = i + 1;
        break;
      }
      if (mainClass == null && !isJar) {
        mainClass = args[i].replace('.', '/');
        programArgIdx = i + 1;
      }
    }

    if (isJar) {
      try {
        final java.util.zip.ZipFile file = new java.util.zip.ZipFile(jar);
        final java.util.zip.ZipEntry entry = file.getEntry("META-INF/MANIFEST.MF");
        if (entry == null) {
          System.out.println(jar + " not found manifest.mf");
          System.exit(-1);
        }
        try (java.io.InputStream is = file.getInputStream(entry)) {
          String line;
          while ((line = readLine(is)) != null) {
            if (line.startsWith("Main-Class: ")) {
              mainClass = line.substring(12);
            }
          }
        }
      } catch (java.io.IOException e) {
        e.printStackTrace();
        System.exit(-1);
      }
    }

    if (mainClass == null) {
      System.out.println("not found main class arg");
      System.exit(-1);
    }

    String[] programArgs = new String[0];
    if (programArgIdx < args.length) {
      // -cp . hello a1 a2
      programArgs = new String[args.length - programArgIdx];
      System.arraycopy(args, programArgIdx, programArgs, 0, programArgs.length);
    }

    // classpath
    final String home = System.getenv("JAVA_HOME");
    if (home == null) {
      System.out.println("must set JAVA_HOME");
      System.exit(-1);
    }

    // rt.jar path
    final String runtimeJarPath = home + "/jre/lib/rt.jar";
    final boolean exists = new java.io.File(runtimeJarPath).exists();
    if (!exists) {
      System.out.println("not found rt.jar " + runtimeJarPath);
      System.exit(-1);
    }

    classpath = runtimeJarPath + ":" + classpath;
    final ClassLoader loader = new ClassLoader(classpath);
    MetaSpace.setClassLoader(loader);

    final Class main = loader.findClass(mainClass);
    if (main == null) {
      System.out.println("Error: Could not find or load main class " + mainClass);
      System.exit(-1);
    }

    Method method = main.getSpecialStaticMethod("main", "([Ljava/lang/String;)V");
    if (method == null) {
      System.out.println("Error: Main method not found in class " + main.name
          + ", please define the main method as:\n   public static void main(String[] args)");
      System.exit(-1);
    }

    final Map<Integer, Instruction> instructions = method.getInstructions();

    if (instructions == null) {
      System.out.println("not found method code instructions");
      System.exit(-1);
    }

    MetaSpace.putMainEnv(new ExecEnv(128));
    // init jvm
    initNativeMethods();
    // load foundation class
    loadFoundationClass(loader);

    // init thread
    initThread();

    ExecEnv env = MetaSpace.getMainEnv();
    Frame frame = new Frame(method);
    // 准备参数
    final Class sacls = MetaSpace.getClassLoader().findClass("[Ljava.lang.String;");
    final InstanceArray array = new InstanceArray(sacls, programArgs.length);
    for (int i = 0; i < programArgs.length; i++) {
      array.instances[i] = Utils.createString(programArgs[i]);
    }
    frame.setRef(0, array);
    env.pushFrame(frame);

    // main class clinit
    Utils.clinit(main);

    Interpreter.run();
  }

  private static void initThread() {
    final Class threadGroup = MetaSpace.getClassLoader().findClass("java/lang/ThreadGroup");
    Utils.clinit(threadGroup);
    final Instance tg = threadGroup.newInstance();
    final Method initMethod = tg.clazz.getSpecialMethod("<init>", "()V");
    final Frame imf = new Frame(initMethod);
    imf.setRef(0, tg);
    Interpreter.execute(imf);

    final Class thread = MetaSpace.getClassLoader().findClass("java/lang/Thread");
    Utils.clinit(thread);
    final Instance t = thread.newInstance();
    MetaSpace.getMainEnv().thread = t;
    t.getField("priority", "I").set(UnionSlot.of(1));
    final Method threadInit = t.clazz.getSpecialMethod("<init>", "(Ljava/lang/ThreadGroup;Ljava/lang/String;)V");
    final Frame timf = new Frame(threadInit);
    timf.setRef(0, t);
    timf.setRef(1, tg);
    timf.setRef(2, Utils.createString("main"));
    Interpreter.execute(timf);

    final Class sys = MetaSpace.findClass("java/lang/System");
    final Method method = sys.getStaticMethod("initializeSystemClass", "()V");
    Frame frame = new Frame(method);
    Interpreter.execute(frame);
  }


  private static void loadFoundationClass(ClassLoader loader) {
    loader.findClass("java/lang/String");
    Class metaClass = loader.findClass("java/lang/Class");
    final java.util.Collection<Class> classes = MetaSpace.allClass();
    for (Class cls : classes) {
      if (cls.mirror == null) {
        Instance obj = metaClass.newInstance();
        cls.mirror = obj;
        obj.metaClass = cls;
      }
    }

    // primitive class
    loadPrimitiveClass("char", "C");
    loadPrimitiveClass("boolean", "Z");
    loadPrimitiveClass("byte", "B");
    loadPrimitiveClass("short", "S");
    loadPrimitiveClass("int", "I");
    loadPrimitiveClass("long", "J");
    loadPrimitiveClass("float", "F");
    loadPrimitiveClass("double", "D");
    loadPrimitiveClass("void", "V");
  }

  private static void loadPrimitiveClass(String name, String alias) {
    Class cache = MetaSpace.findClass(name);
    if (cache != null) {
      return;
    }
    Class clazz = new Class(name, MetaSpace.findClass("java/lang/Object"), new Class[0], new Field[0],
        new Method[0], null, false);
    Instance mirror = MetaSpace.findClass("java/lang/Class").newInstance();
    clazz.mirror = mirror;
    mirror.metaClass = clazz;
    MetaSpace.putClass(name, clazz);
    MetaSpace.putClass(alias, clazz);
  }

  private static void initNativeMethods() {
    MetaSpace.putNativeMethod("MyJVM_version_()I", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        frame.pushInt(52);
      }
    });
    MetaSpace.putNativeMethod("java/lang/Object_registerNatives_()V", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
      }
    });
    MetaSpace.putNativeMethod("java/lang/Class_registerNatives_()V", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
      }
    });
    MetaSpace.putNativeMethod("java/lang/Class_desiredAssertionStatus0_(Ljava/lang/Class;)Z", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        frame.popRef();
        frame.pushInt(0);
      }
    });
    MetaSpace.putNativeMethod("java/lang/Throwable_fillInStackTrace_(I)Ljava/lang/Throwable;", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        final int dummy = frame.popInt();
        final Instance self = frame.popRef();
        final ExecEnv env = MetaSpace.getMainEnv();
        final Frame[] frames = env.frames;
        int top = env.top - 2;
        for (; top >= 0; top--) {
          if (!frames[top].method.name.equals("<init>")) {
            break;
          }
        }
        final StackTraceElement[] elements = new StackTraceElement[top + 1];
        for (int i = 0; i <= top; i++) {
          final Frame f = frames[i];
          elements[i] = new StackTraceElement(f.method.clazz.name, f.method.name, f.getSource(), f.getLine());
        }
        self.extra = elements;

        frame.pushRef(self);
      }
    });

    MetaSpace.putNativeMethod("java/lang/System_registerNatives_()V", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
      }
    });

    MetaSpace.putNativeMethod(
        "java/security/AccessController_doPrivileged_(Ljava/security/PrivilegedAction;)Ljava/lang/Object;",
        new NativeMethod() {
          @Override
          public void invoke(Frame frame) {
            final Instance self = frame.popRef();
            final Method run = self.clazz.getVirtualMethod("run", "()Ljava/lang/Object;");
            frame.pushRef(self);
            Interpreter.execute(run);
          }
        });

    MetaSpace.putNativeMethod("java/lang/Thread_registerNatives_()V", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
      }
    });

    MetaSpace.putNativeMethod("java/lang/Thread_currentThread_()Ljava/lang/Thread;", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        frame.pushRef(MetaSpace.getMainEnv().thread);
      }
    });

    MetaSpace.putNativeMethod(
        "java/security/AccessController_getStackAccessControlContext_()Ljava/security/AccessControlContext;",
        new NativeMethod() {
          @Override
          public void invoke(Frame frame) {
            frame.pushRef(null);
          }
        });

    MetaSpace.putNativeMethod("java/lang/Thread_setPriority0_(I)V", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        final int priority = frame.popInt();
        final Instance self = frame.popRef();
      }
    });

    MetaSpace.putNativeMethod("java/lang/Class_getName0_()Ljava/lang/String;", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        final Instance cls = frame.popRef();
        final Instance instance = Utils.createString(cls.metaClass.name);
        frame.pushRef(instance);
      }
    });

    MetaSpace.putNativeMethod(
        "java/lang/Class_forName0_(Ljava/lang/String;ZLjava/lang/ClassLoader;Ljava/lang/Class;)Ljava/lang/Class;",
        new NativeMethod() {
          @Override
          public void invoke(Frame frame) {
            final Instance callerClass = frame.popRef();
            final Instance loader = frame.popRef();
            final int init = frame.popInt();
            final Instance nameRef = frame.popRef();
            String name = Utils.fromString(nameRef);
            if (name.contains(".")) {
              name = name.replace(".", "/");
            }
            Class cls = MetaSpace.getClassLoader().findClass(name);
            // TODO: 异常处理， ClassNotFoundException
            if (cls == null) {
              final Class cnfeCls = MetaSpace.getClassLoader().findClass("java/lang/ClassNotFoundException");
              Utils.clinit(cnfeCls);
              final Instance instance = cnfeCls.newInstance();
              final Method initMethod = cnfeCls.getSpecialMethod("<init>", "(Ljava/lang/String;)V");
              final Frame initFrame = new Frame(initMethod);
              initFrame.setRef(0, instance);
              initFrame.setRef(1, Utils.createString(name));
              Interpreter.execute(initFrame);
              Utils.handleException(instance);
              return;
            }
            if (init == 1) {
              Utils.clinit(cls);
            }
            frame.pushRef(cls.mirror);
          }
        });

    MetaSpace.putNativeMethod("java/lang/Thread_isAlive_()Z", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        frame.popRef();
        if (MetaSpace.getMainEnv().thread.extra == null) {
          frame.pushInt(0);
        } else {
          frame.pushInt(1);
        }
      }
    });

    MetaSpace.putNativeMethod("java/lang/Thread_start0_()V", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        MetaSpace.getMainEnv().thread.extra = 1;
      }
    });

    MetaSpace.putNativeMethod("sun/misc/Unsafe_registerNatives_()V", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
      }
    });

    MetaSpace.putNativeMethod("java/lang/Object_hashCode_()I", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        final Instance rf = frame.popRef();
        frame.pushInt(rf.hashCode());
      }
    });

    MetaSpace.putNativeMethod("sun/misc/Unsafe_arrayBaseOffset_(Ljava/lang/Class;)I", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        frame.popRef();
        frame.popRef();
        frame.pushInt(0);
      }
    });

    MetaSpace.putNativeMethod("sun/misc/Unsafe_arrayIndexScale_(Ljava/lang/Class;)I", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        frame.popRef();
        frame.popRef();
        frame.pushInt(0);
      }
    });
    MetaSpace.putNativeMethod("sun/misc/Unsafe_addressSize_()I", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        frame.popRef();
        frame.pushInt(0);
      }
    });
    MetaSpace.putNativeMethod("sun/reflect/Reflection_getCallerClass_()Ljava/lang/Class;", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        final ExecEnv env = MetaSpace.getMainEnv();
        final Frame callerFrame = env.callerFrame();
        frame.pushRef(callerFrame.method.clazz.mirror);
      }
    });

    MetaSpace.putNativeMethod("java/lang/Class_getPrimitiveClass_(Ljava/lang/String;)Ljava/lang/Class;",
        new NativeMethod() {
          @Override
          public void invoke(Frame frame) {
            final String val = Utils.fromString(frame.popRef());
            final Class cls = MetaSpace.findClass(val);
            frame.pushRef(cls.mirror);
          }
        });

    MetaSpace.putNativeMethod("java/lang/Float_floatToRawIntBits_(F)I", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        final float fv = frame.popFloat();
        final int tmp = Float.floatToIntBits(fv);
        frame.pushInt(tmp);
      }
    });

    MetaSpace.putNativeMethod("java/lang/Double_doubleToRawLongBits_(D)J", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        final double dv = frame.popDouble();
        final long tmp = Double.doubleToRawLongBits(dv);
        frame.pushLong(tmp);
      }
    });
    MetaSpace.putNativeMethod("java/lang/Double_longBitsToDouble_(J)D", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        final long lv = frame.popLong();
        frame.pushDouble(Double.longBitsToDouble(lv));
      }
    });

    MetaSpace.putNativeMethod("sun/misc/VM_initialize_()V", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
      }
    });

    MetaSpace.putNativeMethod("java/lang/Class_getDeclaredFields0_(Z)[Ljava/lang/reflect/Field;", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        final int publicOnly = frame.popInt();
        final Class cls = frame.popRef().metaClass;
        final Class fs = MetaSpace.getClassLoader().findClass("java/lang/reflect/Field");
        final Class afs = MetaSpace.getClassLoader().findClass("[Ljava/lang/reflect/Field;");
        final Field[] fields = cls.fields;
        final InstanceArray array = new InstanceArray(afs, fields.length);

        for (int i = 0; i < fields.length; i++) {
          final Instance obj = fs.newInstance();
          final Field field = fields[i];
          final Field clazz = obj.getField("clazz", "Ljava/lang/Class;");
          final Field name = obj.getField("name", "Ljava/lang/String;");
          final Field type = obj.getField("type", "Ljava/lang/Class;");
          final Field modifiers = obj.getField("modifiers", "I");
          final Field slot = obj.getField("slot", "I");
          final Field signature = obj.getField("signature", "Ljava/lang/String;");
          final Field annotations = obj.getField("annotations", "[B");
          clazz.set(UnionSlot.of(cls.mirror));
          Instance str = MetaSpace.RUNTIME_STRING_POOL.get(field.name.hashCode());
          if (str == null) {
            str = Utils.createString(field.name);
          }
          ;
          if (!MetaSpace.RUNTIME_STRING_POOL.containsKey(field.name.hashCode())) {
            MetaSpace.RUNTIME_STRING_POOL.put(field.name.hashCode(), str);
          }
          name.set(UnionSlot.of(str));
          Class typeCls = MetaSpace.getClassLoader().findClass(field.getType());
          type.set(UnionSlot.of(typeCls.mirror));
          modifiers.set(UnionSlot.of(field.accessFlags));
          slot.set(UnionSlot.of(i));
          signature.set(UnionSlot.of(Utils.createString(field.descriptor)));
          annotations.set(UnionSlot.of((Instance) null));
          array.instances[i] = obj;
        }

        frame.pushRef(array);
      }
    });

    MetaSpace.putNativeMethod("java/lang/String_intern_()Ljava/lang/String;", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        final Instance ref = frame.popRef();
        final String str = Utils.fromString(ref);
        Instance newRef = MetaSpace.RUNTIME_STRING_POOL.get(str.hashCode());
        if (newRef == null) {
          newRef = ref;
        }
        ;
        frame.pushRef(newRef);
      }
    });

    MetaSpace.putNativeMethod("sun/misc/Unsafe_objectFieldOffset_(Ljava/lang/reflect/Field;)J", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        final Instance ref = frame.popRef();
        final int slot = ref.getField("slot", "I").val.getInt();
        frame.pop(); // this
        frame.pushLong(slot);
      }
    });

    MetaSpace.putNativeMethod(
        "sun/misc/Unsafe_compareAndSwapObject_(Ljava/lang/Object;JLjava/lang/Object;Ljava/lang/Object;)Z",
        new NativeMethod() {
          @Override
          public void invoke(Frame frame) {
            final Instance neo = frame.popRef();
            final Instance old = frame.popRef();
            final long offset = frame.popLong();
            final Instance self = frame.popRef();
            frame.pop(); // unsafe
            frame.pushInt(1);
          }
        });

    MetaSpace.putNativeMethod("java/lang/Class_isInterface_()Z", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        final Instance self = frame.popRef();
        boolean ret = Utils.isInterface(self.clazz.classFile.accessFlags.val);
        frame.pushInt(ret ? 1 : 0);
      }
    });

    MetaSpace.putNativeMethod("java/lang/Class_getDeclaredConstructors0_(Z)[Ljava/lang/reflect/Constructor;",
        new NativeMethod() {
          @Override
          public void invoke(Frame frame) {
            final int publicOnly = frame.popInt();
            final Class cls = frame.popRef().metaClass;

            final Class cs = MetaSpace.getClassLoader().findClass("java/lang/reflect/Constructor");
            final Class acs = MetaSpace.getClassLoader().findClass("[Ljava/lang/reflect/Constructor;");

            final Method[] methods = cls.methods;
            int len = 0;
            for (Method method : methods) {
              if (method.name.equals("<init>")) {
                len++;
              }
            }
            final InstanceArray array = new InstanceArray(acs, len);
            frame.pushRef(array);
            if (len == 0) {
              return;
            }

            int i = 0;
            for (int j = 0; j < methods.length; j++) {
              final Method method = methods[j];
              if (!method.name.equals("<init>")) {
                continue;
              }
              final Instance obj = cs.newInstance();
              final Field clazz = obj.getField("clazz", "Ljava/lang/Class;");
              clazz.set(UnionSlot.of(cls.mirror));
              final Field parameterTypes = obj.getField("parameterTypes", "[Ljava/lang/Class;");
              final Field exceptionTypes = obj.getField("exceptionTypes", "[Ljava/lang/Class;");

              final Field modifiers = obj.getField("modifiers", "I");
              final Field slot = obj.getField("slot", "I");
              final Field signature = obj.getField("signature", "Ljava/lang/String;");
              final Field annotations = obj.getField("annotations", "[B");
              final Field parameterAnnotations = obj.getField("parameterAnnotations", "[B");

              final Class acc = MetaSpace.getClassLoader().findClass("[Ljava/lang/Class;");
              final InstanceArray a2 = new InstanceArray(acc, 0);

              if (method.descriptor.equals("()V")) {
                final InstanceArray a1 = new InstanceArray(acc, 0);
                parameterTypes.set(UnionSlot.of(a1));
              } else if (method.descriptor.equals("(Ljava/lang/String;)V")) {
                final InstanceArray a1 = new InstanceArray(acc, 1);
                a1.instances[0] = MetaSpace.getClassLoader().findClass("java/lang/String").mirror;
                parameterTypes.set(UnionSlot.of(a1));
              }

              exceptionTypes.set(UnionSlot.of(a2));
              modifiers.set(UnionSlot.of(method.accessFlags));
              slot.set(UnionSlot.of(j));
              signature.set(UnionSlot.of(Utils.createString(method.descriptor)));
              annotations.set(UnionSlot.of((Instance) null));
              parameterAnnotations.set(UnionSlot.of((Instance) null));
              array.instances[i++] = obj;
            }
          }
        });

    MetaSpace.putNativeMethod("sun/reflect/Reflection_getClassAccessFlags_(Ljava/lang/Class;)I", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        final Instance ref = frame.popRef();
        final int flags = ref.metaClass.classFile.accessFlags.val;
        frame.pushInt(flags);
      }
    });

    MetaSpace.putNativeMethod("java/lang/Class_getModifiers_()I", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        final Instance self = frame.popRef();
        final int flags = self.metaClass.classFile.accessFlags.val;
        frame.pushInt(flags);
      }
    });

    MetaSpace.putNativeMethod("java/lang/Class_getSuperclass_()Ljava/lang/Class;", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        final Instance self = frame.popRef();
        if (self.metaClass.superClass == null) {
          frame.pushRef(null);
          return;
        }
        final Instance superClass = self.metaClass.superClass.mirror;
        frame.pushRef(superClass);
      }
    });

    MetaSpace.putNativeMethod(
        "sun/reflect/NativeConstructorAccessorImpl_newInstance0_(Ljava/lang/reflect/Constructor;[Ljava/lang/Object;)Ljava/lang/Object;",
        new NativeMethod() {
          @Override
          public void invoke(Frame frame) {
            // static method
            final Instance args = frame.popRef();
            final Instance constructor = frame.popRef();

            final Field cls = constructor.getField("clazz", "Ljava/lang/Class;");
            final Class metaClass = cls.val.getRef().metaClass;
            Utils.clinit(metaClass);

            final Instance instance = metaClass.newInstance();
            frame.pushRef(instance);
            if (args == null) {
              final Method initMethod = metaClass.getSpecialMethod("<init>", "()V");
              final Frame tf = new Frame(initMethod);
              tf.setRef(0, instance);
              Interpreter.execute(tf);
              return;
            }
            final InstanceArray array = (InstanceArray) args;
            if (array.instances.length == 1 && array.instances[0].clazz.name.equals("java/lang/String")) {
              final Method initMethod = metaClass.getSpecialMethod("<init>", "(Ljava/lang/String;)V");
              final Frame tf = new Frame(initMethod);
              tf.setRef(0, instance);
              tf.setRef(1, array.instances[0]);
              Interpreter.execute(tf);
              return;
            }
            throw new IllegalStateException();
          }
        });

    MetaSpace.putNativeMethod("java/lang/System_arraycopy_(Ljava/lang/Object;ILjava/lang/Object;II)V",
        new NativeMethod() {
          @Override
          public void invoke(Frame frame) {
            int len = frame.popInt();
            int dsp = frame.popInt();
            final Instance dest = frame.popRef();
            if (dest instanceof InstanceArray) {
              final InstanceArray da = (InstanceArray) dest;
              int ssp = frame.popInt();
              final InstanceArray sa = (InstanceArray) frame.popRef();
              for (int i = 0; i < len; i++) {
                da.instances[dsp++] = sa.instances[ssp++];
              }
            } else {
              final PrimitiveArray da = (PrimitiveArray) dest;
              int ssp = frame.popInt();
              final PrimitiveArray sa = (PrimitiveArray) frame.popRef();
              for (int i = 0; i < len; i++) {
                if (da.ints != null) {
                  da.ints[dsp++] = sa.ints[ssp++];
                } else if (da.longs != null) {
                  da.longs[dsp++] = sa.longs[ssp++];
                } else if (da.floats != null) {
                  da.floats[dsp++] = sa.floats[ssp++];
                } else {
                  da.doubles[dsp++] = sa.doubles[ssp++];
                }
              }
            }
          }
        });

    MetaSpace.putNativeMethod("java/lang/Object_clone_()Ljava/lang/Object;", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        // TODO：需要检验实例是否实现了 java/lang/Cloneable 接口
        frame.pushRef(frame.popRef().cloneInstance());
      }
    });

    MetaSpace.putNativeMethod("sun/misc/Unsafe_ensureClassInitialized_(Ljava/lang/Class;)V", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        final Class metaClass = frame.popRef().metaClass;
        Utils.clinit(metaClass);
      }
    });

    MetaSpace.putNativeMethod("sun/misc/Unsafe_staticFieldOffset_(Ljava/lang/reflect/Field;)J", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        final Instance instance = frame.popRef();
        frame.popRef(); // unsafe
        final int slot = instance.getField("slot", "I").val.getInt();
        frame.pushLong(slot);
      }
    });

    MetaSpace
        .putNativeMethod("sun/misc/Unsafe_staticFieldBase_(Ljava/lang/reflect/Field;)Ljava/lang/Object;",
            new NativeMethod() {
              @Override
              public void invoke(Frame frame) {
                final Instance field = frame.popRef();
                frame.popRef(); // unsafe

                final Field clsField = field.getField("clazz", "Ljava/lang/Class;");
                final Instance cls = clsField.val.getRef();
                frame.pushRef(cls);
              }
            });

    MetaSpace.putNativeMethod("sun/misc/Unsafe_getObject_(Ljava/lang/Object;J)Ljava/lang/Object;", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        final int slot = (int) frame.popLong();
        final Instance instance = frame.popRef();
        frame.popRef(); // unsafe

        if (instance.clazz.instanceOf("java/lang/Class")) {
          final Field field = instance.metaClass.fields[slot];
          frame.pushRef(field.val.getRef());
          return;
        }
        final Field field = instance.fields[slot];
        frame.pushRef(field.val.getRef());
      }
    });

    MetaSpace.putNativeMethod("java/lang/Object_getClass_()Ljava/lang/Class;", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        final Instance instance = frame.popRef();
        frame.pushRef(instance.clazz.mirror);
      }
    });

    MetaSpace.putNativeMethod("java/lang/Class_isAssignableFrom_(Ljava/lang/Class;)Z", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        final Instance instance = frame.popRef();
        final Instance self = frame.popRef();
        // TODO:
        final boolean ret = instance.metaClass.instanceOf(self.metaClass.name);
        frame.pushInt(ret ? 1 : 0);
      }
    });

    MetaSpace.putNativeMethod("java/lang/Class_getDeclaredMethods0_(Z)[Ljava/lang/reflect/Method;", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        final int publicOnly = frame.popInt();
        final Class cls = frame.popRef().metaClass;

        final Class cs = MetaSpace.getClassLoader().findClass("java/lang/reflect/Method");
        final Class acs = MetaSpace.getClassLoader().findClass("[Ljava/lang/reflect/Method;");

        final Method[] methods = cls.methods;
        int len = 0;
        for (Method method : methods) {
          if (!(method.name.equals("<init>") || method.name.equals("<clinit>"))) {
            len++;
          }
        }
        final InstanceArray array = new InstanceArray(acs, len);
        frame.pushRef(array);
        if (len == 0) {
          return;
        }

        int i = 0;
        for (int j = 0; j < methods.length; j++) {
          final Method method = methods[j];
          if (method.name.equals("<init>") || method.name.equals("<clinit>")) {
            continue;
          }
          final Instance obj = cs.newInstance();
          final Field clazz = obj.getField("clazz", "Ljava/lang/Class;");
          clazz.set(UnionSlot.of(cls.mirror));
          final Field name = obj.getField("name", "Ljava/lang/String;");
          final Instance nameStr = Utils.createString(method.name);
          Instance str = MetaSpace.RUNTIME_STRING_POOL.get(method.name.hashCode());
          if (str == null) {
            str = Utils.createString(method.name);
          }
          ;
          if (!MetaSpace.RUNTIME_STRING_POOL.containsKey(method.name.hashCode())) {
            MetaSpace.RUNTIME_STRING_POOL.put(method.name.hashCode(), str);
          }
          name.set(UnionSlot.of(str));

          final Field parameterTypes = obj.getField("parameterTypes", "[Ljava/lang/Class;");
          final Field returnType = obj.getField("returnType", "Ljava/lang/Class;");
          returnType.set(UnionSlot.of(MetaSpace.getClassLoader().findClass("void").mirror));
          final Field exceptionTypes = obj.getField("exceptionTypes", "[Ljava/lang/Class;");

          final Field modifiers = obj.getField("modifiers", "I");
          final Field slot = obj.getField("slot", "I");
          final Field signature = obj.getField("signature", "Ljava/lang/String;");
          final Field annotations = obj.getField("annotations", "[B");
          final Field parameterAnnotations = obj.getField("parameterAnnotations", "[B");
          final Field annotationDefault = obj.getField("annotationDefault", "[B");

          final Class acc = MetaSpace.getClassLoader().findClass("[Ljava/lang/Class;");
          final InstanceArray a2 = new InstanceArray(acc, 0);

          if (method.descriptor.equals("()V")) {
            final InstanceArray a1 = new InstanceArray(acc, 0);
            parameterTypes.set(UnionSlot.of(a1));
          } else if (method.descriptor.equals("(Ljava/lang/String;)V")) {
            final InstanceArray a1 = new InstanceArray(acc, 1);
            a1.instances[0] = MetaSpace.getClassLoader().findClass("java/lang/String").mirror;
            parameterTypes.set(UnionSlot.of(a1));
          }

          exceptionTypes.set(UnionSlot.of(a2));
          modifiers.set(UnionSlot.of(method.accessFlags));
          slot.set(UnionSlot.of(j));
          signature.set(UnionSlot.of(Utils.createString(method.descriptor)));
          annotations.set(UnionSlot.of((Instance) null));
          parameterAnnotations.set(UnionSlot.of((Instance) null));
          annotationDefault.set(UnionSlot.of((Instance) null));
          array.instances[i++] = obj;
        }
      }
    });

    MetaSpace.putNativeMethod(
        "sun/reflect/NativeMethodAccessorImpl_invoke0_(Ljava/lang/reflect/Method;Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;",
        new NativeMethod() {
          @Override
          public void invoke(Frame frame) {
            // TODO 暂时支持无参无返回值方法
            final InstanceArray args = ((InstanceArray) frame.popRef());
            final Instance obj = frame.popRef();
            final Instance method = frame.popRef();

            final Class cls = method.getField("clazz", "Ljava/lang/Class;").val.getRef().metaClass;
            final int slot = method.getField("slot", "I").val.getInt();
            final Method nativeMethod = cls.methods[slot];
            final Frame newFrame = new Frame(nativeMethod);
            int j = 0;
            if (!Utils.isStatic(nativeMethod.accessFlags)) {
              newFrame.setRef(j++, obj);
            }
            for (Instance instance : args.instances) {
              newFrame.setRef(j++, instance);
            }

            Interpreter.execute(newFrame);
            // TODO ： 返回真实值
            frame.pushRef(null);
          }
        });

    MetaSpace.putNativeMethod("java/lang/ClassLoader_registerNatives_()V", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
      }
    });

    MetaSpace
        .putNativeMethod("java/lang/System_initProperties_(Ljava/util/Properties;)Ljava/util/Properties;",
            new NativeMethod() {
              @Override
              public void invoke(Frame frame) {
                final java.util.Properties properties = System.getProperties();
                final Instance self = frame.popRef();
                frame.pushRef(self);

                for (Entry<Object, Object> entry : properties.entrySet()) {
                  final Object key = entry.getKey();
                  final Object val = entry.getValue();
                  Method method = self.clazz
                      .getVirtualMethod("setProperty", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;");
                  final Frame newFrame = new Frame(method);
                  newFrame.setRef(0, self);
                  newFrame.setRef(1, Utils.createString(key.toString()));
                  newFrame.setRef(2, Utils.createString(val.toString()));
                  Interpreter.execute(newFrame);
                  // pop result
                  MetaSpace.getMainEnv().topFrame().popRef();
                }

                for (Entry<Object, Object> entry : properties.entrySet()) {
                  final Object key = entry.getKey();
                  final Object val = entry.getValue();

                  Method method = self.clazz.getVirtualMethod("getProperty", "(Ljava/lang/String;)Ljava/lang/String;");
                  final Frame newFrame = new Frame(method);
                  newFrame.setRef(0, self);
                  newFrame.setRef(1, Utils.createString(key.toString()));
                  Interpreter.execute(newFrame);

                  final Frame now = MetaSpace.getMainEnv().topFrame();
                  final Instance ref = now.popRef();
                  final String str = Utils.fromString(ref);
//            System.out.println(key + ":\t" + str);
                }
              }
            });
    MetaSpace.putNativeMethod("java/io/FileInputStream_initIDs_()V", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
      }
    });

    MetaSpace.putNativeMethod("java/io/FileDescriptor_initIDs_()V", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
      }
    });

    MetaSpace.putNativeMethod("java/io/FileOutputStream_initIDs_()V", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
      }
    });

    MetaSpace.putNativeMethod(
        "java/security/AccessController_doPrivileged_(Ljava/security/PrivilegedExceptionAction;)Ljava/lang/Object;",
        new NativeMethod() {
          @Override
          public void invoke(Frame frame) {
            final Instance self = frame.popRef();
            final Method run = self.clazz.getVirtualMethod("run", "()Ljava/lang/Object;");
            frame.pushRef(self);
            Interpreter.execute(run);
          }
        });

    MetaSpace.putNativeMethod("java/lang/Class_isPrimitive_()Z", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        final boolean ret = frame.popRef().metaClass.isPrimitive();
        frame.pushInt(ret ? 1 : 0);
      }
    });

    MetaSpace.putNativeMethod("java/lang/System_setIn0_(Ljava/io/InputStream;)V", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        final Class sysCls = MetaSpace.getClassLoader().findClass("java/lang/System");
        final Field inField = sysCls.getStaticField("in", "Ljava/io/InputStream;");
        inField.set(frame);
      }
    });

    MetaSpace.putNativeMethod("java/lang/System_setOut0_(Ljava/io/PrintStream;)V", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        final Class sysCls = MetaSpace.getClassLoader().findClass("java/lang/System");
        final Field outField = sysCls.getStaticField("out", "Ljava/io/PrintStream;");
        outField.set(frame);
      }
    });

    MetaSpace.putNativeMethod("java/lang/System_setErr0_(Ljava/io/PrintStream;)V", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        final Class sysCls = MetaSpace.getClassLoader().findClass("java/lang/System");
        final Field errField = sysCls.getStaticField("err", "Ljava/io/PrintStream;");
        errField.set(frame);
      }
    });

    MetaSpace.putNativeMethod("sun/misc/Unsafe_getIntVolatile_(Ljava/lang/Object;J)I", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        final int slot = (int) frame.popLong();
        final Instance instance = frame.popRef();
        frame.pop(); // self

        final Field field = instance.fields[slot];
        final int val = field.val.getInt();
        frame.pushInt(val);
      }
    });

    MetaSpace.putNativeMethod("sun/misc/Unsafe_compareAndSwapInt_(Ljava/lang/Object;JII)Z", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        final int neo = frame.popInt();
        final int old = frame.popInt();
        final int offset = (int) frame.popLong();
        final Instance instance = frame.popRef();
        frame.pop(); // unsafe
        final Field field = instance.fields[offset];
        final int val = field.val.getInt();
        if (val != old) {
          frame.pushInt(0);
          return;
        }
        field.val.setInt(neo);
        frame.pushInt(1);
      }
    });

    MetaSpace.putNativeMethod("sun/misc/Unsafe_allocateMemory_(J)J", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        long tmp = next;
        long val = frame.popLong();
        frame.popRef();

        byte[] data = new byte[(int) val];
        mem.put(next, data);
        next += val;

        frame.pushLong(tmp);
      }
    });

    MetaSpace.putNativeMethod("sun/misc/Unsafe_putLong_(JJ)V", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        long v2 = frame.popLong();
        long v1 = frame.popLong();
        frame.popRef(); // this

        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(8);
        buffer.putLong(0, v2);
        byte[] bytes = buffer.array();
        mem.put(v1, bytes);
      }
    });
    MetaSpace.putNativeMethod("sun/misc/Unsafe_getByte_(J)B", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        final long idx = frame.popLong();
        final byte[] bytes = mem.get(idx);
        final byte tmp = java.nio.ByteBuffer.wrap(bytes).get();
        frame.pushInt(tmp);
      }
    });
    MetaSpace.putNativeMethod("sun/misc/Unsafe_freeMemory_(J)V", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        final long idx = frame.popLong();
        mem.remove(idx);
      }
    });
    MetaSpace.putNativeMethod("java/util/concurrent/atomic/AtomicLong_VMSupportsCS8_()Z", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        frame.pushInt(0);
      }
    });

    MetaSpace.putNativeMethod("java/io/UnixFileSystem_initIDs_()V", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
      }
    });

    MetaSpace.putNativeMethod("java/lang/System_mapLibraryName_(Ljava/lang/String;)Ljava/lang/String;",
        new NativeMethod() {
          @Override
          public void invoke(Frame frame) {
            final Instance instance = frame.popRef();
            final String str = Utils.fromString(instance);
            if (str.equals("zip")) {
              final String osName = System.getProperty("os.name");
              if (osName.contains("Mac")) {
                frame.pushRef(Utils.createString("libzip.dylib"));
              } else {
                frame.pushRef(Utils.createString("amd64/libzip.so"));
              }
            } else {
              frame.pushRef(instance);
            }
          }
        });

    MetaSpace.putNativeMethod("java/lang/ClassLoader_findBuiltinLib_(Ljava/lang/String;)Ljava/lang/String;",
        new NativeMethod() {
          @Override
          public void invoke(Frame frame) {
            final Instance instance = frame.popRef();
            final String str = Utils.fromString(instance);
            frame.pushRef(instance);
          }
        });

    MetaSpace.putNativeMethod("java/io/UnixFileSystem_getBooleanAttributes0_(Ljava/io/File;)I", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        Instance fileObj = frame.popRef();
        Object thisObj = frame.popRef();
        Instance pathObj = fileObj.getField("path", "Ljava/lang/String;").val.getRef();
        String path = Utils.fromString(pathObj);
        java.io.File file = new java.io.File(path);
        boolean exists = file.exists();
        boolean directory = file.isDirectory();
        int ret = 0;
        if (exists) {
          ret |= 0x01;
        }
        if (directory) {
          ret |= 0x04;
        }
        frame.pushInt(ret);
      }
    });

    MetaSpace.putNativeMethod("java/lang/ClassLoader$NativeLibrary_load_(Ljava/lang/String;Z)V", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        final int tmp = frame.popInt();
        final Instance instance = frame.popRef();
        final Instance self = frame.popRef();
        self.getField("loaded", "Z").set(UnionSlot.of(1));
      }
    });

    MetaSpace.putNativeMethod("sun/misc/Signal_findSignal_(Ljava/lang/String;)I", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        frame.popRef();
        frame.pushInt(0);
      }
    });

    MetaSpace.putNativeMethod("sun/misc/Signal_handle0_(IJ)J", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        frame.popLong();
        frame.popInt();
        frame.pushLong(0L);
      }
    });

    MetaSpace.putNativeMethod("java/lang/Object_notifyAll_()V", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        frame.popRef();
      }
    });

    MetaSpace.putNativeMethod("java/io/FileOutputStream_writeBytes_([BIIZ)V", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        boolean append = frame.popInt() == 1;
        int len = frame.popInt();
        int off = frame.popInt();
        final PrimitiveArray array = (PrimitiveArray) frame.popRef();

        byte[] bytes = new byte[len];
        for (int i = off; i < len; i++) {
          bytes[i - off] = (byte) array.ints[i];
        }

        Instance thisObj = frame.popRef();
        final int fd = thisObj.getField("fd", "Ljava/io/FileDescriptor;").val.getRef().getField("fd", "I").val.getInt();
        // out
        if (fd == 1) {
          thisObj.extra = System.out;
        } else if (fd == 2) {
          thisObj.extra = System.err;
        } else {
          if (thisObj.extra == null) {
            throw new IllegalStateException();
          }
        }

        try {
          if (thisObj.extra instanceof java.io.PrintStream) {
            ((java.io.PrintStream) thisObj.extra).write(bytes);
          } else {
            ((java.io.FileOutputStream) thisObj.extra).write(bytes);
          }
        } catch (java.io.IOException e) {
          e.printStackTrace();
        }
      }
    });

    MetaSpace.putNativeMethod("java/io/FileOutputStream_open0_(Ljava/lang/String;Z)V", new NativeMethod() {
      @Override
      public void invoke(Frame frame) {
        final int append = frame.popInt();
        final Instance name = frame.popRef();
        final Instance self = frame.popRef();

        try {
          final java.io.FileOutputStream fos = new java.io.FileOutputStream(Utils.fromString(name));
          self.extra = fos;
        } catch (java.io.FileNotFoundException e) {
          final Class fnfeCls = MetaSpace.getClassLoader().findClass("java/io/FileNotFoundException");
          final Instance instance = fnfeCls.newInstance();
          final Method initMethod = fnfeCls.getSpecialMethod("<init>", "(Ljava/lang/String;)V");
          final Frame initFrame = new Frame(initMethod);
          initFrame.setRef(0, instance);
          initFrame.setRef(1, Utils.createString(e.getMessage()));
          Interpreter.execute(initFrame);
          Utils.handleException(instance);
        }
      }
    });
  }

  private static String readLine(java.io.InputStream is) throws java.io.IOException {
    StringBuilder line = new StringBuilder();
    int b = is.read();
    if (b < 0) {
      return null;
    }
    while (b > 0) {
      char c = (char) b;
      if (c == '\r' || c == '\n') {
        break;
      }
      if (c == '.') {
        c = '/';
      }
      line.append(c);
      b = is.read();
    }
    return line.toString();
  }
}
