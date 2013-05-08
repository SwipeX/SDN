package org.powerbuddy.sdn;

import org.apache.commons.net.ftp.FTPFile;
import org.powerbuddy.asm.Inspector;
import org.powerbuddy.comp.Compiler;
import org.powerbuddy.net.FTP;
import org.powerbuddy.net.FileDownload;

import java.io.File;

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
        new File("./upload").mkdir();
        new File("./bin").mkdir();
    }

    public static void main(String[] args) throws Exception {
        clearLocal();
      for (FTPFile f : FileDownload.list()) {
            if (f.getName().contains("zip")) {
                FTP.download("/src/", FileDownload.ftp, new FTPFile[]{f});
                delete("./scripts/src/" + f.getName().replace(".zip", ""));

                FileDownload.extract("./scripts/src/" + f.getName());
                Compiler.compile(f.getName().replace(".zip", "") + "/");
            }
        }
        Compiler.jar();
        File upload =  new File("./upload");
        for(File file : upload.listFiles()) {
            if(file.isFile() && file.getName().endsWith(".jar")) {
                if(Inspector.inspect(file)) {
                   // file.delete();
                }
            }
        }
        FTP.up("/jars/", FileDownload.ftp, upload.listFiles());

    }

    private static boolean delete(String dir) {
        File f = new File(dir);
        if(!f.exists()) {
            return false;
        }
        for(File file : f.listFiles()) {
            if(file.isDirectory()) {
                delete(dir + "/" + file.getName() + "/");
                file.delete();
            } else {
                file.delete();
            }
        }
        return f.delete();
    }
}
