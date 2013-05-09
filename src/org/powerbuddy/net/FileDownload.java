package org.powerbuddy.net;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import org.powerbuddy.comp.Compiler;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * Created with IntelliJ IDEA.
 * User: Jasper
 * Date: 8-5-13
 * Time: 1:18
 * To change this template use File | Settings | File Templates.
 */
public class FileDownload {

    public static FTPClient ftp = null;

    public static FTPFile[] list() {
       return list("/src/");
    }

    public static FTPFile[] list(String dir) {
        String hostName = "192.210.232.20";
        String username = "static@powerbuddy.org";
        String password = "staticgonewild!";


        try {
            ftp = new FTPClient();
            ftp.connect(hostName);
            ftp.login(username, password);
            ftp.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
            ftp.changeWorkingDirectory(dir);


            return ftp.listFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static public void extract(String zipFile) throws ZipException, IOException {
        int BUFFER = 2048;
        File file = new File(zipFile);

        ZipFile zip = new ZipFile(file);
        String newPath = zipFile.substring(0, zipFile.length() - 4);

        new File(newPath).mkdir();
        Enumeration zipFileEntries = zip.entries();

        while (zipFileEntries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
            String currentEntry = entry.getName();
            File destFile = new File(newPath, currentEntry);
            File destinationParent = destFile.getParentFile();

            destinationParent.mkdirs();

            if (!entry.isDirectory()) {
                BufferedInputStream is = new BufferedInputStream(zip
                        .getInputStream(entry));
                int currentByte;
                byte data[] = new byte[BUFFER];

                FileOutputStream fos = new FileOutputStream(destFile);
                BufferedOutputStream dest = new BufferedOutputStream(fos,
                        BUFFER);

                while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, currentByte);
                }
                dest.flush();
                dest.close();
                is.close();
            }
        }
    }



}
