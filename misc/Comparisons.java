public class Comparisons {
  public static void main(String[] args) {
    int i = 1;
    int j = 10;
    if (i == 0) { // ifne
      System.out.println(0);
    }
    if (i != 0) { // ifeq
      System.out.println(1);
    }
    if (i > 0) { // ifle
      System.out.println(2);
    }
    if (i >= 0) { // iflt
      System.out.println(3);
    }
    if (i < 0) { // ifge
      System.out.println(4);
    }
    if (i <= 0) { // ifgt
      System.out.println(5);
    }

    if (i == j) { // if_icmpne
      System.out.println(0);
    }
    if (i != j) { // if_icmpeq
      System.out.println(1);
    }
    if (i > j) { // if_icmple
      System.out.println(2);
    }
    if (i >= j) { // if_icmplt
      System.out.println(3);
    }
    if (i < j) { // if_icmpge
      System.out.println(4);
    }
    if (i <= j) { // if_icmpgt
      System.out.println(5);
    }
    
    long li = 1L;
    long lj = 0L;
    if (li > lj) { // lcmp
      System.out.println(1L);
    }

    float fi = 1f;
    float fj = 0f;
    if (fi > fj) { // fcmpl
      System.out.println(1f);
    }
    if (fj < fi) { // fcmpg
      System.out.println(1f);
    }

    double di = 1d;
    double dj = 0d;
    if (di > dj) { // dcmpl
      System.out.println(1d);
    }
    if (dj < di) { // dcmpg
      System.out.println(1d);
    }
  }
}