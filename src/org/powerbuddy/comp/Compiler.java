package org.powerbuddy.comp;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Tim
 * Date: 12/29/12
 * Time: 12:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class Compiler {

    public static void purge() {
        File[] files = new File[]{new File("./scripts"), new File("./bin"), new File("./upload")};
        for (File f : files) {
            if (f.isDirectory()) {
                for (File file : f.listFiles())
                    file.delete();
            }
        }
    }

    public static void jar() throws Exception {
        for (File f : new File("./bin").listFiles()) {

            if (f.isDirectory()) {
                Process jar = null;
                jar = Runtime.getRuntime().exec("jar cf " + "upload/" + f.getName().replaceAll(".class", "") + ".jar -C ./bin/ " + f.getName());
                printLines("error", jar.getErrorStream());
                jar.waitFor();
            } else {
                if (f.getName().contains(".class") && !f.getName().contains("$")) {
                    Process jar = null;
                    jar = Runtime.getRuntime().exec("jar cf " + "upload/" + f.getName().replaceAll(".class", "") + ".jar -C ./bin/ " + f.getName());
                    printLines("error", jar.getErrorStream());
                    jar.waitFor();
                    System.out.println("Jarred " + f.getName() + " Successfully.");
                }
            }
        }
    }

    public static void compile(String directory) throws Exception {
        File[] dir = new File("./scripts/src/" + directory).listFiles();
        for(File file : dir) {
            if(file.isDirectory()) {
                compile(directory + file.getName() + "/");
            } else {
                File direct = new File("bin/" + directory);
                direct.mkdir();
                Process compiler = Runtime.getRuntime().exec("javac -d bin/" + directory + " -classpath ./powerbuddy.jar ./scripts/src/" + directory + "/" + file.getName());
                printLines("error", compiler.getErrorStream());
                compiler.waitFor();
            }
        }
    }

    public static void compile() {
        //this cleans the folders...
        purge();
        //sync src from all scripters

        //compile
        try {
            for (File f : new File("./scripts/src/").listFiles()) {
                //directory
                if (f.isDirectory()) {
                    File file = new File("./bin/" + f.getName());
                    file.mkdir();
                    Process compiler = Runtime.getRuntime().exec("javac -d bin/" +f.getName() + " -classpath ./powerbuddy.jar " + "./scripts/src/" + f.getName() + "/*.java");
                    printLines("error", compiler.getErrorStream());
                    compiler.waitFor();
                    for (File f1 : f.listFiles()) {
                        if (f1.isDirectory())
                            compile(f.getName(), f1);
                    }
                    System.out.println("Compiled " + f.getName() + " Successfully.");
                } else {
                    //single file
                    if (f.getName().contains(".java")) {
                        Process compiler = Runtime.getRuntime().exec("javac -d bin -classpath ./powerbuddy.jar ./scripts/src/" + f.getName());
                        printLines("error", compiler.getErrorStream());
                        compiler.waitFor();
                        System.out.println("Compiled " + f.getName() + " Successfully.");
                    }

                }
            }
            for (File f : new File("./bin").listFiles()) {

                if (f.isDirectory()) {
                    Process jar = null;
                    jar = Runtime.getRuntime().exec("jar cf " + "upload/" + f.getName().replaceAll(".class", "") + ".jar -C ./bin/ " + f.getName());
                    printLines("error", jar.getErrorStream());
                    jar.waitFor();

                } else {
                    if (f.getName().contains(".class") && !f.getName().contains("$")) {
                        Process jar = null;
                        jar = Runtime.getRuntime().exec("jar cf " + "upload/" + f.getName().replaceAll(".class", "") + ".jar -C ./bin/ " + f.getName());
                        printLines("error", jar.getErrorStream());
                        jar.waitFor();
                        System.out.println("Jarred " + f.getName() + " Successfully.");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    public static void compile(String dir, File f) {
        try {
            Process compiler = Runtime.getRuntime().exec("javac -d bin -classpath ./powerbuddy.jar " + "./scripts/" + dir + "/" + f.getName() + "/*.java");
            printLines("error", compiler.getErrorStream());
            compiler.waitFor();
            System.out.println("Compiled " + f.getName() + " Successfully.");
            if (f.listFiles() != null) {
                for (File f1 : f.listFiles()) {
                    if (f1.isDirectory()) {
                        dir += "/" + f.getName() + "/";
                        compile(dir, f1);
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
