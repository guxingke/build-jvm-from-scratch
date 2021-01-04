import java.util.Objects;

/**
 * 数据存储的基本单位
 * 1. 存放一个 32 为的数字
 * 2. 存放一个对象引用
 */
public class Slot {

  // 基本类型
  public int val;
  // 引用类型
  public Instance ref;

  private Slot(int val, Instance ref) {
    this.val = val;
    this.ref = ref;
  }

  public static Slot val(int val) {
    return new Slot(val, null);
  }

  public static Slot ref(Instance ref) {
    return new Slot(0, ref);
  }
}

/**
 * 实例，虚拟机内部产生的所有对象实例
 */
class Instance {

  Class clazz;
  Field[] fields;

  Instance superInstance;

  // java/lang/Class 类实例特有
  Class metaClass;
  // 实例扩展信息
  Object extra;

  Instance(){}

  Instance(Class clazz, Field[] fields) {
    this.clazz = clazz;
    this.fields = fields;
  }

  /**
   * 获取实例字段，递归获取
   */
  public Field getField(String name, String descriptor) {
    for (Field field : fields) {
      if (Utils.isStatic(field.accessFlags)) {
        continue;
      }
      if (Objects.equals(name, field.name) && Objects.equals(descriptor, field.descriptor)) {
        return field;
      }
    }

    if (superInstance == null) {
      return null;
    }

    return superInstance.getField(name, descriptor);
  }

  public Instance cloneInstance(){
    final Instance clone = new Instance(this.clazz, this.fields);
    clone.superInstance = superInstance;
    clone.metaClass = metaClass;
    clone.extra = extra;
    return clone;
  }
}

class InstanceArray extends Instance {

  Instance[] instances;

  public InstanceArray(Class clazz, int size) {
    super(clazz, null);
    instances = new Instance[size];
  }

  public InstanceArray cloneInstance(){
    final InstanceArray clone = new InstanceArray(this.clazz, this.instances.length);
    clone.instances = this.instances;
    return clone;
  }
}

class PrimitiveArray extends Instance {

  int[] ints;
  long[] longs;
  float[] floats;
  double[] doubles;
  int len;

  private PrimitiveArray(Class clazz, int size) {
    super(clazz, null);
    len = size;
  }

  public PrimitiveArray cloneInstance(){
    final PrimitiveArray clone = new PrimitiveArray(this.clazz, len);
    clone.ints = ints;
    clone.longs = longs;
    clone.floats = floats;
    clone.doubles = doubles;
    return clone;
  }

  static PrimitiveArray charArray(int size) {
    final Class arrCls = MetaSpace.findClass("[C");
    final PrimitiveArray array = new PrimitiveArray(arrCls, size);
    array.ints = new int[size];
    return array;
  }

  static PrimitiveArray boolArray(int size) {
    final Class arrCls = MetaSpace.findClass("[Z");
    final PrimitiveArray array = new PrimitiveArray(arrCls, size);
    array.ints = new int[size];
    return array;
  }

  static PrimitiveArray byteArray(int size) {
    final Class arrCls = MetaSpace.findClass("[B");
    final PrimitiveArray array = new PrimitiveArray(arrCls, size);
    array.ints = new int[size];
    return array;
  }

  static PrimitiveArray shortArray(int size) {
    final Class arrCls = MetaSpace.findClass("[S");
    final PrimitiveArray array = new PrimitiveArray(arrCls, size);
    array.ints = new int[size];
    return array;
  }

  static PrimitiveArray intArray(int size) {
    final Class arrCls = MetaSpace.findClass("[I");
    final PrimitiveArray array = new PrimitiveArray(arrCls, size);
    array.ints = new int[size];
    return array;
  }

  static PrimitiveArray longArray(int size) {
    final Class arrCls = MetaSpace.findClass("[J");
    final PrimitiveArray array = new PrimitiveArray(arrCls, size);
    array.longs = new long[size];
    return array;
  }

  static PrimitiveArray floatArray(int size) {
    final Class arrCls = MetaSpace.findClass("[F");
    final PrimitiveArray array = new PrimitiveArray(arrCls, size);
    array.floats = new float[size];
    return array;
  }

  static PrimitiveArray doubleArray(int size) {
    final Class arrCls = MetaSpace.findClass("[D");
    final PrimitiveArray array = new PrimitiveArray(arrCls, size);
    array.doubles = new double[size];
    return array;
  }
}

/**
 * 用于字段的联合 Slot
 */
class UnionSlot {

  private Slot high;
  private Slot low;

  private UnionSlot(Slot high, Slot low) {
    this.high = high;
    this.low = low;
  }

  // 初始化
  static UnionSlot of(Slot high, Slot low) {
    return new UnionSlot(high, low);
  }

  static UnionSlot of(Slot high) {
    return new UnionSlot(high, null);
  }

  static UnionSlot of(Instance instance) {
    return new UnionSlot(Slot.ref(instance), null);
  }

  static UnionSlot of(int val) {
    return new UnionSlot(Slot.val(val), null);
  }

  static UnionSlot of(float val) {
    return of(Float.floatToIntBits(val));
  }

  static UnionSlot of(long val) {
    int high = (int) (val >> 32); //高32位
    int low = (int) (val & 0x000000ffffffffL); //低32位
    return new UnionSlot(Slot.val(high), Slot.val(low));
  }

  static UnionSlot of(double val) {
    return of(Double.doubleToLongBits(val));
  }

  // 存
  void setRef(Instance val) {
    high.ref = val;
  }

  void setInt(int val) {
    high.val = val;
  }

  void setFloat(float val) {
    setInt(Float.floatToIntBits(val));
  }

  void setLong(long val) {
    int highV = (int) (val >> 32); //高32位
    int lowV = (int) (val & 0x000000ffffffffL); //低32位
    high.val = highV;
    low.val = lowV;
  }

  void setDouble(double val) {
    setLong(Double.doubleToLongBits(val));
  }

  void set(UnionSlot neo) {
    this.high = neo.high;
    this.low = neo.low;
  }

  // 取
  Instance getRef() {
    return high.ref;
  }

  int getInt() {
    return high.val;
  }

  float getFloat() {
    return Float.intBitsToFloat(getInt());
  }

  long getLong() {
    final int high = this.high.val;
    final int low = this.low.val;
    long l1 = (high & 0x000000ffffffffL) << 32;
    long l2 = low & 0x00000000ffffffffL;
    return l1 | l2;
  }

  double getDouble() {
    return Double.longBitsToDouble(getLong());
  }
}

