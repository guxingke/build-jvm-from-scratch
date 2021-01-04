public class Math {
  public static void main(String[] args) {
    int i = 1;
    int m = 2;

    long li = 0L;
    long lm = 1L;

    float fi = 0f;
    float fm = 1f;

    double di = 0d;
    double dm = 1d;

    // *add
    System.out.println(i+2);
    System.out.println(li+1L);
    System.out.println(fi+1f);
    System.out.println(di+1d);
    // *sub
    System.out.println(2-i);
    System.out.println(1L-li);
    System.out.println(1f-fi);
    System.out.println(1d-di);
    // *mul
    System.out.println(2 * m);
    System.out.println(1L * lm);
    System.out.println(1f * fm);
    System.out.println(1d * dm);
    // *div
    System.out.println(6/m);
    System.out.println(1L/lm);
    System.out.println(1f/fm);
    System.out.println(1d/dm);
    // *rem
    System.out.println(6%m);
    System.out.println(1L%lm);
    System.out.println(1f%fm);
    System.out.println(1d%dm);
    // *neg
    System.out.println(-i);
    System.out.println(-li);
    System.out.println(-fi);
    System.out.println(-di);
    
    // *shl
    System.out.println(i << 1);
    System.out.println(li << 1);

    // *shr
    System.out.println(i >> 1);
    System.out.println(li >> 1);

    // *ushr
    System.out.println(i >>> 1);
    System.out.println(li >>> 1);

    // *and
    System.out.println(i & 1);
    System.out.println(li & 1L);
    // *or
    System.out.println(i | 1);
    System.out.println(li | 1L);
    // *xor
    System.out.println(i ^ 1);
    System.out.println(li ^ 1L);

    // iinc
    int ni = i;
    ni += 2;
    System.out.println(ni);
  }
}