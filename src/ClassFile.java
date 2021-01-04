import java.io.*;
import java.util.*;

// https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.1
class ClassFile {

  U4 magic;
  U2 minorVersion;
  U2 majorVersion;
  U2 constantPoolCount;
  CpInfo[] constantPool;
  U2 accessFlags;
  U2 thisClass;
  U2 superClass;
  U2 interfacesCount;
  U2[] interfaces;
  U2 fieldsCount;
  FieldInfo[] fields;
  U2 methodsCount;
  MethodInfo[] methods;
  U2 attributesCount;
  AttributeInfo[] attributes;

  public ClassFile(U4 magic,
      U2 minorVersion,
      U2 majorVersion,
      U2 constantPoolCount,
      CpInfo[] constantPool,
      U2 accessFlags,
      U2 thisClass,
      U2 superClass,
      U2 interfacesCount,
      U2[] interfaces,
      U2 fieldsCount,
      FieldInfo[] fields,
      U2 methodsCount,
      MethodInfo[] methods,
      U2 attributesCount,
      AttributeInfo[] attributes
  ) {
    this.magic = magic;
    this.minorVersion = minorVersion;
    this.majorVersion = majorVersion;
    this.constantPoolCount = constantPoolCount;
    this.constantPool = constantPool;
    this.accessFlags = accessFlags;
    this.thisClass = thisClass;
    this.superClass = superClass;
    this.interfacesCount = interfacesCount;
    this.interfaces = interfaces;
    this.fieldsCount = fieldsCount;
    this.fields = fields;
    this.methodsCount = methodsCount;
    this.methods = methods;
    this.attributesCount = attributesCount;
    this.attributes = attributes;
  }
}

// https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4
class CpInfo {

  U1 tag;
  byte[] info;

  public CpInfo(U1 tag, byte[] info) {
    this.info = info;
    this.tag = tag;
  }
}

// https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.5
class FieldInfo {

  U2 accessFlags;
  U2 nameIndex;
  U2 descriptorIndex;
  U2 attributesCount;
  AttributeInfo[] attributes;

  public FieldInfo(U2 accessFlags,
      U2 nameIndex,
      U2 descriptorIndex,
      U2 attributesCount,
      AttributeInfo[] attributes) {
    this.accessFlags = accessFlags;
    this.nameIndex = nameIndex;
    this.descriptorIndex = descriptorIndex;
    this.attributesCount = attributesCount;
    this.attributes = attributes;
  }
}

// https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.6
class MethodInfo {

  U2 accessFlags;
  U2 nameIndex;
  U2 descriptorIndex;
  U2 attributesCount;
  AttributeInfo[] attributes;

  public MethodInfo(U2 accessFlags,
      U2 nameIndex,
      U2 descriptorIndex,
      U2 attributesCount,
      AttributeInfo[] attributes) {
    this.accessFlags = accessFlags;
    this.nameIndex = nameIndex;
    this.descriptorIndex = descriptorIndex;
    this.attributesCount = attributesCount;
    this.attributes = attributes;
  }
}

// https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7
class AttributeInfo {

  U2 attributeNameIndex;
  U4 attributeLength;
  byte[] info;

  AttributeInfo(U2 attributeNameIndex, U4 attributeLength, byte[] info) {
    this.attributeNameIndex = attributeNameIndex;
    this.attributeLength = attributeLength;
    this.info = info;
  }
}

// U1, 对应 class file 最小单位，占用空间 8 bit, 实际上就是个 byte.
class U1 {

  public final int val;

  U1(int val) {
    this.val = val;
  }
}

// U2, 两个 byte， 实际上是 无符号 short 类型
class U2 {

  public final int val;

  U2(int val) {
    this.val = val;
  }
}

// U4， 四个 byte, 实际上是无符号 int 类型
class U4 {

  public final int val;

  U4(int val) {
    this.val = val;
  }
}
