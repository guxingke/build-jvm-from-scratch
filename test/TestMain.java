public class TestMain {

  public static void main(String[] args) throws Exception {
    System.out.println("Test ClassReader...");
    ClassReaderTest.testRead();

    System.out.println("Test ClassLoader...");
    ClassLoaderTest.testLoadClassFromJar();
    ClassLoaderTest.testLoadClassFromDir();
    ClassLoaderTest.testLoadClassFromClasspath();
    ClassLoaderTest.testLoadClass_object();
    ClassLoaderTest.testLoadClass_test();
    ClassLoaderTest.testLoadClass_cache();

    System.out.println("Test Frame...");
    FrameTest.testFrameLocalVars();
    FrameTest.testFrameOperandStack();

    System.out.println("Test Signature...");
    SigTest.test_descriptor();

    System.out.println("Test UnionSlot...");
    UnionSlotTest.testInitAndSet();
  }
}
