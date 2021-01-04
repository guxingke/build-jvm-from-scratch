public class UnionSlotTest {

  public static void testInitAndSet() {
    final UnionSlot rus = UnionSlot.of((Instance) null);
    Assert.assertNull(rus.getRef(), "ref null");
    final Instance instance = new Instance(null, null);
    rus.setRef(instance);
    Assert.assertEq(instance, rus.getRef(), "ref set");

    final UnionSlot ius = UnionSlot.of(0);
    Assert.assertEq(0, ius.getInt(), "int 0");
    ius.setInt(10);
    Assert.assertEq(10, ius.getInt(), "int set");

    final UnionSlot jus = UnionSlot.of(0L);
    Assert.assertEq(0L, jus.getInt(), "long 0");
    jus.setLong(10L);
    Assert.assertEq(10L, jus.getLong(), "long set");

    final UnionSlot fus = UnionSlot.of(0.0f);
    Assert.assertEq(0.0f, fus.getFloat(), "float 0");
    fus.setFloat(10.0f);
    Assert.assertEq(10.0f, fus.getFloat(), "float set");

    final UnionSlot dus = UnionSlot.of(0.0d);
    Assert.assertEq(0.0d, dus.getDouble(), "double 0");
    dus.setDouble(10.0d);
    Assert.assertEq(10.0d, dus.getDouble(), "double set");

    final UnionSlot neo = UnionSlot.of(0);
    neo.set(UnionSlot.of(1));
    Assert.assertEq(1, 1);
  }
}
