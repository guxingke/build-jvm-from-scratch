public class NullPointerExceptions2 {
  public static void main(String[] args) {
    NullPointerExceptions2Obj obj = null;
    System.out.println(obj.obj.x);
  }
}

class NullPointerExceptions2Obj {
  NullPointerExceptions2Obj2 obj;
}

class NullPointerExceptions2Obj2 {
  int x;
}

