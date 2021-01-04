public class NullPointerExceptions {
  int x;
  public static void main(String[] args) {
    NullPointerExceptions obj = new NullPointerExceptions();
    obj.v1();
    
    NullPointerExceptions obj2 = null;
    try {
      obj2.v1();
    } catch (NullPointerException e) {
      System.out.println(1);
    }

    try {
      obj2.x = 1;
    } catch (NullPointerException e) {
      System.out.println(2);
    }

    try {
      System.out.println(obj2.x);
    } catch (NullPointerException e) {
      System.out.println(3);
    }
  }
  void v1() {}
}


