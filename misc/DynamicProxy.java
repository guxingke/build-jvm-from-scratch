import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

class DynamicProxyHandler implements InvocationHandler {

  private final Object proxied;

  DynamicProxyHandler(Object proxied) {
    this.proxied = proxied;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    System.out.println("before: " + method.getName());
    Object ret = method.invoke(proxied, args);
    System.out.println("after: " + method.getName());
    return ret;
  }
}

interface DynamicProxyInterface {

  void t1();

  void t2();
}

class DynamicProxyReal implements DynamicProxyInterface {

  @Override
  public void t1() {
    System.out.println("t1");
  }

  @Override
  public void t2() {
    System.out.println("t2");
  }
}


class DynamicProxyTest {

  public static void main(String[] args) {
    final DynamicProxyReal real = new DynamicProxyReal();

    final DynamicProxyInterface proxy = (DynamicProxyInterface) Proxy
        .newProxyInstance(DynamicProxyInterface.class.getClassLoader(),
            new java.lang.Class[]{DynamicProxyInterface.class}, new DynamicProxyHandler(real));

    proxy.t1();
    proxy.t2();
  }
}
