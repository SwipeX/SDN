import java.io.*;
import java.util.jar.JarFile;

/**
 * Created with IntelliJ IDEA.
 * User: Tim
 * Date: 5/2/13
 * Time: 11:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompilableScript {
    File parent;
    JarFile export;
    byte state = 0;

    public CompilableScript(File parent) {
        this.parent = parent;
    }

    public File getParent() {
        return parent;
    }

    public JarFile getExport() {
        return export;
    }

    /**
     * 0- nothing has been done, 1- has been compiled, 2- has been jarred
     *
     * @return state of the script
     */
    public byte getState() {
        return state;
    }



    public void jar() {
        //get name from manifest
        try {
            Runtime.getRuntime().exec("jar cf " + "upload/" + parent.getName().replaceAll(".class", "") + ".jar -C ./bin/");
            File[] toDelete = new File("./bin").listFiles();
            //remove bin files
            for (File file : toDelete)
                file.delete();
            //set jarfile
            export = new JarFile("./upload/" + parent.getName().replaceAll(".class", ""));
            state = 2;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void compile() {
        try {
            for (File f : parent.listFiles()) {
                if (f.isDirectory()) {
                    Process compiler = Runtime.getRuntime().exec("javac -d bin -classpath ./powerbuddy.jar " + "./scripts/" + f.getName() + "/*.java");
                    printLines("error", compiler.getErrorStream());
                    compiler.waitFor();
                    for (File f1 : f.listFiles()) {
                        if (f1.isDirectory())
                            process(f.getName(), f1);
                    }
                    System.out.println("Compiled " + f.getName() + " Successfully.");
                } else {
                    //single file
                    if (f.getName().contains(".java")) {
                        Process compiler = Runtime.getRuntime().exec("javac -d bin -classpath ./powerbuddy.jar ./scripts/" + f.getName());
                        printLines("error", compiler.getErrorStream());
                        compiler.waitFor();
                        System.out.println("Compiled " + f.getName() + " Successfully.");
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        state = 1;
    }

    public static void process(String dir, File f) {
        try {
            Process compiler = Runtime.getRuntime().exec("javac -d bin -classpath ./powerbuddy.jar " + "./scripts/" + dir + "/" + f.getName() + "/*.java");
            printLines("error", compiler.getErrorStream());
            compiler.waitFor();
            System.out.println("Compiled " + f.getName() + " Successfully.");
            if (f.listFiles() != null) {
                for (File f1 : f.listFiles()) {
                    if (f1.isDirectory()) {
                        dir += "/" + f.getName() + "/";
                        process(dir, f1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static void printLines(String name, InputStream ins) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            System.out.println(name + " " + line);
        }
    }
}
