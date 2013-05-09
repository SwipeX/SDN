package org.powerbuddy.comp;

import java.io.*;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

/**
 * Created with IntelliJ IDEA.
 * User: Jasper
 * Date: 12/29/12
 * Time: 12:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class Compiler {
    private static final int BUFFER_SIZE = 2156;
    private static byte[] mBuffer = new byte[BUFFER_SIZE];

    private static int mByteCount = 0;

    private static boolean mVerbose = false;

    private static String mDestJarName = "";
    private static final char SEP = '/';


    private static void jarDir(File dirOrFile2jar, JarOutputStream jos, String path) throws IOException {
        if (mVerbose)
            System.out.println("checking " + dirOrFile2jar);
        if (dirOrFile2jar.isDirectory()) {
            String[] dirList = dirOrFile2jar.list();
            String subPath = (path == null) ? "" : (path + dirOrFile2jar.getName() + SEP);
            if (path != null) {
                JarEntry je = new JarEntry(subPath);
                je.setTime(dirOrFile2jar.lastModified());
                jos.putNextEntry(je);
                jos.flush();
                jos.closeEntry();
            }
            for (int i = 0; i < dirList.length; i++) {
                File f = new File(dirOrFile2jar, dirList[i]);
                jarDir(f, jos, subPath);
            }
        } else {
            if (dirOrFile2jar.getCanonicalPath().equals(mDestJarName)) {
                if (mVerbose)
                    System.out.println("skipping " + dirOrFile2jar.getPath());
                return;
            }

            if (mVerbose)
                System.out.println("adding " + dirOrFile2jar.getPath());
            FileInputStream fis = new FileInputStream(dirOrFile2jar);
            try {
                JarEntry entry = new JarEntry(path + dirOrFile2jar.getName());
                entry.setTime(dirOrFile2jar.lastModified());
                jos.putNextEntry(entry);
                while ((mByteCount = fis.read(mBuffer)) != -1) {
                    jos.write(mBuffer, 0, mByteCount);
                    if (mVerbose)
                        System.out.println("wrote " + mByteCount + " bytes");
                }
                jos.flush();
                jos.closeEntry();
            } catch (IOException ioe) {
                throw ioe;
            } finally {
                fis.close();
            }
        }
    }
    public static void jar(String script) throws IOException {
        Manifest manifest = new Manifest();

        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        File file = new File("./upload/" + script + ".jar");
        JarOutputStream target = new JarOutputStream(new FileOutputStream(file), manifest);
        File dir = new File("./bin/" + script);
        for(File f : dir.listFiles()) {
            jarDir(f, target, "");
        }
        target.close();
    }

    public static void compile(String directory) throws Exception {
        compile(directory, directory);
    }

    public static void compile(String name, String directory) throws Exception {
        File[] dir = new File("./scripts/src/" + directory).listFiles();
        for (File file : dir) {
            if (file.isDirectory()) {
                File direct = new File("bin/" + directory + file.getName());
                if(!direct.exists())
                    direct.mkdirs();
                compile(name, directory +  file.getName() + "/");

            }
        }

        for(File file : dir) {

            if(file.isFile()) {
                File f = new File("./bin/" + directory);
                if(!f.exists()) {
                    f.mkdirs();
                }
                Process compiler = Runtime.getRuntime().exec("javac -d bin/" + name + " -classpath ./powerbuddy.jar scripts/src/" + directory  + file.getName());
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
