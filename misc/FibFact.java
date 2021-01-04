public class FibFact {
  public static void main(String[] args) {
    // factorial
    int n = 10;
    int fact = 1;
    for (int i = 1; i <= n; i++) {
      fact *= i;
    }
    System.out.println(fact);

    // fibonacci
    int fib = 0;
    int x = 1;
    int y = 1;
    for (int i = 2; i < n; i++) {
      fib = x + y;
      x = y;
      y = fib;
    }
    System.out.println(fib);
  }
}