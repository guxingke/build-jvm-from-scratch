public class LLoadStore {
  public static void main(String[] args) {
    long l1 = 0L; // lstore1
    long l2 = 1L; // lstore2
    long l3 = 0L; // lstore3
    long l4 = 1L; // lstore n

    System.out.println(l4);
    System.out.println(l3);
    System.out.println(l2); // lload2
    System.out.println(l1); // lload1
  }
}