import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Hello2 {

  public static void main(String[] args) throws FileNotFoundException {
    PrintStream out = new PrintStream(new FileOutputStream("temp"));
    System.setOut(out);

    System.out.println("...");
    for (String arg : args) {
      System.out.println(arg);
    }
  }
}
