package org.powerbuddy.sdn;

import org.apache.commons.net.ftp.FTPFile;
import org.powerbuddy.asm.Inspector;
import org.powerbuddy.comp.Compiler;
import org.powerbuddy.data.DataSync;
import org.powerbuddy.net.FTP;
import org.powerbuddy.net.FileDownload;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created with IntelliJ IDEA.
 * User: Jasper
 * Date: 8-5-13
 * Time: 1:09
 * To change this template use File | Settings | File Templates.
 */
public class SDN {

    public static void clearLocal() {
        delete("./upload");
        delete("./bin");
        delete("./scripts/ftp");
        new File("./upload").mkdir();
        new File("./bin").mkdir();
        new File("./scripts/ftp").mkdir();
    }

    public static void main(String[] args) throws Exception {
        long millis2 = System.currentTimeMillis();
        System.out.println("Clearing local data");
        clearLocal();
        System.out.println("Listing ftp files");
        for (FTPFile f : FileDownload.list()) {
            if (f.getName().contains("zip")) {
                long millis = System.currentTimeMillis();
                System.out.println("1. Downloading " + f.getName());
                FTP.download("/src/", FileDownload.ftp, new FTPFile[]{f});
                delete("./scripts/src/" + f.getName().replace(".zip", ""));
                System.out.println("2. Extracting " + f.getName());
                FileDownload.extract("./scripts/src/" + f.getName());
                System.out.println("3. Compiling " + f.getName().replace(".zip", ""));
                Compiler.compile(f.getName().replace(".zip", "") + "/");
                File x  = new File("./bin/pakikiller/org");
                System.out.println("4. Jarring " + f.getName().replace(".zip", ""));
                Compiler.jar(f.getName().replace(".zip", ""));
                System.out.println("~ execution: "  + (System.currentTimeMillis() - millis) + " ms" );
                System.out.println();
            }
        }

        File upload = new File("./upload");
        Inspector inspector = new Inspector();
        for (File file : upload.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".jar")) {
                if (inspector.inspect(file)) {
                    System.out.println("Blocked " + file.getName() + ", for malicious content");
                    file.delete();
                }
            }
        }

        System.out.println("Replacing existing files in ftp");
        FTP.purge(FileDownload.ftp);
        FTP.up("/jars/", FileDownload.ftp, upload.listFiles());
        System.out.println("Updating script page");
        URL url = new URL("http://www.powerbuddy.org/scripts/clear.php?id=455");
        URLConnection con = url.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        con.getInputStream()));
        if(in.readLine().toLowerCase().contains("nigger")) {
            return;
        }
        in.close();


        DataSync.downloadFtp();
        DataSync.gatherData();
        DataSync.pushData();
        System.out.println("Total execution: " + (System.currentTimeMillis() - millis2) + " ms");
    }

    private static boolean delete(String dir) {
        File f = new File(dir);
        if (!f.exists()) {
            return false;
        }
        for (File file : f.listFiles()) {
            if (file.isDirectory()) {
                delete(dir + "/" + file.getName() + "/");
                file.delete();
            } else {
                file.delete();
            }
        }
        return f.delete();
    }


}
