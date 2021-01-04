public class MyJVM {
  public static void main(String[] args) {
    System.out.println(version());
  }
  public static native int version();
}
