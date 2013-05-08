package org.powerbuddy.comp;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Jasper
 * Date: 12/29/12
 * Time: 12:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class Compiler {


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


    private static void printLines(String name, InputStream ins) throws Exception {
        String line;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            System.out.println(name + " " + line);
        }
    }


}
