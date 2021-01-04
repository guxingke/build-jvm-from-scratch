class Exceptions {
  public static void main(String[] args) {
    test(0);
    test(1);

    test2();
  }

  static void test(int n) {
    try {
      if (n == 0) throw new IllegalStateException("0");
      throw new Exception("1");
    }
    catch (IllegalStateException ise) {
      System.out.println("catch by IllegalStateException");
    }
    catch (Exception e) {
      System.out.println("catch by exception");
    }
  }

  static void test2() {
    throw new RuntimeException("sometimes naive");
  }
}

