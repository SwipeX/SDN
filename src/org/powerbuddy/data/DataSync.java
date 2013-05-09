package org.powerbuddy.data;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.powerbuddy.gui.NameMatcher;
import org.powerbuddy.net.FTP;
import org.powerbuddy.net.FileDownload;
import org.powerbuddy.script.Manifest;
import org.powerbuddy.script.Script;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created with IntelliJ IDEA.
 * User: Tim
 * Date: 5/8/13
 * Time: 12:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataSync {
    public static ArrayList<ScriptData> scriptData = new ArrayList<>();

    public static void pushData() {
        for (ScriptData scripts : scriptData) {

                scripts.id = Integer.parseInt(readLine("http://www.powerbuddy.org/scripts/next.php"));
                String type = NameMatcher.getType(scripts.manifest.name(), scripts.manifest.description());
                post("http://www.powerbuddy.org/scripts/create.php", "author=" + scripts.manifest.author(), "desc=" + scripts.manifest.description(),    "name=" + scripts.manifest.name(), "thread=powerbuddy.org", "type=1", "skilltype=" + type, "id=" + scripts.id);

        }
    }

    public static String readLine(String url) {

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try (final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            if ((line = in.readLine()) != null) {
                return line;
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public static String post(final String url, String... args) {
        try {
            final StringBuilder builder = new StringBuilder();
            boolean first = true;
            for (final String arg : args) {
                if (first) {
                    builder.append(arg);
                    first = false;
                } else {
                    builder.append("&").append(arg);
                }
            }
            final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            final OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(builder.toString());
            writer.flush();
            writer.close();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            final StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            return result.toString();
        } catch (final IOException e) {
            return null;
        }
    }

    public static void downloadFtp() {
        FTPFile[] files = FileDownload.list("/jars/");
        FTP.download("./ftp/", FileDownload.ftp, files, false);

    }

    public static void gatherData() {
        try {
            for (File file : new File("./scripts/ftp/").listFiles()) {
                URLClassLoader clazzLoader = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()});
                JarFile jar = new JarFile(file);
                Enumeration en = jar.entries();
                while (en.hasMoreElements()) {
                    JarEntry entry = (JarEntry) en.nextElement();
                    if (entry.getName().contains(".class")) {
                        Class clazz = clazzLoader.loadClass(entry.getName().replaceAll(".class", "").replaceAll("/", "."));

                        if (clazz.newInstance() instanceof Script) {

                            scriptData.add(new ScriptData((Manifest) clazz.getAnnotation(Manifest.class), file));
                            break;
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
