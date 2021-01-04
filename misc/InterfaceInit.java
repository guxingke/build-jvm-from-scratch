class InterfaceObj {
  int val = 0;
  { System.out.println(1); }
}

interface Interface111 {
  InterfaceObj x = new InterfaceObj();
  void v1();
}

interface Interface222 extends Interface111 {
  InterfaceObj y = new InterfaceObj();
  default void v2() { System.out.println(4); }
}

class InterfaceImpl111 implements Interface111 {
  public void v1() { System.out.println(2); }
}

class InterfaceImpl222 implements Interface222 {
  public void v1() { System.out.println(3); }
}

class InterfaceInit {

  public static void main(String[] args) {
    Interface111 i1 = new InterfaceImpl111();
    i1.v1();
    System.out.println(i1.x.val);

    Interface222 i2 = new InterfaceImpl222();
    i2.v1();
    System.out.println(i2.y.val);
  }
}
