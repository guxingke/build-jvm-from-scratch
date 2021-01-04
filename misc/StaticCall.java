public class StaticCall {
  public static void main(String[] args) {
    System.out.println(0);
    test();
    System.out.println(2);
  }

  public static void test() {
    System.out.println(1);
  }
}
