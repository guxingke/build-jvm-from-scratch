public class Utils {

  /**
   * 从常量池中获取 utf8 字符串
   */
  public static String getUtf8(CpInfo[] cp, int utf8Idx) {
    final CpInfo utf8 = cp[utf8Idx];
    if (utf8.tag.val != Const.CONSTANT_Utf8) {
      throw new IllegalStateException("un expect tag");
    }

    return new String(utf8.info);
  }

  /**
   * 获取字符串
   */
  public static String getString(CpInfo[] cp, int strIdx) {
    final CpInfo cpInfo = cp[strIdx];
    if (cpInfo.tag.val != Const.CONSTANT_String) {
      throw new IllegalStateException("un expect tag");
    }
    final byte[] info = cpInfo.info;
    final int idx = (info[0] << 8) + (info[1] & 0xff);
    return getUtf8(cp, idx);
  }

  /**
   * 从常量池中获取 类名
   */
  public static String getClassName(CpInfo[] cp, int classIdx) {
    final CpInfo classInfo = cp[classIdx];
    if (classInfo.tag.val != Const.CONSTANT_Class) {
      throw new IllegalStateException("un expect tag");
    }

    final byte[] info = classInfo.info;
    final int nameIndex = (info[0] << 8) + (info[1] & 0xff);
    return getUtf8(cp, nameIndex);
  }

  /**
   * 从常量池 NameAndType 项中获取 Name.
   */
  public static String getNameFromNameAndType(CpInfo[] cp, int idx) {
    final CpInfo item = cp[idx];
    if (item.tag.val != Const.CONSTANT_NameAndType) {
      throw new IllegalStateException("un expect tag");
    }
    final byte[] info = item.info;
    final int uidx = (info[0] << 8) + (info[1] & 0xff);
    return getUtf8(cp, uidx);
  }

  /**
   * 从常量池 NameAndType 项中获取 type.
   */
  public static String getDescriptorFromNameAndType(CpInfo[] cp, int idx) {
    final CpInfo item = cp[idx];
    if (item.tag.val != Const.CONSTANT_NameAndType) {
      throw new IllegalStateException("un expect tag");
    }
    final byte[] info = item.info;
    final int uidx = (info[2] << 8) | (info[3] & 0xff);
    return getUtf8(cp, uidx);
  }

  /**
   * 从常量池 FiledRef 中获取 类名
   */
  public static String getClassNameFromFieldRef(CpInfo[] cp, int fieldRefIdx) {
    final CpInfo item = cp[fieldRefIdx];
    if (item.tag.val != Const.CONSTANT_Fieldref) {
      throw new IllegalStateException("un expect tag");
    }

    final byte[] info = item.info;
    final int clzIdx = (info[0] << 8) | (info[1] & 0xff);
    return getClassName(cp, clzIdx);
  }

  /**
   * 从常量池 FiledRef 中获取 字段名
   */
  public static String getNameFromFieldRef(CpInfo[] cp, int fieldRefIdx) {
    final CpInfo item = cp[fieldRefIdx];
    if (item.tag.val != Const.CONSTANT_Fieldref) {
      throw new IllegalStateException("un expect tag");
    }

    final byte[] info = item.info;
    final int nameIndex = (info[2] << 8) | (info[3] & 0xff);
    return getNameFromNameAndType(cp, nameIndex);
  }

  /**
   * 从常量池 FiledRef 中获取 字段描述符
   */
  public static String getDescriptorFromFieldRef(CpInfo[] cp, int fieldRefIdx) {
    final CpInfo item = cp[fieldRefIdx];
    if (item.tag.val != Const.CONSTANT_Fieldref) {
      throw new IllegalStateException("un expect tag");
    }

    final byte[] info = item.info;
    final int idx = (info[2] << 8) | (info[3] & 0xff);
    return getDescriptorFromNameAndType(cp, idx);
  }


  /**
   * 从常量池 MethodRef 中获取 类名
   */
  public static String getClassNameFromMethodRef(CpInfo[] cp, int refIdx) {
    final CpInfo item = cp[refIdx];
    if (item.tag.val != Const.CONSTANT_Methodref) {
      throw new IllegalStateException("un expect tag");
    }

    final byte[] info = item.info;
    final int clzIdx = (info[0] << 8) | (info[1] & 0xff);
    return getClassName(cp, clzIdx);
  }

  /**
   * 从常量池 MethodRef 中获取 方法名
   */
  public static String getNameFromMethodRef(CpInfo[] cp, int refIdx) {
    final CpInfo item = cp[refIdx];
    if (item.tag.val != Const.CONSTANT_Methodref) {
      throw new IllegalStateException("un expect tag");
    }

    final byte[] info = item.info;
    final int nameIndex = (info[2] << 8) | (info[3] & 0xff);
    return getNameFromNameAndType(cp, nameIndex);
  }

  /**
   * 从常量池 MethodRef 中获取 方法描述符
   */
  public static String getDescriptorFromMethodRef(CpInfo[] cp, int refIdx) {
    final CpInfo item = cp[refIdx];
    if (item.tag.val != Const.CONSTANT_Methodref) {
      throw new IllegalStateException("un expect tag");
    }

    final byte[] info = item.info;
    final int idx = (info[2] << 8) | (info[3] & 0xff);
    return getDescriptorFromNameAndType(cp, idx);
  }

  /**
   * 从常量池 InterfaceMethodRef 中获取 类名
   */
  public static String getClassNameFromInterfaceMethodRef(CpInfo[] cp, int refIdx) {
    final CpInfo item = cp[refIdx];
    if (item.tag.val != Const.CONSTANT_InterfaceMethodref) {
      throw new IllegalStateException("un expect tag");
    }

    final byte[] info = item.info;
    final int clzIdx = (info[0] << 8) | (info[1] & 0xff);
    return getClassName(cp, clzIdx);
  }

  /**
   * 从常量池 InterfaceMethodRef 中获取 方法名
   */
  public static String getNameFromInterfaceMethodRef(CpInfo[] cp, int refIdx) {
    final CpInfo item = cp[refIdx];
    if (item.tag.val != Const.CONSTANT_InterfaceMethodref) {
      throw new IllegalStateException("un expect tag");
    }

    final byte[] info = item.info;
    final int nameIndex = (info[2] << 8) | (info[3] & 0xff);
    return getNameFromNameAndType(cp, nameIndex);
  }

  /**
   * 从常量池 InterfaceMethodRef 中获取 方法描述符
   */
  public static String getDescriptorFromInterfaceMethodRef(CpInfo[] cp, int refIdx) {
    final CpInfo item = cp[refIdx];
    if (item.tag.val != Const.CONSTANT_InterfaceMethodref) {
      throw new IllegalStateException("un expect tag");
    }

    final byte[] info = item.info;
    final int idx = (info[2] << 8) | (info[3] & 0xff);
    return getDescriptorFromNameAndType(cp, idx);
  }

  /**
   * 判定访问标志是否是 static
   */
  public static boolean isStatic(int accessFlags) {
    return (accessFlags & Const.ACC_STATIC) != 0;
  }

  /**
   * 判定访问标志是否是 native
   */
  public static boolean isNative(int accessFlags) {
    return (accessFlags & Const.ACC_NATIVE) != 0;
  }

  public static boolean isInterface(int accessFlags) {
    return (accessFlags & Const.ACC_INTERFACE) != 0;
  }

  public static boolean isAbstract(int accessFlags) {
    return (accessFlags & Const.ACC_ABSTRACT) != 0;
  }

  public static long makeLong(byte b7, byte b6, byte b5, byte b4,
      byte b3, byte b2, byte b1, byte b0) {
    return ((((long) b7) << 56) |
        (((long) b6 & 0xff) << 48) |
        (((long) b5 & 0xff) << 40) |
        (((long) b4 & 0xff) << 32) |
        (((long) b3 & 0xff) << 24) |
        (((long) b2 & 0xff) << 16) |
        (((long) b1 & 0xff) << 8) |
        (((long) b0 & 0xff)));
  }

  public static double makeDouble(byte b7, byte b6, byte b5, byte b4,
      byte b3, byte b2, byte b1, byte b0) {
    return Double.longBitsToDouble(makeLong(b7, b6, b5, b4, b3, b2, b1, b0));
  }

  public static int makeInt(byte b3, byte b2, byte b1, byte b0) {
    return (((b3) << 24) |
        ((b2 & 0xff) << 16) |
        ((b1 & 0xff) << 8) |
        ((b0 & 0xff)));
  }

  public static float makeFloat(byte b3, byte b2, byte b1, byte b0) {
    return Float.intBitsToFloat(makeInt(b3, b2, b1, b0));
  }
  /**
   * 根据方法描述符解析得到方法参数占用的 slot 大小
   * @param descriptor 方法描述符
   * @return size of method descriptor
   * ()V => 0
   * (JDI)V => 5
   * ([I[[I[Ljava/lang/String;)V => 3
   */
  public static int getArgSlotSize(String descriptor) {
    int slots = 0;
    final int len = descriptor.length();
    for (int i = 1; i < len; i++) { // 从 1 开始，跳过起始的 (
      final char ch = descriptor.charAt(i);
      if (ch == ')') { // 以 ）结束。
        break;
      }
      if (ch == 'J' || ch == 'D') { // long , double 占两个 slot
        slots += 2;
      } else {
        if (ch == '[') { // 数组
          while (descriptor.charAt(i + 1) == '[') { // 多维数组
            i++;
          }
          i++;
        }
        if (descriptor.charAt(i) == 'L') { // 类
          while (descriptor.charAt(i + 1) != ';') { // 类结束为 ；
            i++;
          }
          i++;
        }
        slots += 1; // 除了 long , double 都占用 1 个 slot
      }
    }
    return slots;
  }

  /**
   * 类初始化
   */
  public static void clinit(Class clazz) {
    // 类在初始化中，或已完成初始化，直接返回
    if (clazz.stat >= Const.CLASS_INITING) {
      return;
    }

    // 递归初始化父类
    if (clazz.superClass != null) {
      clinit(clazz.superClass);
    }

    // 初始化包含默认方法的接口
    for (Class inter : clazz.interfaces) {
      // 初始化接口的接口
      for (Class superInter : inter.interfaces) {
        if (superInter.hasDefaultMethod) {
          clinit(superInter);
        }
      }
      // 初始化当前接口
      if (inter.hasDefaultMethod) {
        clinit(inter);
      }
    }

    // 初始化自身
    final Method clinitMethod = clazz.getSpecialStaticMethod("<clinit>", "()V");
    // 存在没有类初始化的情况
    if (clinitMethod == null) {
      clazz.stat = Const.CLASS_INITED;
      return;
    }

    clazz.stat = Const.CLASS_INITING;
    Frame newFrame = new Frame(clinitMethod);
    Interpreter.execute(newFrame);
    clazz.stat = Const.CLASS_INITED;
  }

  /**
   * 返回操作
   * @param slotSize 返回值占用 slot 大小
   */
  public static void doReturn(int slotSize) {
    final ExecEnv env = MetaSpace.getMainEnv();
    final Frame old = env.popFrame();

    // 解释器同步执行方法的结束条件
    if (old.stat == Const.FAKE_FRAME) {
      old.stat = Const.FAKE_FRAME_END;
    }

    if (slotSize == 0) {
      return;
    }

    final Frame now = env.topFrame();
    if (slotSize == 1) {
      now.push(old.pop());
      return;
    }

    if (slotSize == 2) {
      final Slot v2 = old.pop();
      final Slot v1 = old.pop();
      now.push(v1);
      now.push(v2);
      return;
    }

    throw new IllegalStateException();
  }

  public static void doReturn0() {
    doReturn(0);
  }

  public static void doReturn1() {
    doReturn(1);
  }

  public static void doReturn2() {
    doReturn(2);
  }

  public static void invokeMethod(Method method) {
    if (Utils.isNative(method.accessFlags)) {
      final String key = method.getKey();
      NativeMethod nm = MetaSpace.findNativeMethod(key);
      if (nm == null) {
        throw new IllegalStateException("not found native method: " + key);
      }
      nm.invoke(MetaSpace.getMainEnv().topFrame());
    } else {
      Frame newFrame = new Frame(method);
      final ExecEnv env = MetaSpace.getMainEnv();
      final Frame old = env.topFrame();

      // 传参
      final int slots = method.getArgSlotSize();
      for (int i = slots - 1; i >= 0; i--) {
        newFrame.set(i, old.pop());
      }

      if (!Utils.isStatic(method.accessFlags)) {
        // check npe
        if (Utils.checkNullPointException(newFrame.getRef(0))) {
          return;
        }
      }

      env.pushFrame(newFrame);
    }
  }

  /**
   * 创建 java/lang/String 实例
   */
  public static Instance createString(String str) {
    final Class cls = MetaSpace.getClassLoader().findClass("java/lang/String");
    final Instance instance = cls.newInstance();

    final Field field = instance.getField("value", "[C");
    final PrimitiveArray pa = PrimitiveArray.charArray(str.length());
    for (int i = 0; i < str.length(); i++) {
      pa.ints[i] = str.charAt(i);
    }
    UnionSlot us = UnionSlot.of(pa);
    field.set(us);

    return instance;
  }

  /**
   * 从 java/lang/String 实例转成字符串
   */
  public static String fromString(Instance instance) {
    final Field field = instance.getField("value", "[C");
    final PrimitiveArray pa = (PrimitiveArray) field.val.getRef();
    char[] chars = new char[pa.ints.length];
    for (int i = 0; i < pa.ints.length; i++) {
      chars[i] = ((char) pa.ints[i]);
    }
    return new String(chars);
  }

  public static void handleException(Instance instance) {
    final String name = instance.clazz.name;

    final ExecEnv env = MetaSpace.getMainEnv();
    final Frame frame = env.topFrame();
    int handlerPc = frame.method.getHandlerPc(frame.pc, name);

    while (handlerPc < 0 && !env.isEmpty()) {
      env.popFrame(); // 出栈当前栈帧
      if (env.isEmpty()) {
        break;
      }
      final Frame tf = env.topFrame();
      handlerPc = tf.method.getHandlerPc(tf.pc, name);
    }

    // 没有找到匹配的异常处理项，虚拟机接手处理
    if (handlerPc < 0) {
      final StackTraceElement[] stes = (StackTraceElement[]) instance.extra;

      Instance throwableInstance = null;
      throwableInstance = instance;
      while (!throwableInstance.clazz.name.equals("java/lang/Throwable")) {
        throwableInstance = throwableInstance.superInstance;
      }

      final StringBuilder sb = new StringBuilder();
      final Field message = throwableInstance.getField("detailMessage", "Ljava/lang/String;");
      sb.append("Exception in thread \"main\" ").append(instance.clazz.name);
      if (message.val.getRef() != null) {
        sb.append(": ").append(Utils.fromString(message.val.getRef()));
      }
      sb.append("\n");
      for (int i = stes.length - 1; i >= 0; i--) {
        final StackTraceElement ste = stes[i];
        sb.append("\t").append("at ")
            .append(ste.getClassName()).append(".")
            .append(ste.getMethodName())
            .append("(")
            .append(ste.getFileName())
            .append(":")
            .append(ste.getLineNumber())
            .append(")")
            .append("\n");
      }
      final String val = sb.toString();
      System.err.print(val);
      System.exit(-1);
    }

    final Frame now = env.topFrame();
    now.clearOperandStack();
    now.pushRef(instance);
    now.pc = handlerPc;
  }

  /**
   * 判断实例是否为空
   * @return true if self is null, false if self is not null
   */
  public static boolean checkNullPointException(Instance self) {
    if (self != null) {
      return false;
    }

    final Frame top = MetaSpace.getMainEnv().topFrame();
    final Instruction instruction = top.getInstruction();
    final String name = instruction.name();
    final Instance str = Utils.createString(name);

    Class cls = MetaSpace.getClassLoader().findClass("java/lang/NullPointerException");
    Utils.clinit(cls);

    Instance instance = cls.newInstance();
    Method method = cls.getSpecialMethod("<init>", "(Ljava/lang/String;)V");
    Frame frame = new Frame(method);
    frame.setRef(0, instance);
    frame.setRef(1, str);
    Interpreter.execute(frame);

    MetaSpace.getMainEnv().exception = instance;
    return true;
  }
}
