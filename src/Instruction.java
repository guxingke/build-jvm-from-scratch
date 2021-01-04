// 指令接口
interface Instruction {

  // offset, 字长， 因为字节码的长度不一致，一般情况下是 1，此处提供默认方法用来获取指定的字长。用来在指令结束时改变栈帧的程序计数器，使之指向下一条指令。
  default int offset() {
    return 1;
  }

  // 具体指令需要实现的方法，是指令自身的业务逻辑。
  void eval(Frame frame);
  
  default String name() {
    final String name = this.getClass().getSimpleName();
    return name.substring(0, name.length() - 4).toLowerCase();
  }
}

class IaddInst implements Instruction {

  public void eval(Frame frame) {
    int v2 = frame.popInt();
    int v1 = frame.popInt();
    frame.pushInt(v1 + v2);
    frame.pc += offset();
  }

  static IaddInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new IaddInst();
  }
}

class LaddInst implements Instruction {

  public void eval(Frame frame) {
    final long v2 = frame.popLong();
    final long v1 = frame.popLong();
    frame.pushLong(v1 + v2);
    frame.pc += offset();
  }

  static LaddInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new LaddInst();
  }
}

class FaddInst implements Instruction {

  public void eval(Frame frame) {
    final float v2 = frame.popFloat();
    final float v1 = frame.popFloat();
    frame.pushFloat(v1 + v2);
    frame.pc += offset();
  }

  static FaddInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new FaddInst();
  }
}

class DaddInst implements Instruction {

  public void eval(Frame frame) {
    final double v2 = frame.popDouble();
    final double v1 = frame.popDouble();
    frame.pushDouble(v1 + v2);
    frame.pc += offset();
  }

  static DaddInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new DaddInst();
  }
}

class IsubInst implements Instruction {

  public void eval(Frame frame) {
    final int v2 = frame.popInt();
    final int v1 = frame.popInt();
    frame.pushInt(v1 - v2);
    frame.pc += offset();
  }

  public int offset() {
    return 1;
  }

  static IsubInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new IsubInst();
  }
}

class LsubInst implements Instruction {

  public void eval(Frame frame) {
    final long v2 = frame.popLong();
    final long v1 = frame.popLong();
    frame.pushLong(v1 - v2);
    frame.pc += offset();
  }

  static LsubInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new LsubInst();
  }
}

class FsubInst implements Instruction {

  public void eval(Frame frame) {
    final float v2 = frame.popFloat();
    final float v1 = frame.popFloat();
    frame.pushFloat(v1 - v2);
    frame.pc += offset();
  }

  static FsubInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new FsubInst();
  }
}

class DsubInst implements Instruction {

  public void eval(Frame frame) {
    final double v2 = frame.popDouble();
    final double v1 = frame.popDouble();
    frame.pushDouble(v1 - v2);
    frame.pc += offset();
  }

  static DsubInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new DsubInst();
  }
}

class ImulInst implements Instruction {

  public void eval(Frame frame) {
    final int v2 = frame.popInt();
    final int v1 = frame.popInt();
    frame.pushInt(v1 * v2);
    frame.pc += offset();
  }

  static ImulInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new ImulInst();
  }
}

class LmulInst implements Instruction {

  public void eval(Frame frame) {
    final long v2 = frame.popLong();
    final long v1 = frame.popLong();
    frame.pushLong(v1 * v2);
    frame.pc += offset();
  }

  static LmulInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new LmulInst();
  }
}

class FmulInst implements Instruction {

  public void eval(Frame frame) {
    final float v2 = frame.popFloat();
    final float v1 = frame.popFloat();
    frame.pushFloat(v1 * v2);
    frame.pc += offset();
  }

  static FmulInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new FmulInst();
  }
}

class DmulInst implements Instruction {

  public void eval(Frame frame) {
    final double v2 = frame.popDouble();
    final double v1 = frame.popDouble();
    frame.pushDouble(v1 * v2);
    frame.pc += offset();
  }

  static DmulInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new DmulInst();
  }
}

class IdivInst implements Instruction {

  public void eval(Frame frame) {
    final int v2 = frame.popInt();
    final int v1 = frame.popInt();
    frame.pushInt(v1 / v2);
    frame.pc += offset();
  }

  static IdivInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new IdivInst();
  }
}

class LdivInst implements Instruction {

  public void eval(Frame frame) {
    final long v2 = frame.popLong();
    final long v1 = frame.popLong();
    frame.pushLong(v1 / v2);
    frame.pc += offset();
  }

  static LdivInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new LdivInst();
  }
}

class FdivInst implements Instruction {

  public void eval(Frame frame) {
    final float v2 = frame.popFloat();
    final float v1 = frame.popFloat();
    frame.pushFloat(v1 / v2);
    frame.pc += offset();
  }

  static FdivInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new FdivInst();
  }
}

class DdivInst implements Instruction {

  public void eval(Frame frame) {
    final double v2 = frame.popDouble();
    final double v1 = frame.popDouble();
    frame.pushDouble(v1 / v2);
    frame.pc += offset();
  }

  static DdivInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new DdivInst();
  }
}

class IremInst implements Instruction {

  public void eval(Frame frame) {
    final int v2 = frame.popInt();
    final int v1 = frame.popInt();
    frame.pushInt(v1 % v2);
    frame.pc += offset();
  }

  static IremInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new IremInst();
  }
}

class LremInst implements Instruction {

  public void eval(Frame frame) {
    final long v2 = frame.popLong();
    final long v1 = frame.popLong();
    frame.pushLong(v1 % v2);
    frame.pc += offset();
  }

  static LremInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new LremInst();
  }
}

class FremInst implements Instruction {

  public void eval(Frame frame) {
    final float v2 = frame.popFloat();
    final float v1 = frame.popFloat();
    frame.pushFloat(v1 % v2);
    frame.pc += offset();
  }

  static FremInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new FremInst();
  }
}

class DremInst implements Instruction {

  public void eval(Frame frame) {
    final double v2 = frame.popDouble();
    final double v1 = frame.popDouble();
    frame.pushDouble(v1 % v2);
    frame.pc += offset();
  }

  static DremInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new DremInst();
  }
}

class InegInst implements Instruction {

  public void eval(Frame frame) {
    final int v1 = frame.popInt();
    frame.pushInt(-v1);
    frame.pc += offset();
  }

  static InegInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new InegInst();
  }
}

class LnegInst implements Instruction {

  public void eval(Frame frame) {
    final long v1 = frame.popLong();
    frame.pushLong(-v1);
    frame.pc += offset();
  }

  static LnegInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new LnegInst();
  }
}

class FnegInst implements Instruction {

  public void eval(Frame frame) {
    final float v1 = frame.popFloat();
    frame.pushFloat(-v1);
    frame.pc += offset();
  }

  static FnegInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new FnegInst();
  }
}

class DnegInst implements Instruction {

  public void eval(Frame frame) {
    final double v1 = frame.popDouble();
    frame.pushDouble(-v1);
    frame.pc += offset();
  }

  static DnegInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new DnegInst();
  }
}

class IshlInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    int v2 = frame.popInt();
    int v1 = frame.popInt();
    int s = v2 & 0x1f;
    int ret = v1 << s;
    frame.pushInt(ret);

    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static IshlInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new IshlInst();
  }
}

class LshlInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    int v2 = frame.popInt();
    long v1 = frame.popLong();
    int s = v2 & 0x1f;
    long ret = v1 << s;
    frame.pushLong(ret);

    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static LshlInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new LshlInst();
  }
}

class IshrInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    int v2 = frame.popInt();
    int v1 = frame.popInt();
    int s = v2 & 0x1f;
    int ret = v1 >> s;
    frame.pushInt(ret);
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static IshrInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new IshrInst();
  }
}

class LshrInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    final long v2 = frame.popLong();
    final int v1 = frame.popInt();
    frame.pushLong(v1 >> v2);

    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static LshrInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new LshrInst();
  }
}

class IushrInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    int v2 = frame.popInt();
    int v1 = frame.popInt();
    int s = v2 & 0x1f;

    if (v1 >= 0) {
      int ret = v1 >> s;
      frame.pushInt(ret);
    } else {
      int ret = (v1 >> s) + (2 << ~s);
      frame.pushInt(ret);
    }
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static IushrInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new IushrInst();
  }
}

class LushrInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    int v2 = frame.popInt();
    long v1 = frame.popLong();
    int s = v2 & 0x3f;

    if (v1 >= 0) {
      long ret = v1 >> s;
      frame.pushLong(ret);
    } else {
      long ret = (v1 >> s) + (2L << ~s);
      frame.pushLong(ret);
    }
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static LushrInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new LushrInst();
  }
}

class IandInst implements Instruction {

  public void eval(Frame frame) {
    final int v2 = frame.popInt();
    final int v1 = frame.popInt();
    frame.pushInt(v1 & v2);
    frame.pc += offset();
  }

  static IandInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new IandInst();
  }
}

class LandInst implements Instruction {

  public void eval(Frame frame) {
    final long v2 = frame.popLong();
    final long v1 = frame.popLong();
    frame.pushLong(v1 & v2);
    frame.pc += offset();
  }

  static LandInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new LandInst();
  }
}

class IorInst implements Instruction {

  public void eval(Frame frame) {
    final int v2 = frame.popInt();
    final int v1 = frame.popInt();
    frame.pushInt(v1 | v2);
    frame.pc += offset();
  }

  static IorInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new IorInst();
  }
}

class LorInst implements Instruction {

  public void eval(Frame frame) {
    final long v2 = frame.popLong();
    final long v1 = frame.popLong();
    frame.pushLong(v1 | v2);
    frame.pc += offset();
  }

  static LorInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new LorInst();
  }
}

class IxorInst implements Instruction {

  public void eval(Frame frame) {
    final int v2 = frame.popInt();
    final int v1 = frame.popInt();
    frame.pushInt(v1 ^ v2);
    frame.pc += offset();
  }

  static IxorInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new IxorInst();
  }
}

class LxorInst implements Instruction {

  public void eval(Frame frame) {
    final long v2 = frame.popLong();
    final long v1 = frame.popLong();
    frame.pushLong(v1 ^ v2);
    frame.pc += offset();
  }

  static LxorInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new LxorInst();
  }
}

class IincInst implements Instruction {

  public final int index;
  public final int val;

  public IincInst(int index, int val) {
    this.index = index;
    this.val = val;
  }

  public void eval(Frame frame) {
    int tmp = frame.getInt(index);
    tmp += val;
    frame.setInt(index, tmp);
    frame.pc += offset();
  }

  public int offset() {
    return 3;
  }

  static IincInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new IincInst(dis.readUnsignedByte(), dis.readByte());
  }
}

class GotoInst implements Instruction {

  public final int offset;

  public GotoInst(int offset) {
    this.offset = offset;
  }

  public void eval(Frame frame) {
    frame.pc += offset;
  }

  public int offset() {
    return 3;
  }

  static GotoInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new GotoInst(dis.readShort());
  }
}

class NopInst implements Instruction {

  public void eval(Frame frame) {
    frame.pc += offset();
  }

  static NopInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new NopInst();
  }
}

class Aconst_nullInst implements Instruction {

  public void eval(Frame frame) {
    frame.pushRef(null);
    frame.pc += offset();
  }

  static Aconst_nullInst parse(java.io.DataInputStream dis, CpInfo[] cp)
      throws java.io.IOException {
    return new Aconst_nullInst();
  }
}

class Iconst_m1Inst implements Instruction {

  public void eval(Frame frame) {
    frame.pushInt(-1);
    frame.pc += offset();
  }

  static Iconst_m1Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Iconst_m1Inst();
  }
}

class Iconst_0Inst implements Instruction {

  public void eval(Frame frame) {
    frame.pushInt(0);
    frame.pc += offset();
  }

  static Iconst_0Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Iconst_0Inst();
  }
}

class Iconst_1Inst implements Instruction {

  public void eval(Frame frame) {
    frame.pushInt(1);
    frame.pc += offset();
  }

  static Iconst_1Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Iconst_1Inst();
  }
}

class Iconst_2Inst implements Instruction {

  public void eval(Frame frame) {
    frame.pushInt(2);
    frame.pc += offset();
  }

  static Iconst_2Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Iconst_2Inst();
  }
}

class Iconst_3Inst implements Instruction {

  public void eval(Frame frame) {
    frame.pushInt(3);
    frame.pc += offset();
  }

  static Iconst_3Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Iconst_3Inst();
  }
}

class Iconst_4Inst implements Instruction {

  public void eval(Frame frame) {
    frame.pushInt(4);
    frame.pc += offset();
  }

  static Iconst_4Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Iconst_4Inst();
  }
}

class Iconst_5Inst implements Instruction {

  public void eval(Frame frame) {
    frame.pushInt(5);
    frame.pc += offset();
  }

  static Iconst_5Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Iconst_5Inst();
  }
}

class Lconst_0Inst implements Instruction {

  public void eval(Frame frame) {
    frame.pushLong(0L);
    frame.pc += offset();
  }

  static Lconst_0Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Lconst_0Inst();
  }
}

class Lconst_1Inst implements Instruction {

  public void eval(Frame frame) {
    frame.pushLong(1L);
    frame.pc += offset();
  }

  static Lconst_1Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Lconst_1Inst();
  }
}

class Fconst_0Inst implements Instruction {

  public void eval(Frame frame) {
    frame.pushFloat(0f);
    frame.pc += offset();
  }

  static Fconst_0Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Fconst_0Inst();
  }
}

class Fconst_1Inst implements Instruction {

  public void eval(Frame frame) {
    frame.pushFloat(1f);
    frame.pc += offset();
  }

  static Fconst_1Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Fconst_1Inst();
  }
}

class Fconst_2Inst implements Instruction {

  public void eval(Frame frame) {
    frame.pushFloat(2f);
    frame.pc += offset();
  }

  static Fconst_2Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Fconst_2Inst();
  }
}

class Dconst_0Inst implements Instruction {

  public void eval(Frame frame) {
    frame.pushDouble(0d);
    frame.pc += offset();
  }

  static Dconst_0Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Dconst_0Inst();
  }
}

class Dconst_1Inst implements Instruction {

  public void eval(Frame frame) {
    frame.pushDouble(1d);
    frame.pc += offset();
  }

  static Dconst_1Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Dconst_1Inst();
  }
}

class BipushInst implements Instruction {

  public final int val;

  public BipushInst(int val) {
    this.val = val;
  }

  public void eval(Frame frame) {
    frame.pushInt(val);
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 2;
  }

  static BipushInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new BipushInst(dis.readByte());
  }
}

class SipushInst implements Instruction {

  final int val;

  SipushInst(int val) {
    this.val = val;
  }

  public void eval(Frame frame) {
    frame.pushInt(val);
    frame.pc += offset();
  }

  public int offset() {
    return 3;
  }

  static SipushInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new SipushInst(dis.readShort());
  }
}

class LdcInst implements Instruction {

  final int idx;

  LdcInst(int idx) {
    this.idx = idx;
  }

  @Override
  public void eval(Frame frame) {
    final CpInfo[] cp = frame.method.clazz.classFile.constantPool;
    final CpInfo info = cp[idx];
    switch (info.tag.val) {
      case Const.CONSTANT_String:
        final Class cls = MetaSpace.getClassLoader().findClass("java/lang/String");
        Utils.clinit(cls);

        final String str = Utils.getString(cp, idx);
        Instance instance = Utils.createString(str);
        frame.pushRef(instance);
        break;

      case Const.CONSTANT_Class:
        final String className = Utils.getClassName(cp, idx);
        Class clazz = MetaSpace.getClassLoader().findClass(className);
        frame.pushRef(clazz.mirror);
        break;
      case Const.CONSTANT_Integer:
        final byte[] b = info.info;
        final int val = Utils.makeInt(b[0], b[1], b[2], b[3]);
        frame.pushInt(val);
        break;
      case Const.CONSTANT_Float:
        final byte[] b2 = info.info;
        final float val2 = Utils.makeFloat(b2[0], b2[1], b2[2], b2[3]);
        frame.pushFloat(val2);
        break;
      default:
        throw new IllegalStateException();
    }

    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 2;
  }

  static LdcInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new LdcInst(dis.readUnsignedByte());
  }
}

class Ldc_wInst implements Instruction {

  final int idx;

  Ldc_wInst(int idx) {
    this.idx = idx;
  }

  @Override
  public void eval(Frame frame) {
    final LdcInst inst = new LdcInst(idx);
    inst.eval(frame);

    frame.pc += (this.offset() - inst.offset());
  }

  @Override
  public int offset() {
    return 3;
  }

  static Ldc_wInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Ldc_wInst(dis.readUnsignedShort());
  }
}

class Ldc2_wInst implements Instruction {

  final long lv;
  final double dv;
  final boolean isLong;

  Ldc2_wInst(long lv, double dv, boolean isLong) {
    this.lv = lv;
    this.dv = dv;
    this.isLong = isLong;
  }

  @Override
  public void eval(Frame frame) {
    if (isLong) {
      frame.pushLong(lv);
    } else {
      frame.pushDouble(dv);
    }
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 3;
  }

  static Ldc2_wInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    final int idx = dis.readUnsignedShort();
    final CpInfo cpInfo = cp[idx];
    final byte[] b = cpInfo.info;
    if (cpInfo.tag.val == Const.CONSTANT_Long) {
      final long lv = Utils.makeLong(b[0], b[1], b[2], b[3], b[4], b[5], b[6], b[7]);
      return new Ldc2_wInst(lv, 0d, true);
    }
    final double dv = Utils.makeDouble(b[0], b[1], b[2], b[3], b[4], b[5], b[6], b[7]);
    return new Ldc2_wInst(0L, dv, false);
  }
}

class IloadInst implements Instruction {

  final int idx;

  IloadInst(int idx) {
    this.idx = idx;
  }

  public void eval(Frame frame) {
    frame.pushInt(frame.getInt(idx));
    frame.pc += offset();
  }

  public int offset() {
    return 2;
  }

  static IloadInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new IloadInst(dis.readUnsignedByte());
  }
}

class LloadInst implements Instruction {

  final int idx;

  LloadInst(int idx) {
    this.idx = idx;
  }

  public void eval(Frame frame) {
    frame.pushLong(frame.getLong(idx));
    frame.pc += offset();
  }

  public int offset() {
    return 2;
  }

  static LloadInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new LloadInst(dis.readUnsignedByte());
  }
}

class FloadInst implements Instruction {

  final int idx;

  FloadInst(int idx) {
    this.idx = idx;
  }

  public void eval(Frame frame) {
    frame.pushFloat(frame.getFloat(idx));
    frame.pc += offset();
  }

  public int offset() {
    return 2;
  }

  static FloadInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new FloadInst(dis.readUnsignedByte());
  }
}

class DloadInst implements Instruction {

  final int idx;

  DloadInst(int idx) {
    this.idx = idx;
  }

  public void eval(Frame frame) {
    frame.pushDouble(frame.getDouble(idx));
    frame.pc += offset();
  }

  public int offset() {
    return 2;
  }

  static DloadInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new DloadInst(dis.readUnsignedByte());
  }
}

class Iload_0Inst implements Instruction {

  public void eval(Frame frame) {
    frame.pushInt(frame.getInt(0));
    frame.pc += offset();
  }

  static Iload_0Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Iload_0Inst();
  }
}

class Iload_1Inst implements Instruction {

  public void eval(Frame frame) {
    frame.pushInt(frame.getInt(1));
    frame.pc += offset();
  }

  static Iload_1Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Iload_1Inst();
  }
}

class Iload_2Inst implements Instruction {

  public void eval(Frame frame) {
    frame.pushInt(frame.getInt(2));
    frame.pc += offset();
  }

  static Iload_2Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Iload_2Inst();
  }
}

class Iload_3Inst implements Instruction {

  public void eval(Frame frame) {
    frame.pushInt(frame.getInt(3));
    frame.pc += offset();
  }

  static Iload_3Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Iload_3Inst();
  }
}

class Lload_0Inst implements Instruction {

  public void eval(Frame frame) {
    frame.pushLong(frame.getLong(0));
    frame.pc += offset();
  }

  static Lload_0Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Lload_0Inst();
  }
}

class Lload_1Inst implements Instruction {

  public void eval(Frame frame) {
    frame.pushLong(frame.getLong(1));
    frame.pc += offset();
  }

  static Lload_1Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Lload_1Inst();
  }
}

class Lload_2Inst implements Instruction {

  public void eval(Frame frame) {
    frame.pushLong(frame.getLong(2));
    frame.pc += offset();
  }

  static Lload_2Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Lload_2Inst();
  }
}

class Lload_3Inst implements Instruction {

  public void eval(Frame frame) {
    frame.pushLong(frame.getLong(3));
    frame.pc += offset();
  }

  static Lload_3Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Lload_3Inst();
  }
}

class Fload_0Inst implements Instruction {

  public void eval(Frame frame) {
    frame.pushFloat(frame.getFloat(0));
    frame.pc += offset();
  }

  static Fload_0Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Fload_0Inst();
  }
}

class Fload_1Inst implements Instruction {

  public void eval(Frame frame) {
    frame.pushFloat(frame.getFloat(1));
    frame.pc += offset();
  }

  static Fload_1Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Fload_1Inst();
  }
}

class Fload_2Inst implements Instruction {

  public void eval(Frame frame) {
    frame.pushFloat(frame.getFloat(2));
    frame.pc += offset();
  }

  static Fload_2Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Fload_2Inst();
  }
}

class Fload_3Inst implements Instruction {

  public void eval(Frame frame) {
    frame.pushFloat(frame.getFloat(3));
    frame.pc += offset();
  }

  static Fload_3Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Fload_3Inst();
  }
}

class Dload_0Inst implements Instruction {

  public void eval(Frame frame) {
    frame.pushDouble(frame.getDouble(0));
    frame.pc += offset();
  }

  static Dload_0Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Dload_0Inst();
  }
}

class Dload_1Inst implements Instruction {

  public void eval(Frame frame) {
    frame.pushDouble(frame.getDouble(1));
    frame.pc += offset();
  }

  static Dload_1Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Dload_1Inst();
  }
}

class Dload_2Inst implements Instruction {

  public void eval(Frame frame) {
    frame.pushDouble(frame.getDouble(2));
    frame.pc += offset();
  }

  static Dload_2Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Dload_2Inst();
  }
}

class Dload_3Inst implements Instruction {

  public void eval(Frame frame) {
    frame.pushDouble(frame.getDouble(3));
    frame.pc += offset();
  }

  static Dload_3Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Dload_3Inst();
  }
}

class IstoreInst implements Instruction {

  final int idx;

  IstoreInst(int idx) {
    this.idx = idx;
  }

  public void eval(Frame frame) {
    frame.setInt(idx, frame.popInt());
    frame.pc += offset();
  }

  public int offset() {
    return 2;
  }

  static IstoreInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new IstoreInst(dis.readUnsignedByte());
  }
}

class LstoreInst implements Instruction {

  final int idx;

  LstoreInst(int idx) {
    this.idx = idx;
  }

  public void eval(Frame frame) {
    frame.setLong(idx, frame.popLong());
    frame.pc += offset();
  }

  public int offset() {
    return 2;
  }

  static LstoreInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new LstoreInst(dis.readUnsignedByte());
  }
}

class FstoreInst implements Instruction {

  final int idx;

  FstoreInst(int idx) {
    this.idx = idx;
  }

  @Override
  public void eval(Frame frame) {
    frame.setFloat(idx, frame.popFloat());
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 2;
  }

  static FstoreInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new FstoreInst(dis.readUnsignedByte());
  }
}

class DstoreInst implements Instruction {

  final int idx;

  DstoreInst(int idx) {
    this.idx = idx;
  }

  public void eval(Frame frame) {
    frame.setDouble(idx, frame.popDouble());
    frame.pc += offset();
  }

  public int offset() {
    return 2;
  }

  static DstoreInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new DstoreInst(dis.readUnsignedByte());
  }
}

class Istore_0Inst implements Instruction {

  public void eval(Frame frame) {
    frame.setInt(0, frame.popInt());
    frame.pc += offset();
  }

  static Istore_0Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Istore_0Inst();
  }
}

class Istore_1Inst implements Instruction {

  public void eval(Frame frame) {
    frame.setInt(1, frame.popInt());
    frame.pc += offset();
  }

  static Istore_1Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Istore_1Inst();
  }
}

class Istore_2Inst implements Instruction {

  public void eval(Frame frame) {
    frame.setInt(2, frame.popInt());
    frame.pc += offset();
  }

  static Istore_2Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Istore_2Inst();
  }
}

class Istore_3Inst implements Instruction {

  public void eval(Frame frame) {
    frame.setInt(3, frame.popInt());
    frame.pc += offset();
  }

  static Istore_3Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Istore_3Inst();
  }
}

class Lstore_0Inst implements Instruction {

  public void eval(Frame frame) {
    frame.setLong(0, frame.popLong());
    frame.pc += offset();
  }

  static Lstore_0Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Lstore_0Inst();
  }
}

class Lstore_1Inst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.setLong(1, frame.popLong());
    frame.pc += offset();
  }

  static Lstore_1Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Lstore_1Inst();
  }
}

class Lstore_2Inst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.setLong(2, frame.popLong());
    frame.pc += offset();
  }

  static Lstore_2Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Lstore_2Inst();
  }
}

class Lstore_3Inst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.setLong(3, frame.popLong());
    frame.pc += offset();
  }

  static Lstore_3Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Lstore_3Inst();
  }
}

class Fstore_0Inst implements Instruction {

  public void eval(Frame frame) {
    frame.setFloat(0, frame.popFloat());
    frame.pc += offset();
  }

  static Fstore_0Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Fstore_0Inst();
  }
}

class Fstore_1Inst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.setFloat(1, frame.popFloat());
    frame.pc += offset();
  }

  static Fstore_1Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Fstore_1Inst();
  }
}

class Fstore_2Inst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.setFloat(2, frame.popFloat());
    frame.pc += offset();
  }

  static Fstore_2Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Fstore_2Inst();
  }
}

class Fstore_3Inst implements Instruction {

  public void eval(Frame frame) {
    frame.setFloat(3, frame.popFloat());
    frame.pc += offset();
  }

  static Fstore_3Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Fstore_3Inst();
  }
}

class Dstore_0Inst implements Instruction {

  public void eval(Frame frame) {
    frame.setDouble(0, frame.popDouble());
    frame.pc += offset();
  }

  static Dstore_0Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Dstore_0Inst();
  }
}

class Dstore_1Inst implements Instruction {

  public void eval(Frame frame) {
    frame.setDouble(1, frame.popDouble());
    frame.pc += offset();
  }

  static Dstore_1Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Dstore_1Inst();
  }
}

class Dstore_2Inst implements Instruction {

  public void eval(Frame frame) {
    frame.setDouble(2, frame.popDouble());
    frame.pc += offset();
  }

  static Dstore_2Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Dstore_2Inst();
  }
}

class Dstore_3Inst implements Instruction {

  public void eval(Frame frame) {
    frame.setDouble(3, frame.popDouble());
    frame.pc += offset();
  }

  static Dstore_3Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Dstore_3Inst();
  }
}

class AloadInst implements Instruction {

  final int idx;

  AloadInst(int idx) {
    this.idx = idx;
  }

  @Override
  public void eval(Frame frame) {
    frame.pushRef(frame.getRef(idx));
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 2;
  }

  static AloadInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new AloadInst(dis.readUnsignedByte());
  }
}

class Aload_0Inst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.pushRef(frame.getRef(0));
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static Aload_0Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Aload_0Inst();
  }
}

class Aload_1Inst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.pushRef(frame.getRef(1));
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static Aload_1Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Aload_1Inst();
  }
}

class Aload_2Inst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.pushRef(frame.getRef(2));
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static Aload_2Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Aload_2Inst();
  }
}

class Aload_3Inst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.pushRef(frame.getRef(3));
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static Aload_3Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Aload_3Inst();
  }
}

class IaloadInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    final int idx = frame.popInt();
    final PrimitiveArray array = (PrimitiveArray) frame.popRef();
    int item = array.ints[idx];
    frame.pushInt(item);
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static IaloadInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new IaloadInst();
  }
}

class LaloadInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    final int idx = frame.popInt();
    final PrimitiveArray array = (PrimitiveArray) frame.popRef();
    final long val = array.longs[idx];
    frame.pushLong(val);

    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static LaloadInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new LaloadInst();
  }
}

class FaloadInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    final int idx = frame.popInt();
    final PrimitiveArray array = (PrimitiveArray) frame.popRef();
    final float val = array.floats[idx];
    frame.pushFloat(val);

    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static FaloadInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new FaloadInst();
  }
}

class DaloadInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    final int idx = frame.popInt();
    final PrimitiveArray array = (PrimitiveArray) frame.popRef();
    final double val = array.doubles[idx];
    frame.pushDouble(val);

    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static DaloadInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new DaloadInst();
  }
}

class AaloadInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    final int idx = frame.popInt();
    final InstanceArray array = (InstanceArray) frame.popRef();
    final Instance instance = array.instances[idx];
    frame.pushRef(instance);

    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static AaloadInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new AaloadInst();
  }
}

class BaloadInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    final int idx = frame.popInt();
    final PrimitiveArray array = (PrimitiveArray) frame.popRef();
    int item = array.ints[idx];
    frame.pushInt(item);
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static BaloadInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new BaloadInst();
  }
}

class CaloadInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    final int idx = frame.popInt();
    final PrimitiveArray array = (PrimitiveArray) frame.popRef();
    int item = array.ints[idx];
    frame.pushInt(item);
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static CaloadInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new CaloadInst();
  }
}

class SaloadInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    final int idx = frame.popInt();
    final PrimitiveArray array = (PrimitiveArray) frame.popRef();
    int item = array.ints[idx];
    frame.pushInt(item);
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static SaloadInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new SaloadInst();
  }
}

class AstoreInst implements Instruction {

  final int idx;

  AstoreInst(int idx) {
    this.idx = idx;
  }

  @Override
  public void eval(Frame frame) {
    frame.setRef(idx, frame.popRef());
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 2;
  }

  static AstoreInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new AstoreInst(dis.readUnsignedByte());
  }
}

class Astore_0Inst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.setRef(0, frame.popRef());
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static Astore_0Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Astore_0Inst();
  }
}

class Astore_1Inst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.setRef(1, frame.popRef());
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static Astore_1Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Astore_1Inst();
  }
}

class Astore_2Inst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.setRef(2, frame.popRef());
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static Astore_2Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Astore_2Inst();
  }
}

class Astore_3Inst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.setRef(3, frame.popRef());
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static Astore_3Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Astore_3Inst();
  }
}

class IastoreInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    final int tmp = frame.popInt();
    final int idx = frame.popInt();
    final PrimitiveArray array = (PrimitiveArray) frame.popRef();
    array.ints[idx] = tmp;

    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static IastoreInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new IastoreInst();
  }
}

class LastoreInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    final long tmp = frame.popLong();
    final int idx = frame.popInt();
    final PrimitiveArray array = (PrimitiveArray) frame.popRef();
    array.longs[idx] = tmp;

    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static LastoreInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new LastoreInst();
  }
}

class FastoreInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    final float tmp = frame.popFloat();
    final int idx = frame.popInt();
    final PrimitiveArray array = (PrimitiveArray) frame.popRef();
    array.floats[idx] = tmp;

    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static FastoreInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new FastoreInst();
  }
}

class DastoreInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    final double tmp = frame.popDouble();
    final int idx = frame.popInt();
    final PrimitiveArray array = (PrimitiveArray) frame.popRef();
    array.doubles[idx] = tmp;

    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static DastoreInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new DastoreInst();
  }
}

class AastoreInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    Instance val = frame.popRef();
    int idx = frame.popInt();
    InstanceArray array = (InstanceArray) frame.popRef();
    array.instances[idx] = val;

    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static AastoreInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new AastoreInst();
  }
}

class BastoreInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    final int tmp = frame.popInt();
    final int idx = frame.popInt();
    final PrimitiveArray array = (PrimitiveArray) frame.popRef();
    array.ints[idx] = tmp;

    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static BastoreInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new BastoreInst();
  }
}

class CastoreInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    final int tmp = frame.popInt();
    final int idx = frame.popInt();
    final PrimitiveArray array = (PrimitiveArray) frame.popRef();
    array.ints[idx] = tmp;

    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static CastoreInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new CastoreInst();
  }
}

class SastoreInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    final int tmp = frame.popInt();
    final int idx = frame.popInt();
    final PrimitiveArray array = (PrimitiveArray) frame.popRef();
    array.ints[idx] = tmp;

    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static SastoreInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new SastoreInst();
  }
}

class PopInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.pop();
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static PopInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new PopInst();
  }
}

class Pop2Inst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.pop();
    frame.pop();
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static Pop2Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Pop2Inst();
  }
}

class DupInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    final Slot val = frame.pop();
    frame.push(val);
    frame.push(val);

    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static DupInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new DupInst();
  }
}

class Dup_x1Inst implements Instruction {

  @Override
  public void eval(Frame frame) {
    Slot s1 = frame.pop();
    Slot s2 = frame.pop();
    frame.push(s1);
    frame.push(s2);
    frame.push(s1);
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static Dup_x1Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Dup_x1Inst();
  }
}

class Dup_x2Inst implements Instruction {

  @Override
  public void eval(Frame frame) {
    Slot v1 = frame.pop();
    Slot v2 = frame.pop();
    Slot v3 = frame.pop();
    frame.push(v1);
    frame.push(v3);
    frame.push(v2);
    frame.push(v1);

    frame.pc += offset();
    throw new IllegalStateException();
  }

  @Override
  public int offset() {
    return 1;
  }

  static Dup_x2Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    throw new IllegalStateException("parse Dup_x2Inst");
  }
}

class Dup2Inst implements Instruction {

  @Override
  public void eval(Frame frame) {
    final Slot v2 = frame.pop();
    final Slot v1 = frame.pop();
    frame.push(v1);
    frame.push(v2);
    frame.push(v1);
    frame.push(v2);
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static Dup2Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Dup2Inst();
  }
}

class Dup2_x1Inst implements Instruction {

  @Override
  public void eval(Frame frame) {
    Slot v1 = frame.pop();
    Slot v2 = frame.pop();
    Slot v3 = frame.pop();
    frame.push(v2);
    frame.push(v1);
    frame.push(v3);
    frame.push(v2);
    frame.push(v1);

    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static Dup2_x1Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Dup2_x1Inst();
  }
}

class Dup2_x2Inst implements Instruction {

  @Override
  public void eval(Frame frame) {
    Slot v1 = frame.pop();
    Slot v2 = frame.pop();
    Slot v3 = frame.pop();
    Slot v4 = frame.pop();
    frame.push(v2);
    frame.push(v1);
    frame.push(v4);
    frame.push(v3);
    frame.push(v2);
    frame.push(v1);
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static Dup2_x2Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new Dup2_x2Inst();
  }
}

class SwapInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    Slot v1 = frame.pop();
    Slot v2 = frame.pop();
    frame.push(v1);
    frame.push(v2);
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static SwapInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new SwapInst();
  }
}

class I2lInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    final int val = frame.popInt();
    frame.pushLong(val);
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static I2lInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new I2lInst();
  }
}

class I2fInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.pushFloat(frame.popInt());
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static I2fInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new I2fInst();
  }
}

class I2dInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.pushDouble(frame.popInt());
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static I2dInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new I2dInst();
  }
}

class L2iInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    final long tmp = frame.popLong();
    frame.pushInt((int) tmp);
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static L2iInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new L2iInst();
  }
}

class L2fInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    final long tmp = frame.popLong();
    frame.pushFloat((float) tmp);
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static L2fInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new L2fInst();
  }
}

class L2dInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    final long tmp = frame.popLong();
    frame.pushDouble((double) tmp);
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static L2dInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new L2dInst();
  }
}

class F2iInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.pushInt(((int) frame.popFloat()));
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static F2iInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new F2iInst();
  }
}

class F2lInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.pushLong((long) frame.popFloat());
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static F2lInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new F2lInst();
  }
}

class F2dInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.pushDouble((double) frame.popFloat());
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static F2dInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new F2dInst();
  }
}

class D2iInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.pushInt(((int) frame.popDouble()));
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static D2iInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new D2iInst();
  }
}

class D2lInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.pushLong(((long) frame.popDouble()));
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static D2lInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new D2lInst();
  }
}

class D2fInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.pushFloat(((float) frame.popDouble()));
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static D2fInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new D2fInst();
  }
}

class I2bInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.pushInt(((byte) frame.popInt()));
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static I2bInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new I2bInst();
  }
}

class I2cInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.pushInt(((char) frame.popInt()));
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static I2cInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new I2cInst();
  }
}

class I2sInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.pushInt(((short) frame.popInt()));
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static I2sInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new I2sInst();
  }
}

class LcmpInst implements Instruction {

  public void eval(Frame frame) {
    final long v2 = frame.popLong();
    final long v1 = frame.popLong();
    int ret = 0;
    if (v1 > v2) {
      ret = 1;
    } else if (v1 < v2) {
      ret = -1;
    }
    frame.pushInt(ret);
    frame.pc += offset();
  }

  static LcmpInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new LcmpInst();
  }
}

class FcmplInst implements Instruction {

  public void eval(Frame frame) {
    final float v2 = frame.popFloat();
    final float v1 = frame.popFloat();
    int ret = 0;
    if (Float.isNaN(v1) || Float.isNaN(v2)) {
      ret = 1;
    } else if (v1 > v2) {
      ret = 1;
    } else if (v1 < v2) {
      ret = -1;
    }
    frame.pushInt(ret);
    frame.pc += offset();
  }

  static FcmplInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new FcmplInst();
  }
}

class FcmpgInst implements Instruction {

  public void eval(Frame frame) {
    final float v2 = frame.popFloat();
    final float v1 = frame.popFloat();
    int ret = 0;
    if (Float.isNaN(v1) || Float.isNaN(v2)) {
      ret = 1;
    } else if (v1 > v2) {
      ret = 1;
    } else if (v1 < v2) {
      ret = -1;
    }
    frame.pushInt(ret);
    frame.pc += offset();
  }

  static FcmpgInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new FcmpgInst();
  }
}

class DcmplInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    final double v2 = frame.popDouble();
    final double v1 = frame.popDouble();
    int ret = 0;
    if (Double.isNaN(v1) || Double.isNaN(v2)) {
      ret = 1;
    } else if (v1 > v2) {
      ret = 1;
    } else if (v1 < v2) {
      ret = -1;
    }
    frame.pushInt(ret);
    frame.pc += offset();
  }

  static DcmplInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new DcmplInst();
  }
}

class DcmpgInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    final double v2 = frame.popDouble();
    final double v1 = frame.popDouble();
    int ret = 0;
    if (Double.isNaN(v1) || Double.isNaN(v2)) {
      ret = 1;
    } else if (v1 > v2) {
      ret = 1;
    } else if (v1 < v2) {
      ret = -1;
    }
    frame.pushInt(ret);
    frame.pc += offset();
  }

  static DcmpgInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new DcmpgInst();
  }
}

class IfeqInst implements Instruction {

  final int offset;

  IfeqInst(int offset) {
    this.offset = offset;
  }

  public void eval(Frame frame) {
    final int val = frame.popInt();
    if (val == 0) {
      frame.pc += offset;
      return;
    }
    frame.pc += offset();
  }

  public int offset() {
    return 3;
  }

  static IfeqInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new IfeqInst(dis.readShort());
  }
}

class IfneInst implements Instruction {

  final int offset;

  IfneInst(int offset) {
    this.offset = offset;
  }

  public void eval(Frame frame) {
    final int val = frame.popInt();
    if (val != 0) {
      frame.pc += offset;
      return;
    }
    frame.pc += offset();
  }

  public int offset() {
    return 3;
  }

  static IfneInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new IfneInst(dis.readShort());
  }
}

class IfltInst implements Instruction {

  final int offset;

  IfltInst(int offset) {
    this.offset = offset;
  }

  public void eval(Frame frame) {
    final int val = frame.popInt();
    if (val < 0) {
      frame.pc += offset;
      return;
    }
    frame.pc += offset();
  }

  public int offset() {
    return 3;
  }

  static IfltInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new IfltInst(dis.readShort());
  }
}

class IfgeInst implements Instruction {

  final int offset;

  IfgeInst(int offset) {
    this.offset = offset;
  }

  public void eval(Frame frame) {
    int val = frame.popInt();
    if (val >= 0) {
      frame.pc += offset;
      return;
    }
    frame.pc += offset();
  }

  public int offset() {
    return 3;
  }

  static IfgeInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new IfgeInst(dis.readShort());
  }
}

class IfgtInst implements Instruction {

  final int offset;

  IfgtInst(int offset) {
    this.offset = offset;
  }

  public void eval(Frame frame) {
    int val = frame.popInt();
    if (val > 0) {
      frame.pc += offset;
      return;
    }
    frame.pc += offset();
  }

  public int offset() {
    return 3;
  }

  static IfgtInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new IfgtInst(dis.readShort());
  }
}

class IfleInst implements Instruction {

  final int offset;

  IfleInst(int offset) {
    this.offset = offset;
  }

  public void eval(Frame frame) {
    int val = frame.popInt();
    if (val <= 0) {
      frame.pc += offset;
      return;
    }
    frame.pc += offset();
  }

  public int offset() {
    return 3;
  }

  static IfleInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new IfleInst(dis.readShort());
  }
}

class If_icmpeqInst implements Instruction {

  final int offset;

  If_icmpeqInst(int offset) {
    this.offset = offset;
  }

  public void eval(Frame frame) {
    int v2 = frame.popInt();
    int v1 = frame.popInt();
    if (v1 == v2) {
      frame.pc += offset;
      return;
    }

    frame.pc += offset();
  }

  public int offset() {
    return 3;
  }

  static If_icmpeqInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new If_icmpeqInst(dis.readShort());
  }
}

class If_icmpneInst implements Instruction {

  final int offset;

  If_icmpneInst(int offset) {
    this.offset = offset;
  }

  public void eval(Frame frame) {
    int v2 = frame.popInt();
    int v1 = frame.popInt();
    if (v1 != v2) {
      frame.pc += offset;
      return;
    }
    frame.pc += offset();
  }

  public int offset() {
    return 3;
  }

  static If_icmpneInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new If_icmpneInst(dis.readShort());
  }
}

class If_icmpltInst implements Instruction {

  final int offset;

  If_icmpltInst(int offset) {
    this.offset = offset;
  }

  public void eval(Frame frame) {
    int v2 = frame.popInt();
    int v1 = frame.popInt();
    if (v1 <= v2) {
      frame.pc += offset;
      return;
    }
    frame.pc += offset();
  }

  public int offset() {
    return 3;
  }

  static If_icmpltInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new If_icmpltInst(dis.readShort());
  }
}

class If_icmpgeInst implements Instruction {

  final int offset;

  If_icmpgeInst(int offset) {
    this.offset = offset;
  }

  public void eval(Frame frame) {
    int v2 = frame.popInt();
    int v1 = frame.popInt();
    if (v1 >= v2) {
      frame.pc += offset;
      return;
    }
    frame.pc += offset();
  }

  public int offset() {
    return 3;
  }

  static If_icmpgeInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new If_icmpgeInst(dis.readShort());
  }
}

class If_icmpgtInst implements Instruction {

  public final int offset;

  public If_icmpgtInst(int offset) {
    this.offset = offset;
  }

  public void eval(Frame frame) {
    int v2 = frame.popInt();
    int v1 = frame.popInt();
    if (v1 > v2) {
      frame.pc += offset;
      return;
    }
    frame.pc += offset();
  }

  public int offset() {
    return 3;
  }

  static If_icmpgtInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new If_icmpgtInst(dis.readShort());
  }
}

class If_icmpleInst implements Instruction {

  final int offset;

  If_icmpleInst(int offset) {
    this.offset = offset;
  }

  public void eval(Frame frame) {
    int v2 = frame.popInt();
    int v1 = frame.popInt();
    if (v1 <= v2) {
      frame.pc += offset;
      return;
    }
    frame.pc += offset();
  }

  public int offset() {
    return 3;
  }

  static If_icmpleInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new If_icmpleInst(dis.readShort());
  }
}


class If_acmpeqInst implements Instruction {

  final int offset;

  If_acmpeqInst(int offset) {
    this.offset = offset;
  }

  @Override
  public void eval(Frame frame) {
    final Instance v2 = frame.popRef();
    final Instance v1 = frame.popRef();
    if (v1 == v2) {
      frame.pc += offset;
      return;
    }
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 3;
  }

  static If_acmpeqInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new If_acmpeqInst(dis.readShort());
  }
}

class If_acmpneInst implements Instruction {

  final int offset;

  If_acmpneInst(int offset) {
    this.offset = offset;
  }

  @Override
  public void eval(Frame frame) {
    final Instance v2 = frame.popRef();
    final Instance v1 = frame.popRef();
    if (v1 != v2) {
      frame.pc += offset;
      return;
    }
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 3;
  }

  static If_acmpneInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new If_acmpneInst(dis.readShort());
  }
}

class JsrInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.pc += offset();
    throw new IllegalStateException();
  }

  @Override
  public int offset() {
    return 1;
  }

  static JsrInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    throw new IllegalStateException("parse JsrInst");
  }
}

class RetInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.pc += offset();
    throw new IllegalStateException();
  }

  @Override
  public int offset() {
    return 1;
  }

  static RetInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    throw new IllegalStateException("parse RetInst");
  }
}

class TableswitchInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.pc += offset();
    throw new IllegalStateException();
  }

  @Override
  public int offset() {
    return 1;
  }

  static TableswitchInst parse(java.io.DataInputStream dis, CpInfo[] cp)
      throws java.io.IOException {
    throw new IllegalStateException("parse TableswitchInst");
  }
}

class LookupswitchInst implements Instruction {

  public final int offset;
  public final int def;
  public final int pairsCnt;
  public final java.util.Map<Integer, Integer> table;

  LookupswitchInst(int offset, int def, int pairsCnt, java.util.Map<Integer, Integer> table) {
    this.offset = offset;
    this.def = def;
    this.pairsCnt = pairsCnt;
    this.table = table;
  }

  @Override
  public void eval(Frame frame) {
    Integer tmp = frame.popInt();
    Integer jump = table.getOrDefault(tmp, def);
    frame.pc += jump;
  }

  @Override
  public int offset() {
    return this.offset;
  }

  static LookupswitchInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    final MyDataInputStream stm = (MyDataInputStream) dis;
    int lsOffset = 1;
    int lsPadding = stm.readPadding();
    lsOffset += lsPadding;

    int lsDef = stm.readInt();
    lsOffset += 4;
    int lsPairsCnt = stm.readInt();
    lsOffset += 4;

    int lsPairsLen = lsPairsCnt * 2 * 4;
    java.util.Map<Integer, Integer> lsMap = new java.util.LinkedHashMap<>();
    for (int i = 0; i < lsPairsCnt; i++) {
      lsMap.put(stm.readInt(), stm.readInt());
    }

    lsOffset += lsPairsLen;
    return new LookupswitchInst(lsOffset, lsDef, lsPairsCnt, lsMap);
  }
}

class IreturnInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    Utils.doReturn1();
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static IreturnInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new IreturnInst();
  }
}

class LreturnInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    Utils.doReturn2();
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static LreturnInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new LreturnInst();
  }
}

class FreturnInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    Utils.doReturn1();
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static FreturnInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new FreturnInst();
  }
}

class DreturnInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    Utils.doReturn2();
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static DreturnInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new DreturnInst();
  }
}

class AreturnInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    Utils.doReturn1();
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static AreturnInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new AreturnInst();
  }
}

class ReturnInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    Utils.doReturn0();
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static ReturnInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new ReturnInst();
  }
}

class GetstaticInst implements Instruction {

  final String clazz;
  final String name;
  final String descriptor;

  GetstaticInst(String clazz, String name, String descriptor) {
    this.clazz = clazz;
    this.name = name;
    this.descriptor = descriptor;
  }

  @Override
  public void eval(Frame frame) {
    final Class cls = MetaSpace.getClassLoader().findClass(clazz);
    Field field = cls.getStaticField(name, descriptor);
    Utils.clinit(field.clazz);

    field.get(frame);

    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 3;
  }

  static GetstaticInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    final int gsfi = dis.readUnsignedShort();
    return new GetstaticInst(Utils.getClassNameFromFieldRef(cp, gsfi),
        Utils.getNameFromFieldRef(cp, gsfi),
        Utils.getDescriptorFromFieldRef(cp, gsfi));
  }
}

class PutstaticInst implements Instruction {

  final String clazz;
  final String name;
  final String descriptor;

  PutstaticInst(String clazz, String name, String descriptor) {
    this.clazz = clazz;
    this.name = name;
    this.descriptor = descriptor;
  }

  @Override
  public void eval(Frame frame) {
    final Class cls = MetaSpace.getClassLoader().findClass(clazz);
    Field field = cls.getStaticField(name, descriptor);
    field.set(frame);
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 3;
  }

  static PutstaticInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    final int gsfi = dis.readUnsignedShort();
    return new PutstaticInst(Utils.getClassNameFromFieldRef(cp, gsfi),
        Utils.getNameFromFieldRef(cp, gsfi),
        Utils.getDescriptorFromFieldRef(cp, gsfi));
  }
}

class GetfieldInst implements Instruction {

  final String clazz;
  final String name;
  final String descriptor;

  GetfieldInst(String clazz, String name, String descriptor) {
    this.clazz = clazz;
    this.name = name;
    this.descriptor = descriptor;
  }

  @Override
  public void eval(Frame frame) {
    final Instance self = frame.popRef();
    if (Utils.checkNullPointException(self)) {
      return;
    }

    final Field field = self.getField(name, descriptor);
    field.get(frame);

    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 3;
  }

  static GetfieldInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    final int gffi = dis.readUnsignedShort();
    return new GetfieldInst(Utils.getClassNameFromFieldRef(cp, gffi),
        Utils.getNameFromFieldRef(cp, gffi),
        Utils.getDescriptorFromFieldRef(cp, gffi));
  }
}

class PutfieldInst implements Instruction {

  final String clazz;
  final String name;
  final String descriptor;

  PutfieldInst(String clazz, String name, String descriptor) {
    this.clazz = clazz;
    this.name = name;
    this.descriptor = descriptor;
  }

  @Override
  public void eval(Frame frame) {
    UnionSlot us = null;
    if (descriptor.equals("J") || descriptor.equals("D")) {
      final Slot low = frame.pop();
      final Slot high = frame.pop();
      us = UnionSlot.of(high, low);
    } else {
      us = UnionSlot.of(frame.pop());
    }

    final Instance self = frame.popRef();
    if (Utils.checkNullPointException(self)) {
      return;
    }
    Field field = self.getField(name, descriptor);
    field.set(us);

    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 3;
  }

  static PutfieldInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    final int pffi = dis.readUnsignedShort();
    return new PutfieldInst(Utils.getClassNameFromFieldRef(cp, pffi),
        Utils.getNameFromFieldRef(cp, pffi),
        Utils.getDescriptorFromFieldRef(cp, pffi));
  }
}

class InvokevirtualInst implements Instruction {

  final String clazz;
  final String name;
  final String descriptor;

  InvokevirtualInst(String clazz, String name, String descriptor) {
    this.clazz = clazz;
    this.name = name;
    this.descriptor = descriptor;
  }

  @Override
  public void eval(Frame frame) {
    final Class clazz = MetaSpace.getClassLoader().findClass(this.clazz);

    // 获取 this
    final int size = Utils.getArgSlotSize(descriptor);
    final Instance self = frame.getThis(size);
    if (Utils.checkNullPointException(self)) {
      return;
    }

    // 从实例中获取虚方法
    Method method = self.clazz.getVirtualMethod(name, descriptor);

    // 如果方法为空，则尝试获取接口的默认方法
    if (method == null) {
      method = clazz.getDefaultMethod(name, descriptor);
    }

    if (method == null) {
      // java.lang.NoSuchMethodError
      throw new IllegalStateException();
    }

    Utils.invokeMethod(method);
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 3;
  }

  static InvokevirtualInst parse(java.io.DataInputStream dis, CpInfo[] cp)
      throws java.io.IOException {
    final int ivi = dis.readUnsignedShort();
    return new InvokevirtualInst(Utils.getClassNameFromMethodRef(cp, ivi),
        Utils.getNameFromMethodRef(cp, ivi),
        Utils.getDescriptorFromMethodRef(cp, ivi));
  }
}


class InvokespecialInst implements Instruction {

  final String clazz;
  final String name;
  final String descriptor;

  InvokespecialInst(String clazz, String name, String descriptor) {
    this.clazz = clazz;
    this.name = name;
    this.descriptor = descriptor;
  }

  @Override
  public void eval(Frame frame) {
    final Class cls = MetaSpace.getClassLoader().findClass(clazz);

    final Method method = cls.getSpecialMethod(name, descriptor);
    Utils.clinit(method.clazz);

    final int size = Utils.getArgSlotSize(descriptor);
    final Instance self = frame.getThis(size);
    if (Utils.checkNullPointException(self)) {
      return;
    }

    Utils.invokeMethod(method);
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 3;
  }

  static InvokespecialInst parse(java.io.DataInputStream dis, CpInfo[] cp)
      throws java.io.IOException {
    final int ivi = dis.readUnsignedShort();
    return new InvokespecialInst(Utils.getClassNameFromMethodRef(cp, ivi),
        Utils.getNameFromMethodRef(cp, ivi),
        Utils.getDescriptorFromMethodRef(cp, ivi));
  }
}

class InvokestaticInst implements Instruction {

  final String clazz;
  final String name;
  final String descriptor;

  InvokestaticInst(String clazz, String name, String descriptor) {
    this.clazz = clazz;
    this.name = name;
    this.descriptor = descriptor;
  }

  @Override
  public int offset() {
    return 3;
  }

  @Override
  public void eval(Frame frame) {
    Class clazz = MetaSpace.getClassLoader().findClass(this.clazz);
    Method method = clazz.getStaticMethod(name, descriptor);
    // 静态方法存在继承情况
    Utils.clinit(method.clazz);

    Utils.invokeMethod(method);
    frame.pc += offset();
  }

  static InvokestaticInst parse(java.io.DataInputStream dis, CpInfo[] cp)
      throws java.io.IOException {
    int isi = dis.readUnsignedShort();
    return new InvokestaticInst(Utils.getClassNameFromMethodRef(cp, isi),
        Utils.getNameFromMethodRef(cp, isi),
        Utils.getDescriptorFromMethodRef(cp, isi));
  }
}

class InvokeinterfaceInst implements Instruction {

  final String clazz;
  final String name;
  final String descriptor;

  InvokeinterfaceInst(String clazz, String name, String descriptor) {
    this.clazz = clazz;
    this.name = name;
    this.descriptor = descriptor;
  }

  @Override
  public void eval(Frame frame) {
    final Class clazz = MetaSpace.getClassLoader().findClass(this.clazz);

    // 获取 this
    final int size = Utils.getArgSlotSize(descriptor);
    final Instance self = frame.getThis(size);
    if (Utils.checkNullPointException(self)) {
      return;
    }

    // 从实例中获取虚方法
    Method method = self.clazz.getVirtualMethod(name, descriptor);

    // 如果方法为空，则尝试获取接口的默认方法
    if (method == null) {
      method = clazz.getDefaultMethod(name, descriptor);
    }

    if (method == null) {
      // java.lang.NoSuchMethodError
      throw new IllegalStateException();
    }

    Utils.invokeMethod(method);
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 5;
  }

  static InvokeinterfaceInst parse(java.io.DataInputStream dis, CpInfo[] cp)
      throws java.io.IOException {
    int ifi = dis.readUnsignedShort();
    final InvokeinterfaceInst inst = new InvokeinterfaceInst(
        Utils.getClassNameFromInterfaceMethodRef(cp, ifi),
        Utils.getNameFromInterfaceMethodRef(cp, ifi),
        Utils.getDescriptorFromInterfaceMethodRef(cp, ifi));
    dis.readByte(); // for count
    dis.readByte(); // for zero
    return inst;
  }
}

class InvokedynamicInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.pc += offset();
    throw new IllegalStateException();
  }

  @Override
  public int offset() {
    return 1;
  }

  static InvokedynamicInst parse(java.io.DataInputStream dis, CpInfo[] cp)
      throws java.io.IOException {
    throw new IllegalStateException("parse InvokedynamicInst");
  }
}

class NewInst implements Instruction {

  final String className;

  NewInst(String className) {
    this.className = className;
  }

  @Override
  public void eval(Frame frame) {
    Class clazz = MetaSpace.getClassLoader().findClass(this.className);
    Utils.clinit(clazz);

    Instance instance = clazz.newInstance();
    frame.pushRef(instance);

    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 3;
  }

  static NewInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new NewInst(Utils.getClassName(cp, dis.readUnsignedShort()));
  }
}

class NewarrayInst implements Instruction {

  final int tag;

  NewarrayInst(int tag) {
    this.tag = tag;
  }

  @Override
  public void eval(Frame frame) {
    final int count = frame.popInt();

    PrimitiveArray ref = null;
    switch (tag) {
      case 4:
        ref = PrimitiveArray.boolArray(count);
        break;
      case 5:
        ref = PrimitiveArray.charArray(count);
        break;
      case 6:
        ref = PrimitiveArray.floatArray(count);
        break;
      case 7:
        ref = PrimitiveArray.doubleArray(count);
        break;
      case 8:
        ref = PrimitiveArray.byteArray(count);
        break;
      case 9:
        ref = PrimitiveArray.shortArray(count);
        break;
      case 10:
        ref = PrimitiveArray.intArray(count);
        break;
      case 11:
        ref = PrimitiveArray.longArray(count);
        break;
      default:
        throw new IllegalStateException();
    }

    frame.pushRef(ref);
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 2;
  }

  static NewarrayInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new NewarrayInst(dis.readUnsignedByte());
  }
}

class AnewarrayInst implements Instruction {

  final String clazz;

  AnewarrayInst(String clazz) {
    this.clazz = clazz;
  }

  @Override
  public void eval(Frame frame) {
    String arrName = "[L" + clazz + ";";
    Class arrCls = MetaSpace.getClassLoader().findClass(arrName);
    final int cnt = frame.popInt();
    final InstanceArray array = new InstanceArray(arrCls, cnt);
    frame.pushRef(array);

    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 3;
  }

  static AnewarrayInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new AnewarrayInst(Utils.getClassName(cp, dis.readUnsignedShort()));
  }
}

class ArraylengthInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    final Instance instance = frame.popRef();
    int len = 0;
    if (instance instanceof InstanceArray) {
      len = ((InstanceArray) instance).instances.length;
    } else {
      len = ((PrimitiveArray) instance).len;
    }
    frame.pushInt(len);
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static ArraylengthInst parse(java.io.DataInputStream dis, CpInfo[] cp)
      throws java.io.IOException {
    return new ArraylengthInst();
  }
}

class AthrowInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    final Instance instance = frame.popRef();
    Utils.handleException(instance);
  }

  @Override
  public int offset() {
    return 1;
  }

  static AthrowInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new AthrowInst();
  }
}

class CheckcastInst implements Instruction {

  final String clazz;

  CheckcastInst(String clazz) {
    this.clazz = clazz;
  }

  @Override
  public void eval(Frame frame) {
    final Instance self = frame.popRef();
    if (self != null) {
      final boolean ret = self.clazz.instanceOf(this.clazz);
      if (!ret) {
        // ClassCastException
        final Class cceCls = MetaSpace.getClassLoader().findClass("java/lang/ClassCastException");
        final Instance instance = cceCls.newInstance();
        final Method initMethod = cceCls.getSpecialMethod("<init>", "()V");
        final Frame initFrame = new Frame(initMethod);
        initFrame.setRef(0, instance);
        Interpreter.execute(initFrame);

        MetaSpace.getMainEnv().exception = instance;
        return;
      }
    }

    frame.pushRef(self);
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 3;
  }

  static CheckcastInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new CheckcastInst(Utils.getClassName(cp, dis.readUnsignedShort()));
  }
}

class InstanceofInst implements Instruction {

  final String clazz;

  InstanceofInst(String clazz) {
    this.clazz = clazz;
  }

  @Override
  public void eval(Frame frame) {
    final Instance cls = frame.popRef();
    if (cls == null) {
      frame.pushInt(0);
    } else {
      boolean ret = cls.clazz.instanceOf(this.clazz);
      frame.pushInt(ret ? 1 : 0);
    }

    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 3;
  }

  static InstanceofInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new InstanceofInst(Utils.getClassName(cp, dis.readUnsignedShort()));
  }
}


class MonitorenterInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.pop();
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static MonitorenterInst parse(java.io.DataInputStream dis, CpInfo[] cp)
      throws java.io.IOException {
    return new MonitorenterInst();
  }
}

class MonitorexitInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.pop();
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 1;
  }

  static MonitorexitInst parse(java.io.DataInputStream dis, CpInfo[] cp)
      throws java.io.IOException {
    return new MonitorexitInst();
  }
}


class WideInst implements Instruction {
  final int offset;
  final Instruction instruction;

  WideInst(int offset, Instruction instruction) {
    this.offset = offset;
    this.instruction = instruction;
  }

  @Override
  public void eval(Frame frame) {
    instruction.eval(frame);
    frame.pc += (this.offset() - instruction.offset());
  }

  @Override
  public int offset() {
    return offset;
  }

  static WideInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    int wideOpcode = dis.readUnsignedByte();
    switch (wideOpcode) {
      case 0x84:
        return new WideInst(6, new IincInst(dis.readUnsignedShort(), dis.readShort()));
      case 0x15:
        return new WideInst(4, new IloadInst(dis.readUnsignedShort()));
      case 0x17:
        return new WideInst(4, new FloadInst(dis.readUnsignedShort()));
      case 0x19:
        return new WideInst(4, new AloadInst(dis.readUnsignedShort()));
      case 0x16:
        return new WideInst(4, new LloadInst(dis.readUnsignedShort()));
      case 0x18:
        return new WideInst(4, new DloadInst(dis.readUnsignedShort()));
      case 0x36:
        return new WideInst(4, new IstoreInst(dis.readUnsignedShort()));
      case 0x38:
        return new WideInst(4, new FstoreInst(dis.readUnsignedShort()));
      case 0x3a:
        return new WideInst(4, new AstoreInst(dis.readUnsignedShort()));
      case 0x37:
        return new WideInst(4, new LstoreInst(dis.readUnsignedShort()));
      case 0x39:
        return new WideInst(4, new DstoreInst(dis.readUnsignedShort()));
      case 0xa9:
        // ret, ignore
        throw new UnsupportedOperationException();
    }
    throw new UnsupportedOperationException();
  }
}

class MultianewarrayInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.pc += offset();
    throw new IllegalStateException();
  }

  @Override
  public int offset() {
    return 1;
  }

  static MultianewarrayInst parse(java.io.DataInputStream dis, CpInfo[] cp)
      throws java.io.IOException {
    throw new IllegalStateException("parse MultianewarrayInst");
  }
}

class IfnullInst implements Instruction {

  final int offset;

  IfnullInst(int offset) {
    this.offset = offset;
  }

  @Override
  public void eval(Frame frame) {
    final Instance ref = frame.popRef();
    if (ref == null) {
      frame.pc += offset;
      return;
    }
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 3;
  }

  static IfnullInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new IfnullInst(dis.readShort());
  }
}

class IfnonnullInst implements Instruction {

  final int offset;

  IfnonnullInst(int offset) {
    this.offset = offset;
  }

  @Override
  public void eval(Frame frame) {
    final Instance ref = frame.popRef();
    if (ref != null) {
      frame.pc += offset;
      return;
    }
    frame.pc += offset();
  }

  @Override
  public int offset() {
    return 3;
  }

  static IfnonnullInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    return new IfnonnullInst(dis.readShort());
  }
}


class Goto_wInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.pc += offset();
    throw new IllegalStateException();
  }

  @Override
  public int offset() {
    return 1;
  }

  static Goto_wInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    throw new IllegalStateException("parse Goto_wInst");
  }
}

class Jsr_wInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.pc += offset();
    throw new IllegalStateException();
  }

  @Override
  public int offset() {
    return 1;
  }

  static Jsr_wInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    throw new IllegalStateException("parse Jsr_wInst");
  }
}

class BreakpointInst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.pc += offset();
    throw new IllegalStateException();
  }

  @Override
  public int offset() {
    return 1;
  }

  static BreakpointInst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    throw new IllegalStateException("parse BreakpointInst");
  }
}

class Impdep1Inst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.pc += offset();
    throw new IllegalStateException();
  }

  @Override
  public int offset() {
    return 1;
  }

  static Impdep1Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    throw new IllegalStateException("parse Impdep1Inst");
  }
}

class Impdep2Inst implements Instruction {

  @Override
  public void eval(Frame frame) {
    frame.pc += offset();
    throw new IllegalStateException();
  }

  @Override
  public int offset() {
    return 1;
  }

  static Impdep2Inst parse(java.io.DataInputStream dis, CpInfo[] cp) throws java.io.IOException {
    throw new IllegalStateException("parse Impdep2Inst");
  }
}
