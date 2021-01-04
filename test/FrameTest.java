public class FrameTest {

  public static void testFrameLocalVars() {
    final Frame frame = new Frame(10, 10);
    frame.setInt(0, 10);
    Assert.assertEq(10, frame.getInt(0), "set int 10");

    frame.setFloat(1, 10f);
    Assert.assertEq(10f, frame.getFloat(1), "set float 10f");

    frame.setLong(1, 10L);
    Assert.assertEq(10L, frame.getLong(1), "set long 10L");

    frame.setLong(1, Integer.MAX_VALUE + 100L);
    Assert.assertEq(Integer.MAX_VALUE + 100L, frame.getLong(1), "set long Integer.MAX_VALUE + 100L");

    frame.setDouble(1, 10d);
    Assert.assertEq(10d, frame.getDouble(1), "set double 10d");

    final Instance obj = new Instance(null, null);
    frame.setRef(2, obj);
    Assert.assertEq(obj, frame.getRef(2), "set ref obj");
  }

  public static void testFrameOperandStack() {
    final Frame frame = new Frame(10, 10);

    frame.pushInt(10);
    Assert.assertEq(10, frame.popInt(), "push int 10");

    frame.pushFloat(10f);
    Assert.assertEq(10f, frame.popFloat(), "push float 10f");

    frame.pushLong(10L);
    Assert.assertEq(10L, frame.popLong(), "push long 10L");

    frame.pushLong(Integer.MAX_VALUE + 100L);
    Assert.assertEq(Integer.MAX_VALUE + 100L, frame.popLong(), "push long Integer.MAX_VALUE + 100L");

    frame.pushDouble(10d);
    Assert.assertEq(10d, frame.popDouble(), "push double 10d");

    final Instance obj = new Instance(null, null);
    frame.pushRef(obj);
    Assert.assertEq(obj, frame.popRef(), "push ref obj");
  }
}