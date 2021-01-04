
class ObjectOverride1 {

  protected void t1() {
    System.out.println(1);
  }

  protected void t2() {
    System.out.println(2);
  }

  protected void t3() {
    System.out.println(3);
  }
}

class ObjectOverride2 extends ObjectOverride1 {

  protected void t2() {
    System.out.println(0);
  }
}

class ObjectOverride3 extends ObjectOverride2 {

  protected void t3() {
    System.out.println(0);
  }
}

class ObjectOverride {
  public static void main(String[] args) {
    // 1 2 3
    ObjectOverride1 o1 = new ObjectOverride1();
    o1.t1();
    o1.t2();
    o1.t3();

    // 1 0 3
    ObjectOverride2 o2 = new ObjectOverride2();
    o2.t1();
    o2.t2();
    o2.t3();

    // 1 0 0
    ObjectOverride3 o3 = new ObjectOverride3();
    o3.t1();
    o3.t2();
    o3.t3();

    // 1 0 3
    ObjectOverride1 o21 = new ObjectOverride2();
    o21.t1();
    o21.t2();
    o21.t3();

    // 1 0 0
    ObjectOverride1 o31 = new ObjectOverride3();
    o31.t1();
    o31.t2();
    o31.t3();
  }
}


