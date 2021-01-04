public class ClassLoaderTest {

  public static void testLoadClassFromDir() {
    final ClassLoader loader = new ClassLoader("misc");

    final ClassFile cf = loader.loadClassFileFromDir("misc", "Test");
    Assert.assertNotNull(cf, "load class file from dir");
  }

  public static void testLoadClassFromJar() {
    final String home = System.getenv("JAVA_HOME");
    String runtimeJarPath = home + "/jre/lib/rt.jar";
    final ClassLoader loader = new ClassLoader(runtimeJarPath);

    final ClassFile cf = loader.loadClassFileFromJar(runtimeJarPath, "java/lang/Object");
    Assert.assertNotNull(cf, "load class file from jar");
  }

  public static void testLoadClassFromClasspath() {
    final String home = System.getenv("JAVA_HOME");
    String runtimeJarPath = home + "/jre/lib/rt.jar";

    String classpath = runtimeJarPath + ":" + "misc";
    final ClassLoader loader = new ClassLoader(classpath);

    final ClassFile cf = loader.loadClassFileFromClasspath(classpath, "java/lang/Object");
    Assert.assertNotNull(cf, "load java/lang/Object class file from compose classpath");

    final ClassFile tcf = loader.loadClassFileFromClasspath("misc", "Test");
    Assert.assertNotNull(tcf, "load Test class file from compose classpath");
  }

  public static void testLoadClass_object() throws Exception {
    final String home = System.getenv("JAVA_HOME");
    Assert.assertNotNull(home, "env JAVA_HOME");

    String runtimeJarPath = home + "/jre/lib/rt.jar";
    final ClassLoader loader = new ClassLoader(runtimeJarPath);

    // load java/lang/Object
    final Class clazz = loader.findClass("java/lang/Object");
    Assert.assertNull(clazz.superClass, "java/lang/Object super class is null");
    Assert.assertEq(14, clazz.methods.length, "java/lang/Object has 14 methods");
  }

  public static void testLoadClass_test() {
    final String home = System.getenv("JAVA_HOME");
    String runtimeJarPath = home + "/jre/lib/rt.jar";
    String classpath = runtimeJarPath + ":" + "misc";
    final ClassLoader loader = new ClassLoader(classpath);

    // load Test
    final Class clazz = loader.findClass("Test");
    Assert.assertNotNull(clazz.superClass, "Test super class is not null");
    Assert.assertEq(2, clazz.methods.length, "Test has 2 methods");
  }

  public static void testLoadClass_cache() {
    final String home = System.getenv("JAVA_HOME");
    String runtimeJarPath = home + "/jre/lib/rt.jar";
    String classpath = runtimeJarPath + ":" + "misc";
    final ClassLoader loader = new ClassLoader(classpath);

    // load Test
    final Class clazz = loader.findClass("Test");
    final Class clazz2 = loader.findClass("Test");

    Assert.assertEq(clazz, clazz2, "classloader cache");
  }
}