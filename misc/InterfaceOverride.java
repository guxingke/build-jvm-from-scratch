interface Interface1 {

  void v1();
}

interface Interface2 extends Interface1 {

  void v2();
}

class InterfaceImpl1 implements Interface1 {

  @Override
  public void v1() {
    System.out.println(1);
  }
}

class InterfaceImpl2 implements Interface2 {

  @Override
  public void v1() {
    System.out.println(2);
  }

  @Override
  public void v2() {
    System.out.println(3);
  }
}

class InterfaceOverride {

  public static void main(String[] args) {
    Interface1 i1 = new InterfaceImpl1();
    i1.v1();
    Interface1 i12 = new InterfaceImpl2();
    i12.v1();

    Interface2 i2 = new InterfaceImpl2();
    i2.v1();
    i2.v2();

    InterfaceImpl1 impl1 = new InterfaceImpl1();
    impl1.v1();
    InterfaceImpl2 impl2 = new InterfaceImpl2();
    impl2.v1();
    impl2.v2();
  }
}
