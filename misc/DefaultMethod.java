interface Interface11 {
  default void v1() { System.out.println(1); }
}

class InterfaceImpl11 implements Interface11 { }

class InterfaceImpl12 implements Interface11 {
  public void v1() { System.out.println(2); }
}

class DefaultMethod {
  public static void main(String[] args) {
    Interface11 i11 = new InterfaceImpl11();
    i11.v1();

    Interface11 i12 = new InterfaceImpl12();
    i12.v1();

    InterfaceImpl11 impl11 = new InterfaceImpl11();
    impl11.v1();

    InterfaceImpl12 impl12 = new InterfaceImpl12();
    impl12.v1();
  }
}
