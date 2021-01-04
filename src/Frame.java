import java.util.Map;

// 栈帧
class Frame {

  // 程序计数器, 默认值为 0
  public int pc;

  // 本地变量表
  public final Slot[] localVars;

  // 操作数栈
  public final Slot[] operandStack;
  // 操作数栈顶位置
  private int operandTop;
  public int stat;

  public Frame(int maxLocals, int maxStack) {
    this.localVars = new Slot[maxLocals];
    this.operandStack = new Slot[maxStack];
    this.pc = 0;
    this.operandTop = 0;
  }

  // 栈帧对应方法
  public Method method;

  public Frame(Method method) {
    this.localVars = new Slot[method.getMaxLocals()];
    this.operandStack = new Slot[method.getMaxStack()];
    this.pc = 0;
    this.operandTop = 0;
    this.method = method;
  }

  // 获取当前指令
  public Instruction getInstruction() {
    final Map<Integer, Instruction> instructions = this.method.getInstructions();
    return instructions.get(pc);
  }

  // 获取当前指令所对应的行号
  public int getLine() {
    return this.method.getLine(pc);
  }

  // 获取当前方法类所对应的源文件
  public String getSource() {
    return this.method.clazz.getSource();
  }

  /**
   * 操作数栈, 推送新值
   */
  public void push(Slot slot) {
    this.operandStack[operandTop++] = slot;
  }

  /**
   * 操作数栈, 弹出栈顶
   */
  public Slot pop() {
    final Slot slot = this.operandStack[--operandTop];
    this.operandStack[operandTop] = null;
    return slot;
  }

  public void pushInt(int val) {
    this.push(Slot.val(val));
  }

  public int popInt() {
    return this.pop().val;
  }

  public void pushFloat(float val) {
    int tmp = Float.floatToIntBits(val);
    this.pushInt(tmp);
  }

  public float popFloat() {
    final int tmp = this.popInt();
    return Float.intBitsToFloat(tmp);
  }

  public void pushLong(long val) {
    int high = (int) (val >> 32); //高32位
    int low = (int) (val & 0x00000000ffffffffL); //低32位

    this.pushInt(high);
    this.pushInt(low);
  }

  public long popLong() {
    final int low = this.popInt();
    final int high = this.popInt();
    long l1 = (high & 0x00000000ffffffffL) << 32;
    long l2 = low & 0x00000000ffffffffL;
    return l1 | l2;
  }

  public void pushDouble(double val) {
    final long tmp = Double.doubleToLongBits(val);
    this.pushLong(tmp);
  }

  public double popDouble() {
    final long tmp = this.popLong();
    return Double.longBitsToDouble(tmp);
  }

  public void pushRef(Instance ref) {
    this.push(Slot.ref(ref));
  }

  public Instance popRef() {
    return this.pop().ref;
  }

  /**
   * 本地变量表，设值
   */
  public void set(int idx, Slot val) {
    this.localVars[idx] = val;
  }

  /**
   * 本地变量表，取值
   */
  public Slot get(int idx) {
    return this.localVars[idx];
  }

  public void setInt(int idx, int val) {
    this.set(idx, Slot.val(val));
  }

  public int getInt(int idx) {
    return this.get(idx).val;
  }

  public void setFloat(int idx, float val) {
    int tmp = Float.floatToIntBits(val);
    this.setInt(idx, tmp);
  }

  public float getFloat(int idx) {
    final int tmp = this.getInt(idx);
    return Float.intBitsToFloat(tmp);
  }

  public void setLong(int idx, long val) {
    int high = (int) (val >> 32); //高32位
    int low = (int) (val & 0x000000ffffffffL); //低32位

    this.setInt(idx, high);
    this.setInt(idx + 1, low);
  }

  public long getLong(int idx) {
    int high = this.getInt(idx);
    int low = this.getInt(idx + 1);

    long l1 = (high & 0x00000000ffffffffL) << 32;
    long l2 = low & 0x00000000ffffffffL;
    return l1 | l2;
  }

  public void setDouble(int idx, double val) {
    long tmp = Double.doubleToLongBits(val);
    this.setLong(idx, tmp);
  }

  public double getDouble(int idx) {
    long tmp = this.getLong(idx);
    return Double.longBitsToDouble(tmp);
  }

  public void setRef(int idx, Instance ref) {
    this.set(idx, Slot.ref(ref));
  }

  public Instance getRef(int idx) {
    return this.get(idx).ref;
  }

  public Instance getThis(int argSlotSize) {
    return this.operandStack[operandTop - 1 - argSlotSize].ref;
  }

  public void clearOperandStack() {
    this.operandTop = 0;
    for (Slot slot : this.operandStack) {
      slot = null;
    }
  }
}

