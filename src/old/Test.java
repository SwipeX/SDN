package old;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Tim
 * Date: 4/27/13
 * Time: 3:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class Test {

    public static void main(String... args) {
        File[] sources = new File("./scripts").listFiles();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

    }
}
