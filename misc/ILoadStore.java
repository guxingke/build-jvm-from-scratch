public class ILoadStore {
  public static void main(String[] args) {
    int i1 = 1; // istore1
    int i2 = 2; // istore2
    int i3 = 3; // istore3
    int i4 = 4; // istore n

    System.out.println(i4);
    System.out.println(i3);
    System.out.println(i2); // iload2
    System.out.println(i1); // iload1
  }
}