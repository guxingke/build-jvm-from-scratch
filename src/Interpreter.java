import java.util.Arrays;

// 解释器
class Interpreter {

  /**
   * 解析器运行
   */
  public static void run() {
    // 核心循环
    ExecEnv env = MetaSpace.getMainEnv();
    do {
      Frame frame = env.topFrame();
      Instruction instruction = frame.getInstruction();
      // 执行指令
      if (Main.verboseInt) {
        if (Main.verboseIntArg == null || frame.method.getKey().equals(Main.verboseIntArg)) {
          final char[] chars = new char[env.top * 2 + 1];
          Arrays.fill(chars, ' ');
          String space = new String(chars);
          System.out.println(space + frame.pc + " : " + instruction.name());
        }
      }
      instruction.eval(frame);
      if (env.exception != null) {
        Utils.handleException(env.exception);
        env.exception = null;
      }
    } while (!env.isEmpty());
  }

  /**
   * 同步执行指定方法
   */
  public static void execute(Method method) {
    final ExecEnv env = MetaSpace.getMainEnv();
    Frame newFrame = new Frame(method);
    final Frame old = env.topFrame();
    // 传参
    final int slots = method.getArgSlotSize();
    for (int i = slots - 1; i >= 0; i--) {
      newFrame.set(i, old.pop());
    }
    execute(newFrame);
  }

  /**
   * 同步执行栈帧
   */
  public static void execute(Frame newFrame) {
    final ExecEnv env = MetaSpace.getMainEnv();
    env.pushFrame(newFrame);

    newFrame.stat = Const.FAKE_FRAME;
    do {
      Frame frame = env.topFrame();
      Instruction instruction = frame.getInstruction();
      // 执行指令
      if (Main.verboseInt) {
        if (Main.verboseIntArg == null || frame.method.getKey().equals(Main.verboseIntArg)) {
          final char[] chars = new char[env.top * 2 + 1];
          Arrays.fill(chars, ' ');
          String space = new String(chars);
          System.out.println(space + frame.pc + " : " + instruction.name());
        }
      }
      instruction.eval(frame);
      if (env.exception != null) {
        Utils.handleException(env.exception);
        env.exception = null;
      }
    } while (newFrame.stat == Const.FAKE_FRAME);
  }
}