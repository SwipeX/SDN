package old;

import org.powerbuddy.script.Manifest;
import org.powerbuddy.script.Script;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created with IntelliJ IDEA.
 * User: Tim
 * Date: 12/29/12
 * Time: 12:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class Compiler {
    static ArrayList<ScriptEntry> entries = new ArrayList<ScriptEntry>();

    public static void purge() {
        File[] files = new File[]{new File("./scripts"), new File("./bin"), new File("./upload")};
        for (File f : files) {
            if (f.isDirectory()) {
                for (File file : f.listFiles())
                    file.delete();
            }
        }
    }

    public static void main(String[] args) {
        //this cleans the folders...
        purge();
        //sync src from all scripters
        old.Sync.sync();
        //compile
        try {
            for (File f : new File("./scripts").listFiles()) {
                //directory
                if (f.isDirectory()) {
                    Process compiler = Runtime.getRuntime().exec("javac -d bin -classpath ./powerbuddy.jar " + "./scripts/" + f.getName() + "/*.java");
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
                        Process compiler = Runtime.getRuntime().exec("javac -d bin -classpath ./powerbuddy.jar ./scripts/" + f.getName());
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

        for (File f : new File("./upload").listFiles()) {
            int id = -1;
            if (getAttributes(f) == null)
                continue;
            System.out.println(getAttributes(f));
            String[] data = getAttributes(f).split(">");
            if (data.length >= 5)
                entries.add(new ScriptEntry(f.getName(), data[0], data[1], data[2], 1.0, data[4], id));
            else
                entries.add(new ScriptEntry(f.getName(), data[0], data[1], data[2], 1.0, "http://www.powerbuddy.org", id));
        }
        new old.Sync.FTP().upload();
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

    /**
     * @param file
     * @return attributes seperated by comma
     */
    public static String getAttributes(File file) {
        Class<?> clazz = null;
        try {
            final JarFile jar = new JarFile(file);
            final Enumeration<JarEntry> entries = jar.entries();
            final ClassLoader classloader = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()});
            while (entries.hasMoreElements()) {
                final String name = entries.nextElement().getName().replace('/', '.').replace('\\', '.');
                if (name.contains("$") || !name.endsWith(".class")) {
                    continue;
                }
                System.out.println(name);
                clazz = classloader.loadClass(name.substring(0, name.length() - 6));
                if (clazz != null) {
                    if (clazz.isAnnotationPresent(Manifest.class)) {
                        final Manifest manifest = clazz.getAnnotation(Manifest.class);
                        String attr = "";
                        return attr + manifest.name() + ">" + manifest.description().replaceAll(" ", "%20") + ">" + manifest.author() + ">" + manifest.version();// + "," + manifest.thread();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File findScriptClass(final String fileLoc) {
        final File directory = new File(fileLoc);
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                findScriptClass(file.listFiles()[0].getAbsolutePath());
            } else if (file.getName().contains(".class") && !file.getName().contains("$")) {
                String className = file.getName().replace(".class", "");
                Class<?> toLoad = null;
                try {
                    // Convert File to a URL
                    URL url = file.toURI().toURL(); // file:/c:/myclasses/
                    URL[] urls = new URL[]{url};
                    // Create a new class loader with the directory
                    ClassLoader cl = new URLClassLoader(urls);
                    // Load in the class; MyClass.class should be located in
                    // the directory file:/c:/myclasses/com/mycompany
                    toLoad = cl.loadClass(className);
                    if (!(toLoad.newInstance() instanceof Script)) {
                        continue;
                    }
                } catch (MalformedURLException e) {
                } catch (ClassNotFoundException e) {
                } catch (InstantiationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Manifest manifest = toLoad.getAnnotation(Manifest.class);
                if (manifest == null || manifest.name() == null
                        || manifest.name().length() < 1) {
                    System.out.println((manifest == null) + " " + manifest.name() == null);
                }
                return file;

            }
        }
        return null;
    }
}
