import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

class ReflectionObj {

  static String x = "x";
  String y;

  ReflectionObj() { this.y = "1"; }

  ReflectionObj(String y) { this.y = y; }

  static void s1() { System.out.println(x); }

  void o1() { System.out.println(y); }
}

class ReflectionObj2 {
  ReflectionObj2() {
    System.out.println("o2");
  }
}

class ReflectionTest {

  public static void main(String[] args) throws Exception {
    // class
    java.lang.Class<ReflectionObj> cls = ReflectionObj.class;
    System.out.println(cls.getName());

    // default constructor
    ReflectionObj obj = cls.newInstance();
    System.out.println(obj.y);

    // special constructor
    final Constructor<ReflectionObj> c2 = cls.getDeclaredConstructor(String.class);
    final ReflectionObj obj2 = c2.newInstance("10");
    System.out.println(obj2.y);

    // static field x
    final Field fieldX = cls.getDeclaredField("x");
    final String fx = (String) fieldX.get(obj2);
    System.out.println(fx);
    // static
    final String fx2 = (String) fieldX.get(null);
    System.out.println(fx2);

    // instance field y
    final Field fieldY = cls.getDeclaredField("y");
    final String fy = (String) fieldY.get(obj2);
    System.out.println(fy);

    // methods
    final Method[] methods = cls.getDeclaredMethods();
    for (Method method : methods) {
      System.out.println(method.getName());
    }

    // static method call
    final Method m1 = cls.getDeclaredMethod("s1");
    m1.invoke(null);
    m1.invoke(obj2);

    // instance method call
    final Method o1 = cls.getDeclaredMethod("o1");
    o1.invoke(obj2);
    o1.invoke(obj);

    final java.lang.Class<?> cls2 = java.lang.Class.forName("ReflectionObj2");
    System.out.println(cls2.getName());

    // class not found
    try {
      java.lang.Class.forName("ReflectionObj3");
    } catch (ClassNotFoundException e) {
      System.out.println(e.getMessage());
    }
  }
}
