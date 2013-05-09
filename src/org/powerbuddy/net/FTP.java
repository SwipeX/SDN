package org.powerbuddy.net;


import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.powerbuddy.asm.Inspector;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Tim
 * Date: 12/20/12
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class FTP {



    public static void purge(FTPClient ftp) throws Exception {
        for(File file : new File("./upload").listFiles()) {
            ftp.changeWorkingDirectory("/jars/");
            ftp.deleteFile(Inspector.getScriptName(file) + ".jar");
        }

        for(FTPFile file : FileDownload.list("/src/")) {
            ftp.changeWorkingDirectory("/src/");
            ftp.deleteFile(file.getName());
        }

    }

    public static void download(String dir, FTPClient ftp, FTPFile[] children) {
        download(dir, ftp, children, true);
    }

    public static void download(String dir, FTPClient ftp, FTPFile[] children, boolean t) {
        try {
            for (FTPFile ftpFile : children) {
                if (ftpFile.isFile()) {
                    OutputStream output;
                    File f = new File("./scripts/" + dir);
                    if (!f.exists()) {
                        f.mkdirs();
                    }
                    output = new FileOutputStream("./scripts/" + dir + ftpFile.getName());
                    ftp.retrieveFile((t ? "/src/" : "/jars/") + ftpFile.getName(), output);
                } else if (ftpFile.isDirectory() && !ftpFile.getName().startsWith(".")) {
                    File f = new File("./scripts/" + dir);
                    if (!f.exists()) {
                        f.mkdirs();
                    }
                    dir += "/" + ftpFile.getName() + "/";
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
                    String name = Inspector.getScriptName(file) + ".jar";
                    if(name.contains("..jar")) {
                        continue;
                    }
                    ftp.storeFile(dir + name, new FileInputStream(file));
                } else if (file.isDirectory() && !file.getName().startsWith(".")) {
                    dir += "/" + file.getName() + "/";
                    ftp.makeDirectory(dir);
                    up(dir, ftp, file.listFiles());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}