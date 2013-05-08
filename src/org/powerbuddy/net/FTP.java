package org.powerbuddy.net;


import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.powerbuddy.asm.Inspector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Tim
 * Date: 12/20/12
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class FTP {



    public static void download(String dir, FTPClient ftp, FTPFile[] children) {
        try {
            for (FTPFile ftpFile : children) {
                if (ftpFile.isFile()) {
                    OutputStream output;
                    File f = new File("./scripts/" + dir);
                    if (!f.exists()) {
                        f.mkdirs();
                    }
                    output = new FileOutputStream("./scripts/" + dir + ftpFile.getName());
                    ftp.retrieveFile("/src/" + ftpFile.getName(), output);
                    System.out.println("Syncing: " + dir + ftpFile.getName());
                } else if (ftpFile.isDirectory() && !ftpFile.getName().startsWith(".")) {
                    File f = new File("./scripts/" + dir);
                    if (!f.exists()) {
                        f.mkdirs();
                    }
                    dir += "/" + ftpFile.getName() + "/";
                    System.out.println("Syncing dir: " + dir);
                    download(dir, ftp, ftp.listFiles("/in/" + dir));
                }
            }
        } catch (Exception e) {

        }
    }

    public static void up(String dir, FTPClient ftp, File[] children) {
        try {
            for (File file : children) {
                if (file.isFile() && file.getName().endsWith(".jar")) {
                    ftp.storeFile(dir + Inspector.getScriptName(file) + "." + file.getName(), new FileInputStream(file));
                    System.out.println("Syncing: " + dir + Inspector.getScriptName(file) + "." + file.getName());
                } else if (file.isDirectory() && !file.getName().startsWith(".")) {
                    dir += "/" + file.getName() + "/";
                    ftp.makeDirectory(dir);
                    System.out.println("Syncing dir: " + dir);
                    up(dir, ftp, file.listFiles());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}