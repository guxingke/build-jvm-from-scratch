import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

abstract class MetaSpace {

  static final Map<String, Class> CLASS_MAP = new HashMap<>();
  static final Map<Integer, Instance> RUNTIME_STRING_POOL = new HashMap<>();

  static Class findClass(String name) {
    return CLASS_MAP.get(name);
  }

  static void putClass(String name, Class clazz) {
    if (CLASS_MAP.containsKey(name)) {
      throw new IllegalStateException("duplicate class name, " + name);
    }
    CLASS_MAP.put(name, clazz);
  }

  static java.util.Collection<Class> allClass() {
    return CLASS_MAP.values();
  }

  /**
   * 类加载器
   */
  static ClassLoader loader;

  public static void setClassLoader(ClassLoader classLoader) {
    loader = classLoader;
  }

  public static ClassLoader getClassLoader() {
    return loader;
  }

  /**
   * 执行环境，目前没有多线程支持，执行环境只有 main
   */
  static ExecEnv main;

  static ExecEnv getMainEnv() {
    return main;
  }

  static void putMainEnv(ExecEnv env) {
    main = env;
  }

  static final Map<String, NativeMethod> NATIVE_METHOD_MAP = new HashMap<>();

  public static NativeMethod findNativeMethod(String key) {
    return NATIVE_METHOD_MAP.get(key);
  }

  public static void putNativeMethod(String key, NativeMethod nativeMethod) {
    NATIVE_METHOD_MAP.put(key, nativeMethod);
  }
}

class ExecEnv {

  // 栈顶，待 push 数据的位置
  int top;
  Frame[] frames;

  // 线程实例
  Instance thread;

  // 异常
  Instance exception;

  ExecEnv(int maxStack) {
    this.frames = new Frame[maxStack];
  }

  void pushFrame(Frame frame) {
    if (top >= this.frames.length) {
      throw new IllegalStateException("stackoverflow");
    }

    if (Main.verboseCall) {
      if (Main.verboseCallArg == null || frame.method.getKey().contains(Main.verboseCallArg)) {
        String space = "";
        if (top != 0) {
          final char[] chars = new char[top * 2];
          Arrays.fill(chars, ' ');
          space = new String(chars);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(">" + space + frame.method.getKey());
        if (top > 1) {
          sb.append(" <<< ").append(frames[top - 2].method.getKey());
        }
        System.out.println(sb.toString());
      }
    }

    frames[top++] = frame;
  }

  Frame popFrame() {
    if (top < 1) {
      throw new IllegalStateException("empty stack");
    }
    Frame frame = frames[--top];
    frames[top] = null; // 从数组中移除引用。

    if (Main.verboseCall) {
      if (Main.verboseCallArg == null || frame.method.getKey().contains(Main.verboseCallArg)) {
        String space = "";
        final char[] chars = new char[top * 2];
        Arrays.fill(chars, ' ');
        space = new String(chars);
        final StringBuilder sb = new StringBuilder();
        sb.append("<" + space + frame.method.getKey());
        if (top > 0) {
          sb.append(" >>> " + frames[top - 1].method.getKey());
        }
        System.out.println(sb.toString());
      }
    }
    return frame;
  }

  boolean isEmpty() {
    return this.top == 0;
  }

  // 当前栈顶栈帧
  public Frame topFrame() {
    return this.frames[top - 1];
  }

  public Frame callerFrame() {
    return this.frames[top - 2];
  }
}


/**
 * 本地方法接口
 */
interface NativeMethod {

  /**
   * 本地方法执行，根据方法定义, 消耗栈帧的参数，并推送返回值到栈顶
   */
  void invoke(Frame frame);
}
