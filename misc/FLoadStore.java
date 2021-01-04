public class FLoadStore {
  public static void main(String[] args) {
    float f1 = 1f; // fstore1
    float f2 = 0f; // fstore2
    float f3 = 1f; // fstore3
    float f4 = 0f; // fstore n

    System.out.println(f4);
    System.out.println(f3);
    System.out.println(f2); // fload2
    System.out.println(f1); // fload1
  }
}