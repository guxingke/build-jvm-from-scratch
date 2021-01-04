public abstract class Assert {

  private static final String RED = "\033[0;31m";
  private static final String GREEN = "\033[0;32m";
  private static final String NO_COLOR = "\033[0m";

  public static void assertEq(int expect, int actual, String message) {
    if (expect == actual) {
      System.out.println(String.format("Running test, %s ..." + " %sPassed%s", message, GREEN, NO_COLOR));
      return;
    }
    String mes = String.format("Running test, %s ..." + " %sFailed%s, expect %s but %s, Aborting.", message, RED, NO_COLOR, expect, actual);
    System.out.println(mes);
    System.exit(1);
  }

  public static void assertEq(int expect, int actual) {
    if (expect == actual) {
      return;
    }
    String mes = String.format("Running test, ..." + " %sFailed%s, expect %s but %s, Aborting.", RED, NO_COLOR, expect, actual);
    System.out.println(mes);
    System.exit(1);
  }

  public static void assertNotNull(Object actual, String message) {
    if (actual != null) {
      System.out.println(String.format("Running test, %s ..." + " %sPassed%s", message, GREEN, NO_COLOR));
      return;
    }
    String mes = String.format("Running test, %s ..." + " %sFailed%s, but null, Aborting.", message, RED, NO_COLOR);
    System.out.println(mes);
    System.exit(1);
  }

  public static void assertNull(Object actual, String message) {
    if (actual == null) {
      System.out.println(String.format("Running test, %s ..." + " %sPassed%s", message, GREEN, NO_COLOR));
      return;
    }
    String mes = String.format("Running test, %s ..." + " %sFailed%s, but not null, Aborting.", message, RED, NO_COLOR);
    System.out.println(mes);
    System.exit(1);
  }

  public static void assertEq(long expect, long actual, String message) {
    if (expect == actual) {
      System.out.println(String.format("Running test, %s ..." + " %sPassed%s", message, GREEN, NO_COLOR));
      return;
    }
    String mes = String.format("Running test, %s ..." + " %sFailed%s, expect %s but %s, Aborting.", message, RED, NO_COLOR, expect, actual);
    System.out.println(mes);
    System.exit(1);
  }

  public static void assertEq(float expect, float actual, String message) {
    if (expect == actual) {
      System.out.println(String.format("Running test, %s ..." + " %sPassed%s", message, GREEN, NO_COLOR));
      return;
    }
    String mes = String.format("Running test, %s ..." + " %sFailed%s, expect %s but %s, Aborting.", message, RED, NO_COLOR, expect, actual);
    System.out.println(mes);
    System.exit(1);
  }

  public static void assertEq(double expect, double actual, String message) {
    if (expect == actual) {
      System.out.println(String.format("Running test, %s ..." + " %sPassed%s", message, GREEN, NO_COLOR));
      return;
    }
    String mes = String.format("Running test, %s ..." + " %sFailed%s, expect %s but %s, Aborting.", message, RED, NO_COLOR, expect, actual);
    System.out.println(mes);
    System.exit(1);
  }

  public static void assertEq(Object expect, Object actual, String message) {
    if (expect == actual) {
      System.out.println(String.format("Running test, %s ..." + " %sPassed%s", message, GREEN, NO_COLOR));
      return;
    }
    String mes = String.format("Running test, %s ..." + " %sFailed%s, expect %s but %s, Aborting.", message, RED, NO_COLOR, expect, actual);
    System.out.println(mes);
    System.exit(1);
  }
}