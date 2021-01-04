public class StaticCall2 {
  public static void main(String[] args) {
    int i = test();
    System.out.println(i);
  }

  public static int test() {
    return 1;
  }
}
