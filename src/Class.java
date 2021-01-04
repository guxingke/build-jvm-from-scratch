import java.io.*;
import java.util.*;

class Class {

  ClassFile classFile;
  /**
   * class 状态
   */
  int stat;
  String name;
  Class superClass;
  Class[] interfaces;
  Field[] fields;
  Method[] methods;

  boolean hasDefaultMethod;

  // 运行时的镜子，为反射提供服务
  Instance mirror;

  String source;

  public Class(String name, Class superClass, Class[] interfaces, Field[] fields, Method[] methods, ClassFile classFile,
      boolean hasDefaultMethod) {
    this.name = name;
    this.superClass = superClass;
    this.interfaces = interfaces;
    this.fields = fields;
    this.methods = methods;
    this.classFile = classFile;
    this.hasDefaultMethod = hasDefaultMethod;

    if (fields != null) {
      for (Field field : fields) {
        field.clazz = this;
      }
    }

    if (methods != null) {
      for (Method method : methods) {
        method.clazz = this;
      }
    }
  }

  /**
   * 获取 class 文件源码名
   */
  public String getSource() {
    if (source != null) {
      return source;
    }
    for (AttributeInfo attribute : this.classFile.attributes) {
      final String attrName = Utils.getUtf8(this.classFile.constantPool, attribute.attributeNameIndex.val);
      if (attrName.equals("SourceFile")) {
        byte[] info = attribute.info;
        final int idx = (info[0] << 8) + (info[1] & 0xff);
        source = Utils.getUtf8(this.classFile.constantPool, idx);
      }
    }
    return source;
  }

  /**
   * 获取当前类的静态方法
   * @param name 方法名
   * @param descriptor 方法描述符
   * @return return null if not matchable
   */
  public Method getSpecialStaticMethod(String name, String descriptor) {
    for (Method method : methods) {
      if (!Utils.isStatic(method.accessFlags)) {
        continue;
      }
      if (Objects.equals(name, method.name) && Objects.equals(descriptor, method.descriptor)) {
        return method;
      }
    }
    return null;
  }

  /**
   * 获取静态方法, 递归寻找
   * @param name 方法名
   * @param descriptor 方法描述符
   * @return return null if not matchable
   */
  public Method getStaticMethod(String name, String descriptor) {
    final Method method = this.getSpecialStaticMethod(name, descriptor);
    if (method != null) {
      return method;
    }
    if (superClass == null) {
      return null;
    }
    return this.superClass.getStaticMethod(name, descriptor);
  }

  /**
   * 获取静态字段，递归寻找
   * @param name 字段名
   * @param descriptor 字段描述符
   * @return return null if not matchable
   */
  public Field getStaticField(String name, String descriptor) {
    for (Field field : fields) {
      if (!Utils.isStatic(field.accessFlags)) {
        continue;
      }
      if (Objects.equals(name, field.name) && Objects.equals(descriptor, field.descriptor)) {
        return field;
      }
    }

    if (superClass == null) {
      return null;
    }
    return this.superClass.getStaticField(name, descriptor);
  }

  /**
   * 实例化
   */
  public Instance newInstance() {
    // 实例字段复制
    final Field[] newFields = new Field[fields.length];
    for (int i = 0; i < newFields.length; i++) {
      final Field field = fields[i];
      Field newField;
      if (Utils.isStatic(field.accessFlags)) {
        newField = field; // 静态字段复制引用
      } else {
        // 非静态字段复制内容，并且初始化
        newField = new Field(field.accessFlags, field.name, field.descriptor);
        newField.init();
      }
      newFields[i] = newField;
    }

    final Instance instance = new Instance(this, newFields);
    if (this.superClass != null) {
      instance.superInstance = this.superClass.newInstance();
    }
    return instance;
  }

  /**
   * 在当前类获取非静态方法
   */
  public Method getSpecialMethod(String name, String descriptor) {
    for (Method method : methods) {
      if (Utils.isStatic(method.accessFlags)) {
        continue;
      }

      if (method.name.equals(name) && method.descriptor.equals(descriptor)) {
        return method;
      }
    }
    return null;
  }

  /**
   * 获取非静态方法，递归获取
   */
  public Method getVirtualMethod(String name, String descriptor) {
    final Method method = this.getSpecialMethod(name, descriptor);
    if (method != null) {
      return method;
    }
    if (superClass == null) {
      return null;
    }
    return superClass.getVirtualMethod(name, descriptor);
  }

  /**
   * 获取默认方法, 递归获取
   */
  public Method getDefaultMethod(String name, String descriptor) {
    Method method = null;
    // 如果是接口，则尝试从当前的方法类的方法表的获取
    if (Utils.isInterface(this.classFile.accessFlags.val) && this.hasDefaultMethod) {
      method = this.getSpecialMethod(name, descriptor);
      if (method != null) {
        return method;
      }
      if (interfaces.length == 0) {
        return null;
      }
    }

    // 递归从当前接口中获取
    for (Class inter : interfaces) {
      method = inter.getDefaultMethod(name, descriptor);
      if (method != null) {
        break;
      }
    }
    return method;
  }

  public boolean instanceOf(String clazz) {
    if (this.name.equals(clazz)) {
      return true;
    }
    for (Class inter : this.interfaces) {
      boolean ret = inter.instanceOf(clazz);
      if (ret) {
        return true;
      }
    }
    if (this.superClass != null) {
      return this.superClass.instanceOf(clazz);
    }
    return false;
  }

  public boolean isPrimitive() {
    switch (name) {
      case "void":
      case "boolean":
      case "byte":
      case "char":
      case "short":
      case "int":
      case "long":
      case "float":
      case "double":
        return true;
    }
    return false;
  }
}

class Field {

  int accessFlags;
  String name;
  String descriptor;
  Class clazz;
  UnionSlot val;

  public Field(int accessFlags, String name, String descriptor) {
    this.accessFlags = accessFlags;
    this.name = name;
    this.descriptor = descriptor;
  }

  public String getType() {
    if (descriptor.startsWith("L")) {
      return descriptor.substring(1, descriptor.length() - 1);
    }
    return descriptor;
  }

  /**
   * 初始化
   */
  public void init() {
    switch (descriptor) {
      case "Z":
      case "C":
      case "B":
      case "S":
      case "I":
        val = UnionSlot.of(0);
        break;
      case "J":
        val = UnionSlot.of(0L);
        break;
      case "F":
        val = UnionSlot.of(0f);
        break;
      case "D":
        val = UnionSlot.of(0d);
        break;
      default: // ref
        val = UnionSlot.of((Instance) null);
        break;
    }
  }

  /**
   * 获取字段的值，并推送到栈帧
   */
  public void get(Frame frame) {
    switch (descriptor) {
      case "Z":
      case "C":
      case "B":
      case "S":
      case "I":
        frame.pushInt(val.getInt());
        break;
      case "J":
        frame.pushLong(val.getLong());
        break;
      case "F":
        frame.pushFloat(val.getFloat());
        break;
      case "D":
        frame.pushDouble(val.getDouble());
        break;
      default: // ref
        frame.pushRef(val.getRef());
        break;
    }
  }

  /**
   * 设置字段的值，从栈帧中弹出相应的数据 */
  public void set(Frame frame) {
    switch (descriptor) {
      case "Z":
      case "C":
      case "B":
      case "S":
      case "I":
        val.setInt(frame.popInt());
        break;
      case "J":
        val.setLong(frame.popLong());
        break;
      case "F":
        val.setFloat(frame.popFloat());
        break;
      case "D":
        val.setDouble(frame.popDouble());
        break;
      default: // ref
        val.setRef(frame.popRef());
        break;
    }
  }

  /**
   * 设置为新的 slot
   */
  public void set(UnionSlot neo) {
    val.set(neo);
  }
}

class Method {

  int accessFlags;
  String name;
  String descriptor;
  Code code;
  Class clazz;

  public Method(int accessFlags, String name, String descriptor, Code code) {
    this.accessFlags = accessFlags;
    this.name = name;
    this.descriptor = descriptor;
    this.code = code;
  }

  public int getMaxLocals() {
    return this.code.maxLocals;
  }

  public int getMaxStack() {
    return this.code.maxStack;
  }

  // 指令集缓存
  Map<Integer, Instruction> instructions;

  public Map<Integer, Instruction> getInstructions() {
    // load from cache
    if (this.instructions != null) {
      return this.instructions;
    }
    final Map<Integer, Instruction> instructions = this.code.getInstructions(clazz.classFile.constantPool);
    this.instructions = instructions;
    return instructions;
  }

  public int getLine(int pc) {
    return this.code.getLine(clazz.classFile.constantPool, pc);
  }

  public int getArgSlotSize() {
    final int size = Utils.getArgSlotSize(this.descriptor);
    if (Utils.isStatic(this.accessFlags)) {
      return size;
    }
    return size + 1;
  }

  public String getKey() {
    return this.clazz.name + "_" + this.name + "_" + this.descriptor;
  }

  public int getHandlerPc(int pc, String name) {
    if (this.code.exceptionTable == null) {
      return -1;
    }
    for (ExceptionTableItem item : this.code.exceptionTable) {
      if (pc < item.startPc || pc >= item.endPc) {
        continue;
      }

      // for all exception
      if (item.catchType == 0) {
        return item.handlerPc;
      }

      if (!name.equals(Utils.getClassName(clazz.classFile.constantPool, item.catchType))) {
        continue;
      }

      return item.handlerPc;
    }
    return -1;
  }
}

class ExceptionTableItem {

  final int startPc;
  final int endPc;
  final int handlerPc;
  final int catchType;

  ExceptionTableItem(int startPc, int endPc, int handlerPc, int catchType) {
    this.startPc = startPc;
    this.endPc = endPc;
    this.handlerPc = handlerPc;
    this.catchType = catchType;
  }
}

class LineNumberTable {

  final int startPc;
  final int lineNumber;

  LineNumberTable(int startPc, int lineNumber) {
    this.startPc = startPc;
    this.lineNumber = lineNumber;
  }
}

class Code {

  int maxStack;
  int maxLocals;
  ExceptionTableItem[] exceptionTable;
  // the real code
  byte[] bytes;
  AttributeInfo[] attributes;
  LineNumberTable[] table;

  public Code(int maxStack, int maxLocals, ExceptionTableItem[] exceptionTable, byte[] bytes, AttributeInfo[] attributes) {
    this.maxStack = maxStack;
    this.maxLocals = maxLocals;
    this.exceptionTable = exceptionTable;
    this.bytes = bytes;
    this.attributes = attributes;
  }

  /**
   * 获取方法的字节码指令
   *
   * @return 字节码指令集合
   */
  public Map<Integer, Instruction> getInstructions(CpInfo[] cp) {
    List<Instruction> instructions = new ArrayList<>();
    try (MyByteArrayInputStream bis = new MyByteArrayInputStream(this.bytes);
        final MyDataInputStream dis = new MyDataInputStream(bis)
    ) {
      while (dis.available() > 0) {
        final int opcode = dis.read();
        parser(instructions, opcode, dis, cp);
      }
    } catch (IOException e) {
      throw new IllegalStateException("method code parse failure");
    }

    Map<Integer, Instruction> instructionMap = new LinkedHashMap<>();
    int pc = 0;
    for (Instruction instruction : instructions) {
      instructionMap.put(pc, instruction);
      pc += instruction.offset();
    }
    return instructionMap;
  }

  public int getLine(CpInfo[] cp, int pc) {
    if (this.table != null) {
      return getLine(this.table, pc);
    }

    LineNumberTable[] table = null;
    for (AttributeInfo attribute : this.attributes) {
      final String attrName = Utils.getUtf8(cp, attribute.attributeNameIndex.val);
      if (attrName.equals("LineNumberTable")) {
        try (MyByteArrayInputStream bis = new MyByteArrayInputStream(attribute.info);
            final MyDataInputStream dis = new MyDataInputStream(bis)
        ) {
          final int len = dis.readUnsignedShort();
          table = new LineNumberTable[len];
          for (int i = 0; i < table.length; i++) {
            table[i] = new LineNumberTable(dis.readUnsignedShort(), dis.readUnsignedShort());
          }
        } catch (IOException e) {
          e.printStackTrace();
          throw new IllegalStateException("unknown exception");
        }
      }
    }
    if (table == null) {
      return -1;
    }
    this.table = table;
    // not found
    return getLine(this.table, pc);
  }

  public int getLine(LineNumberTable[] table, int pc) {
    int line = table[0].lineNumber;
    for (int i = 1; i < table.length; i++) {
      if (pc < table[i].startPc) {
        return line;
      }
      line = table[i].lineNumber;
    }
    return line;
  }

  private void parser(List<Instruction> instructions, int opcode, DataInputStream dis, CpInfo[] cp) throws
      IOException {
    switch (opcode) {
      case Const.OPC_NOP:
        instructions.add(NopInst.parse(dis, cp));
        break;
      case Const.OPC_ACONST_NULL:
        instructions.add(Aconst_nullInst.parse(dis, cp));
        break;
      case Const.OPC_ICONST_M1:
        instructions.add(Iconst_m1Inst.parse(dis, cp));
        break;
      case Const.OPC_ICONST_0:
        instructions.add(Iconst_0Inst.parse(dis, cp));
        break;
      case Const.OPC_ICONST_1:
        instructions.add(Iconst_1Inst.parse(dis, cp));
        break;
      case Const.OPC_ICONST_2:
        instructions.add(Iconst_2Inst.parse(dis, cp));
        break;
      case Const.OPC_ICONST_3:
        instructions.add(Iconst_3Inst.parse(dis, cp));
        break;
      case Const.OPC_ICONST_4:
        instructions.add(Iconst_4Inst.parse(dis, cp));
        break;
      case Const.OPC_ICONST_5:
        instructions.add(Iconst_5Inst.parse(dis, cp));
        break;
      case Const.OPC_LCONST_0:
        instructions.add(Lconst_0Inst.parse(dis, cp));
        break;
      case Const.OPC_LCONST_1:
        instructions.add(Lconst_1Inst.parse(dis, cp));
        break;
      case Const.OPC_FCONST_0:
        instructions.add(Fconst_0Inst.parse(dis, cp));
        break;
      case Const.OPC_FCONST_1:
        instructions.add(Fconst_1Inst.parse(dis, cp));
        break;
      case Const.OPC_FCONST_2:
        instructions.add(Fconst_2Inst.parse(dis, cp));
        break;
      case Const.OPC_DCONST_0:
        instructions.add(Dconst_0Inst.parse(dis, cp));
        break;
      case Const.OPC_DCONST_1:
        instructions.add(Dconst_1Inst.parse(dis, cp));
        break;
      case Const.OPC_BIPUSH:
        instructions.add(BipushInst.parse(dis, cp));
        break;
      case Const.OPC_SIPUSH:
        instructions.add(SipushInst.parse(dis, cp));
        break;
      case Const.OPC_LDC:
        instructions.add(LdcInst.parse(dis, cp));
        break;
      case Const.OPC_LDC_W:
        instructions.add(Ldc_wInst.parse(dis, cp));
        break;
      case Const.OPC_LDC2_W:
        instructions.add(Ldc2_wInst.parse(dis, cp));
        break;
      case Const.OPC_ILOAD:
        instructions.add(IloadInst.parse(dis, cp));
        break;
      case Const.OPC_LLOAD:
        instructions.add(LloadInst.parse(dis, cp));
        break;
      case Const.OPC_FLOAD:
        instructions.add(FloadInst.parse(dis, cp));
        break;
      case Const.OPC_DLOAD:
        instructions.add(DloadInst.parse(dis, cp));
        break;
      case Const.OPC_ALOAD:
        instructions.add(AloadInst.parse(dis, cp));
        break;
      case Const.OPC_ILOAD_0:
        instructions.add(Iload_0Inst.parse(dis, cp));
        break;
      case Const.OPC_ILOAD_1:
        instructions.add(Iload_1Inst.parse(dis, cp));
        break;
      case Const.OPC_ILOAD_2:
        instructions.add(Iload_2Inst.parse(dis, cp));
        break;
      case Const.OPC_ILOAD_3:
        instructions.add(Iload_3Inst.parse(dis, cp));
        break;
      case Const.OPC_LLOAD_0:
        instructions.add(Lload_0Inst.parse(dis, cp));
        break;
      case Const.OPC_LLOAD_1:
        instructions.add(Lload_1Inst.parse(dis, cp));
        break;
      case Const.OPC_LLOAD_2:
        instructions.add(Lload_2Inst.parse(dis, cp));
        break;
      case Const.OPC_LLOAD_3:
        instructions.add(Lload_3Inst.parse(dis, cp));
        break;
      case Const.OPC_FLOAD_0:
        instructions.add(Fload_0Inst.parse(dis, cp));
        break;
      case Const.OPC_FLOAD_1:
        instructions.add(Fload_1Inst.parse(dis, cp));
        break;
      case Const.OPC_FLOAD_2:
        instructions.add(Fload_2Inst.parse(dis, cp));
        break;
      case Const.OPC_FLOAD_3:
        instructions.add(Fload_3Inst.parse(dis, cp));
        break;
      case Const.OPC_DLOAD_0:
        instructions.add(Dload_0Inst.parse(dis, cp));
        break;
      case Const.OPC_DLOAD_1:
        instructions.add(Dload_1Inst.parse(dis, cp));
        break;
      case Const.OPC_DLOAD_2:
        instructions.add(Dload_2Inst.parse(dis, cp));
        break;
      case Const.OPC_DLOAD_3:
        instructions.add(Dload_3Inst.parse(dis, cp));
        break;
      case Const.OPC_ALOAD_0:
        instructions.add(Aload_0Inst.parse(dis, cp));
        break;
      case Const.OPC_ALOAD_1:
        instructions.add(Aload_1Inst.parse(dis, cp));
        break;
      case Const.OPC_ALOAD_2:
        instructions.add(Aload_2Inst.parse(dis, cp));
        break;
      case Const.OPC_ALOAD_3:
        instructions.add(Aload_3Inst.parse(dis, cp));
        break;
      case Const.OPC_IALOAD:
        instructions.add(IaloadInst.parse(dis, cp));
        break;
      case Const.OPC_LALOAD:
        instructions.add(LaloadInst.parse(dis, cp));
        break;
      case Const.OPC_FALOAD:
        instructions.add(FaloadInst.parse(dis, cp));
        break;
      case Const.OPC_DALOAD:
        instructions.add(DaloadInst.parse(dis, cp));
        break;
      case Const.OPC_AALOAD:
        instructions.add(AaloadInst.parse(dis, cp));
        break;
      case Const.OPC_BALOAD:
        instructions.add(BaloadInst.parse(dis, cp));
        break;
      case Const.OPC_CALOAD:
        instructions.add(CaloadInst.parse(dis, cp));
        break;
      case Const.OPC_SALOAD:
        instructions.add(SaloadInst.parse(dis, cp));
        break;
      case Const.OPC_ISTORE:
        instructions.add(IstoreInst.parse(dis, cp));
        break;
      case Const.OPC_LSTORE:
        instructions.add(LstoreInst.parse(dis, cp));
        break;
      case Const.OPC_FSTORE:
        instructions.add(FstoreInst.parse(dis, cp));
        break;
      case Const.OPC_DSTORE:
        instructions.add(DstoreInst.parse(dis, cp));
        break;
      case Const.OPC_ASTORE:
        instructions.add(AstoreInst.parse(dis, cp));
        break;
      case Const.OPC_ISTORE_0:
        instructions.add(Istore_0Inst.parse(dis, cp));
        break;
      case Const.OPC_ISTORE_1:
        instructions.add(Istore_1Inst.parse(dis, cp));
        break;
      case Const.OPC_ISTORE_2:
        instructions.add(Istore_2Inst.parse(dis, cp));
        break;
      case Const.OPC_ISTORE_3:
        instructions.add(Istore_3Inst.parse(dis, cp));
        break;
      case Const.OPC_LSTORE_0:
        instructions.add(Lstore_0Inst.parse(dis, cp));
        break;
      case Const.OPC_LSTORE_1:
        instructions.add(Lstore_1Inst.parse(dis, cp));
        break;
      case Const.OPC_LSTORE_2:
        instructions.add(Lstore_2Inst.parse(dis, cp));
        break;
      case Const.OPC_LSTORE_3:
        instructions.add(Lstore_3Inst.parse(dis, cp));
        break;
      case Const.OPC_FSTORE_0:
        instructions.add(Fstore_0Inst.parse(dis, cp));
        break;
      case Const.OPC_FSTORE_1:
        instructions.add(Fstore_1Inst.parse(dis, cp));
        break;
      case Const.OPC_FSTORE_2:
        instructions.add(Fstore_2Inst.parse(dis, cp));
        break;
      case Const.OPC_FSTORE_3:
        instructions.add(Fstore_3Inst.parse(dis, cp));
        break;
      case Const.OPC_DSTORE_0:
        instructions.add(Dstore_0Inst.parse(dis, cp));
        break;
      case Const.OPC_DSTORE_1:
        instructions.add(Dstore_1Inst.parse(dis, cp));
        break;
      case Const.OPC_DSTORE_2:
        instructions.add(Dstore_2Inst.parse(dis, cp));
        break;
      case Const.OPC_DSTORE_3:
        instructions.add(Dstore_3Inst.parse(dis, cp));
        break;
      case Const.OPC_ASTORE_0:
        instructions.add(Astore_0Inst.parse(dis, cp));
        break;
      case Const.OPC_ASTORE_1:
        instructions.add(Astore_1Inst.parse(dis, cp));
        break;
      case Const.OPC_ASTORE_2:
        instructions.add(Astore_2Inst.parse(dis, cp));
        break;
      case Const.OPC_ASTORE_3:
        instructions.add(Astore_3Inst.parse(dis, cp));
        break;
      case Const.OPC_IASTORE:
        instructions.add(IastoreInst.parse(dis, cp));
        break;
      case Const.OPC_LASTORE:
        instructions.add(LastoreInst.parse(dis, cp));
        break;
      case Const.OPC_FASTORE:
        instructions.add(FastoreInst.parse(dis, cp));
        break;
      case Const.OPC_DASTORE:
        instructions.add(DastoreInst.parse(dis, cp));
        break;
      case Const.OPC_AASTORE:
        instructions.add(AastoreInst.parse(dis, cp));
        break;
      case Const.OPC_BASTORE:
        instructions.add(BastoreInst.parse(dis, cp));
        break;
      case Const.OPC_CASTORE:
        instructions.add(CastoreInst.parse(dis, cp));
        break;
      case Const.OPC_SASTORE:
        instructions.add(SastoreInst.parse(dis, cp));
        break;
      case Const.OPC_POP:
        instructions.add(PopInst.parse(dis, cp));
        break;
      case Const.OPC_POP2:
        instructions.add(Pop2Inst.parse(dis, cp));
        break;
      case Const.OPC_DUP:
        instructions.add(DupInst.parse(dis, cp));
        break;
      case Const.OPC_DUP_X1:
        instructions.add(Dup_x1Inst.parse(dis, cp));
        break;
      case Const.OPC_DUP_X2:
        instructions.add(Dup_x2Inst.parse(dis, cp));
        break;
      case Const.OPC_DUP2:
        instructions.add(Dup2Inst.parse(dis, cp));
        break;
      case Const.OPC_DUP2_X1:
        instructions.add(Dup2_x1Inst.parse(dis, cp));
        break;
      case Const.OPC_DUP2_X2:
        instructions.add(Dup2_x2Inst.parse(dis, cp));
        break;
      case Const.OPC_SWAP:
        instructions.add(SwapInst.parse(dis, cp));
        break;
      case Const.OPC_IADD:
        instructions.add(IaddInst.parse(dis, cp));
        break;
      case Const.OPC_LADD:
        instructions.add(LaddInst.parse(dis, cp));
        break;
      case Const.OPC_FADD:
        instructions.add(FaddInst.parse(dis, cp));
        break;
      case Const.OPC_DADD:
        instructions.add(DaddInst.parse(dis, cp));
        break;
      case Const.OPC_ISUB:
        instructions.add(IsubInst.parse(dis, cp));
        break;
      case Const.OPC_LSUB:
        instructions.add(LsubInst.parse(dis, cp));
        break;
      case Const.OPC_FSUB:
        instructions.add(FsubInst.parse(dis, cp));
        break;
      case Const.OPC_DSUB:
        instructions.add(DsubInst.parse(dis, cp));
        break;
      case Const.OPC_IMUL:
        instructions.add(ImulInst.parse(dis, cp));
        break;
      case Const.OPC_LMUL:
        instructions.add(LmulInst.parse(dis, cp));
        break;
      case Const.OPC_FMUL:
        instructions.add(FmulInst.parse(dis, cp));
        break;
      case Const.OPC_DMUL:
        instructions.add(DmulInst.parse(dis, cp));
        break;
      case Const.OPC_IDIV:
        instructions.add(IdivInst.parse(dis, cp));
        break;
      case Const.OPC_LDIV:
        instructions.add(LdivInst.parse(dis, cp));
        break;
      case Const.OPC_FDIV:
        instructions.add(FdivInst.parse(dis, cp));
        break;
      case Const.OPC_DDIV:
        instructions.add(DdivInst.parse(dis, cp));
        break;
      case Const.OPC_IREM:
        instructions.add(IremInst.parse(dis, cp));
        break;
      case Const.OPC_LREM:
        instructions.add(LremInst.parse(dis, cp));
        break;
      case Const.OPC_FREM:
        instructions.add(FremInst.parse(dis, cp));
        break;
      case Const.OPC_DREM:
        instructions.add(DremInst.parse(dis, cp));
        break;
      case Const.OPC_INEG:
        instructions.add(InegInst.parse(dis, cp));
        break;
      case Const.OPC_LNEG:
        instructions.add(LnegInst.parse(dis, cp));
        break;
      case Const.OPC_FNEG:
        instructions.add(FnegInst.parse(dis, cp));
        break;
      case Const.OPC_DNEG:
        instructions.add(DnegInst.parse(dis, cp));
        break;
      case Const.OPC_ISHL:
        instructions.add(IshlInst.parse(dis, cp));
        break;
      case Const.OPC_LSHL:
        instructions.add(LshlInst.parse(dis, cp));
        break;
      case Const.OPC_ISHR:
        instructions.add(IshrInst.parse(dis, cp));
        break;
      case Const.OPC_LSHR:
        instructions.add(LshrInst.parse(dis, cp));
        break;
      case Const.OPC_IUSHR:
        instructions.add(IushrInst.parse(dis, cp));
        break;
      case Const.OPC_LUSHR:
        instructions.add(LushrInst.parse(dis, cp));
        break;
      case Const.OPC_IAND:
        instructions.add(IandInst.parse(dis, cp));
        break;
      case Const.OPC_LAND:
        instructions.add(LandInst.parse(dis, cp));
        break;
      case Const.OPC_IOR:
        instructions.add(IorInst.parse(dis, cp));
        break;
      case Const.OPC_LOR:
        instructions.add(LorInst.parse(dis, cp));
        break;
      case Const.OPC_IXOR:
        instructions.add(IxorInst.parse(dis, cp));
        break;
      case Const.OPC_LXOR:
        instructions.add(LxorInst.parse(dis, cp));
        break;
      case Const.OPC_IINC:
        instructions.add(IincInst.parse(dis, cp));
        break;
      case Const.OPC_I2L:
        instructions.add(I2lInst.parse(dis, cp));
        break;
      case Const.OPC_I2F:
        instructions.add(I2fInst.parse(dis, cp));
        break;
      case Const.OPC_I2D:
        instructions.add(I2dInst.parse(dis, cp));
        break;
      case Const.OPC_L2I:
        instructions.add(L2iInst.parse(dis, cp));
        break;
      case Const.OPC_L2F:
        instructions.add(L2fInst.parse(dis, cp));
        break;
      case Const.OPC_L2D:
        instructions.add(L2dInst.parse(dis, cp));
        break;
      case Const.OPC_F2I:
        instructions.add(F2iInst.parse(dis, cp));
        break;
      case Const.OPC_F2L:
        instructions.add(F2lInst.parse(dis, cp));
        break;
      case Const.OPC_F2D:
        instructions.add(F2dInst.parse(dis, cp));
        break;
      case Const.OPC_D2I:
        instructions.add(D2iInst.parse(dis, cp));
        break;
      case Const.OPC_D2L:
        instructions.add(D2lInst.parse(dis, cp));
        break;
      case Const.OPC_D2F:
        instructions.add(D2fInst.parse(dis, cp));
        break;
      case Const.OPC_I2B:
        instructions.add(I2bInst.parse(dis, cp));
        break;
      case Const.OPC_I2C:
        instructions.add(I2cInst.parse(dis, cp));
        break;
      case Const.OPC_I2S:
        instructions.add(I2sInst.parse(dis, cp));
        break;
      case Const.OPC_LCMP:
        instructions.add(LcmpInst.parse(dis, cp));
        break;
      case Const.OPC_FCMPL:
        instructions.add(FcmplInst.parse(dis, cp));
        break;
      case Const.OPC_FCMPG:
        instructions.add(FcmpgInst.parse(dis, cp));
        break;
      case Const.OPC_DCMPL:
        instructions.add(DcmplInst.parse(dis, cp));
        break;
      case Const.OPC_DCMPG:
        instructions.add(DcmpgInst.parse(dis, cp));
        break;
      case Const.OPC_IFEQ:
        instructions.add(IfeqInst.parse(dis, cp));
        break;
      case Const.OPC_IFNE:
        instructions.add(IfneInst.parse(dis, cp));
        break;
      case Const.OPC_IFLT:
        instructions.add(IfltInst.parse(dis, cp));
        break;
      case Const.OPC_IFGE:
        instructions.add(IfgeInst.parse(dis, cp));
        break;
      case Const.OPC_IFGT:
        instructions.add(IfgtInst.parse(dis, cp));
        break;
      case Const.OPC_IFLE:
        instructions.add(IfleInst.parse(dis, cp));
        break;
      case Const.OPC_IF_ICMPEQ:
        instructions.add(If_icmpeqInst.parse(dis, cp));
        break;
      case Const.OPC_IF_ICMPNE:
        instructions.add(If_icmpneInst.parse(dis, cp));
        break;
      case Const.OPC_IF_ICMPLT:
        instructions.add(If_icmpltInst.parse(dis, cp));
        break;
      case Const.OPC_IF_ICMPGE:
        instructions.add(If_icmpgeInst.parse(dis, cp));
        break;
      case Const.OPC_IF_ICMPGT:
        instructions.add(If_icmpgtInst.parse(dis, cp));
        break;
      case Const.OPC_IF_ICMPLE:
        instructions.add(If_icmpleInst.parse(dis, cp));
        break;
      case Const.OPC_IF_ACMPEQ:
        instructions.add(If_acmpeqInst.parse(dis, cp));
        break;
      case Const.OPC_IF_ACMPNE:
        instructions.add(If_acmpneInst.parse(dis, cp));
        break;
      case Const.OPC_GOTO:
        instructions.add(GotoInst.parse(dis, cp));
        break;
      case Const.OPC_JSR:
        instructions.add(JsrInst.parse(dis, cp));
        break;
      case Const.OPC_RET:
        instructions.add(RetInst.parse(dis, cp));
        break;
      case Const.OPC_TABLESWITCH:
        instructions.add(TableswitchInst.parse(dis, cp));
        break;
      case Const.OPC_LOOKUPSWITCH:
        instructions.add(LookupswitchInst.parse(dis, cp));
        break;
      case Const.OPC_IRETURN:
        instructions.add(IreturnInst.parse(dis, cp));
        break;
      case Const.OPC_LRETURN:
        instructions.add(LreturnInst.parse(dis, cp));
        break;
      case Const.OPC_FRETURN:
        instructions.add(FreturnInst.parse(dis, cp));
        break;
      case Const.OPC_DRETURN:
        instructions.add(DreturnInst.parse(dis, cp));
        break;
      case Const.OPC_ARETURN:
        instructions.add(AreturnInst.parse(dis, cp));
        break;
      case Const.OPC_RETURN:
        instructions.add(ReturnInst.parse(dis, cp));
        break;
      case Const.OPC_GETSTATIC:
        instructions.add(GetstaticInst.parse(dis, cp));
        break;
      case Const.OPC_PUTSTATIC:
        instructions.add(PutstaticInst.parse(dis, cp));
        break;
      case Const.OPC_GETFIELD:
        instructions.add(GetfieldInst.parse(dis, cp));
        break;
      case Const.OPC_PUTFIELD:
        instructions.add(PutfieldInst.parse(dis, cp));
        break;
      case Const.OPC_INVOKEVIRTUAL:
        instructions.add(InvokevirtualInst.parse(dis, cp));
        break;
      case Const.OPC_INVOKESPECIAL:
        instructions.add(InvokespecialInst.parse(dis, cp));
        break;
      case Const.OPC_INVOKESTATIC:
        instructions.add(InvokestaticInst.parse(dis, cp));
        break;
      case Const.OPC_INVOKEINTERFACE:
        instructions.add(InvokeinterfaceInst.parse(dis, cp));
        break;
      case Const.OPC_INVOKEDYNAMIC:
        instructions.add(InvokedynamicInst.parse(dis, cp));
        break;
      case Const.OPC_NEW:
        instructions.add(NewInst.parse(dis, cp));
        break;
      case Const.OPC_NEWARRAY:
        instructions.add(NewarrayInst.parse(dis, cp));
        break;
      case Const.OPC_ANEWARRAY:
        instructions.add(AnewarrayInst.parse(dis, cp));
        break;
      case Const.OPC_ARRAYLENGTH:
        instructions.add(ArraylengthInst.parse(dis, cp));
        break;
      case Const.OPC_ATHROW:
        instructions.add(AthrowInst.parse(dis, cp));
        break;
      case Const.OPC_CHECKCAST:
        instructions.add(CheckcastInst.parse(dis, cp));
        break;
      case Const.OPC_INSTANCEOF:
        instructions.add(InstanceofInst.parse(dis, cp));
        break;
      case Const.OPC_MONITORENTER:
        instructions.add(MonitorenterInst.parse(dis, cp));
        break;
      case Const.OPC_MONITOREXIT:
        instructions.add(MonitorexitInst.parse(dis, cp));
        break;
      case Const.OPC_WIDE:
        instructions.add(WideInst.parse(dis, cp));
        break;
      case Const.OPC_MULTIANEWARRAY:
        instructions.add(MultianewarrayInst.parse(dis, cp));
        break;
      case Const.OPC_IFNULL:
        instructions.add(IfnullInst.parse(dis, cp));
        break;
      case Const.OPC_IFNONNULL:
        instructions.add(IfnonnullInst.parse(dis, cp));
        break;
      case Const.OPC_GOTO_W:
        instructions.add(Goto_wInst.parse(dis, cp));
        break;
      case Const.OPC_JSR_W:
        instructions.add(Jsr_wInst.parse(dis, cp));
        break;
      case Const.OPC_BREAKPOINT:
        instructions.add(BreakpointInst.parse(dis, cp));
        break;
      case Const.OPC_IMPDEP1:
        instructions.add(Impdep1Inst.parse(dis, cp));
        break;
      case Const.OPC_IMPDEP2:
        instructions.add(Impdep2Inst.parse(dis, cp));
        break;

      default:
        throw new IllegalStateException("unknown opcode " + opcode);
    }
  }
}
