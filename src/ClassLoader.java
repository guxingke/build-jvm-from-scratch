import java.io.*;
import java.util.*;
import java.util.zip.*;

class ClassLoader {

  String classpath;

  public ClassLoader(String classpath) {
    this.classpath = classpath;
  }
  
    /**
   * 从 jar 包中加载 class
   * @param path jar 文件路劲
   * @param name 类名
   * @return return null if not found
   */
  ClassFile loadClassFileFromJar(String path, String name) {
    ZipFile file;
    try {
      file = new ZipFile(path);
    } catch (IOException e) {
      throw new IllegalStateException("not found jar file, " + path);
    }

    ZipEntry entry = file.getEntry(name + ".class");
    if (entry == null) {
      return null;
    }

    try (InputStream is = file.getInputStream(entry);
        DataInputStream dis = new DataInputStream(is)) {
      return ClassReader.read(dis);
    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalStateException("read class failure, " + name);
    }
  }

  /**
   * 从目录加载 class
   * @param path 目录路径，不能以 '/' 结尾，e.g /home/admin/code
   * @param name 类名
   * @return return null if not found.
   */
  ClassFile loadClassFileFromDir(String path, String name) {
    if (!name.contains("/")) {
      final File dir = new File(path);
      if (!dir.exists()) {
        return null;
      }
      final String[] files = dir.list();
      if (files == null) {
        return null;
      }

      for (String file : files) {
        // found it
        if (Objects.equals(file, name + ".class")) {
          try (FileInputStream fis = new FileInputStream(new File(path + "/" + name + ".class"));
              DataInputStream dis = new DataInputStream(fis)) {
            return ClassReader.read(dis);
          } catch (Exception e) {
            throw new IllegalStateException("read class failure, " + name);
          }
        }
      }
      return null;
    }

    // 继续下一个目录
    int idx = name.indexOf("/");
    String subDir = name.substring(0, idx);
    String subPath = path + "/" + subDir;
    final String newName = name.substring(idx + 1);
    return loadClassFileFromDir(subPath, newName);
  }

  /**
   * 从 classpath 中加载 class
   * @param classpath e.g temp/test.jar:misc
   * @param name 类名
   * @return return null if not found
   */
  ClassFile loadClassFileFromClasspath(String classpath, String name) {
    ClassFile classFile = null;
    for (String path : classpath.split(":")) {
      // 2.1 jar ?
      if (path.endsWith(".jar")) {
        classFile = loadClassFileFromJar(path, name);
        if (classFile != null) {
          break;
        }
      } else {
        // dir
        classFile = loadClassFileFromDir(path, name);
        if (classFile != null) {
          break;
        }
      }
    }

    return classFile;
  }

  /**
   * 从 classpath 加载类。
   * @param name 类名，e.g java/lang/Class
   * @return {@link Class} 实例
   */
  Class findClass(String name) {
    Class cacheClass = MetaSpace.findClass(name);
    if (cacheClass != null) {
      return cacheClass;
    }

    // 数组
    if (name.startsWith("[")) {
      return findArrayClass(name);
    }

    // parse file
    ClassFile classFile = loadClassFileFromClasspath(this.classpath, name);
    if (classFile == null) {
      return null;
    }

    // defineClass
    Class clazz = defineClass(classFile);

    // link class
    linkClass(clazz);

    return clazz;
  }

  private Class findArrayClass(String name) {
    // TODO 实现 java/lang/Cloneable 接口。
    Class clazz = new Class(name, MetaSpace.findClass("java/lang/Object"), new Class[0], new Field[0], new Method[0], null, false);
    MetaSpace.putClass(name, clazz);
    linkClass(clazz);
    clazz.stat = Const.CLASS_INITED;
    return clazz;
  }

  private Class defineClass(ClassFile classFile) {
    final CpInfo[] cp = classFile.constantPool;
    String name = Utils.getClassName(cp, classFile.thisClass.val);
    // check super class
    final int superIdx = classFile.superClass.val;
    if (superIdx == 0) {
      if (!Objects.equals("java/lang/Object", name)) {
        throw new IllegalStateException("class has no super " + name);
      }
    }
    Class superClass = null;
    // load super first
    if (superIdx != 0) {
      final String superClassName = Utils.getClassName(cp, classFile.superClass.val);
      superClass = findClass(superClassName);
    }

    // interfaces
    final Class[] interfaces = new Class[classFile.interfacesCount.val];
    for (int i = 0; i < interfaces.length; i++) {
      interfaces[i] = findClass(Utils.getClassName(cp, classFile.interfaces[i].val));
    }

    // fields
    Field[] fields = new Field[classFile.fieldsCount.val];
    for (int i = 0; i < fields.length; i++) {
      final FieldInfo fieldInfo = classFile.fields[i];
      final int fieldAccessFlags = fieldInfo.accessFlags.val;
      final String fieldName = Utils.getUtf8(cp, fieldInfo.nameIndex.val);
      final String fieldDescriptor = Utils.getUtf8(cp, fieldInfo.descriptorIndex.val);
      fields[i] = new Field(fieldAccessFlags, fieldName, fieldDescriptor);
    }

    // methods
    boolean hasDefaultMethod = false;
    Method[] methods = new Method[classFile.methodsCount.val];
    for (int i = 0; i < methods.length; i++) {
      final MethodInfo methodInfo = classFile.methods[i];
      final int methodAccessFlags = methodInfo.accessFlags.val;
      if (Utils.isInterface(classFile.accessFlags.val) && (!Utils.isStatic(methodAccessFlags) && !Utils.isAbstract(methodAccessFlags))) {
        hasDefaultMethod = true;
      }
      final String methodName = Utils.getUtf8(cp, methodInfo.nameIndex.val);
      final String methodDescriptor = Utils.getUtf8(cp, methodInfo.descriptorIndex.val);
      // code
      Code code = null;
      for (AttributeInfo attribute : methodInfo.attributes) {
        final String attrName = Utils.getUtf8(cp, attribute.attributeNameIndex.val);
        if (Objects.equals("Code", attrName)) {
          final byte[] bytes = attribute.info;
          try (MyByteArrayInputStream bis = new MyByteArrayInputStream(bytes);
              final MyDataInputStream dis = new MyDataInputStream(bis)
          ) {
            final int maxStack = dis.readUnsignedShort();
            final int maxLocals = dis.readUnsignedShort();
            final int codeLength = dis.readInt();
            byte[] codeBytes = new byte[codeLength];
            dis.read(codeBytes);

            final int exceptionTableLength = dis.readUnsignedShort();
            // 异常表解析
            final ExceptionTableItem[] exceptionTable = new ExceptionTableItem[exceptionTableLength];
            if (exceptionTableLength > 0) {
              for (int j = 0; j < exceptionTableLength; j++) {
                exceptionTable[j] = new ExceptionTableItem(dis.readUnsignedShort(), dis.readUnsignedShort(),
                    dis.readUnsignedShort(), dis.readUnsignedShort());
              }
            }

            final int attributesCount = dis.readUnsignedShort();
            AttributeInfo[] attributeInfos = new AttributeInfo[attributesCount];
            for (int j = 0; j < attributeInfos.length; j++) {
              int attributeNameIndex = dis.readUnsignedShort();
              int attributeLength = dis.readInt();
              byte[] info = new byte[attributeLength];
              dis.read(info);
              attributeInfos[j] = new AttributeInfo(new U2(attributeNameIndex), new U4(attributeLength), info);
            }

            code = new Code(maxStack, maxLocals, exceptionTable, codeBytes, attributeInfos);
          } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("unknown exception");
          }
        }
      }
      methods[i] = new Method(methodAccessFlags, methodName, methodDescriptor, code);
    }

    final Class clazz = new Class(name, superClass, interfaces, fields, methods, classFile, hasDefaultMethod);
    clazz.stat = Const.CLASS_LOADED;
    MetaSpace.putClass(name, clazz);
    return clazz;
  }

  private void linkClass(Class clazz) {
    // 优先链接父类
    if (clazz.superClass != null && clazz.superClass.stat < Const.CLASS_LINKED) {
      linkClass(clazz.superClass);
    }

    // Link class
    if (MetaSpace.findClass("java/lang/Class") != null) {
      Instance mirror = MetaSpace.findClass("java/lang/Class").newInstance();
      clazz.mirror = mirror;
      mirror.metaClass = clazz;
    }
    // verification, 不实现

    // preparation
    // 静态字段的初始化
    for (Field field : clazz.fields) {
      if (Utils.isStatic(field.accessFlags)) {
        field.init();
      }
    }

    // resolution, 不实现
    clazz.stat = Const.CLASS_LINKED;
  }
}

class MyByteArrayInputStream extends ByteArrayInputStream {

  public MyByteArrayInputStream(byte[] buf) {
    super(buf);
  }

  public int getPosition() {
    return this.pos;
  }
}

class MyDataInputStream extends DataInputStream {

  public MyDataInputStream(MyByteArrayInputStream in) {
    super(in);
  }

  public int readPadding() {
    int offset = 0;
    while (((MyByteArrayInputStream) in).getPosition() % 4 != 0) {
      try {
        in.read();
        offset++;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return offset;
  }
}