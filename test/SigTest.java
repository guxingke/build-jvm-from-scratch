public class SigTest {

  public static void test_descriptor() {
    Assert.assertEq(0, Utils.getArgSlotSize("()V"));
    Assert.assertEq(4, Utils.getArgSlotSize("(II[CI)V"));
    Assert.assertEq(4, Utils.getArgSlotSize("(IILjava/lang/String;I)V"));
    Assert.assertEq(4, Utils.getArgSlotSize("(II[Ljava/lang/String;I)V"));
    Assert.assertEq(4, Utils.getArgSlotSize("([[[II[Ljava/lang/String;I)V"));
    Assert.assertEq(10, Utils.getArgSlotSize("([[[I[J[DI[Ljava/lang/String;IJD)V"), "([[[I[J[DI[Ljava/lang/String;IJD)V args slots is 10");
  }
}
