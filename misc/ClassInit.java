class ClassInit1 {
  static {
    System.out.println(1);
  }
  public static void main(String[] args) {
    System.out.println(2);
  }
}
class ClassInit2 extends ClassInit1{
  static {
    System.out.println(2);
  }
  public static void main(String[] args) {
    System.out.println(3);
  }
}
class ClassInit3 extends ClassInit2 {
  static {
    System.out.println(3);
  }
  public static void main(String[] args) {
    System.out.println(4);
  }
}
