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
        String hostName = "192.210.232.20";
        String username = "static@powerbuddy.org";
        String password = "staticgonewild!";


        try {
            //FTPClientConfig config = new FTPClientConfig(FTPClientConfig.SYST_L8);
            ftp = new FTPClient();
            //ftp.configure(config);
            ftp.connect(hostName);
            ftp.login(username, password);
            ftp.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
            ftp.changeWorkingDirectory("/src/");


            return ftp.listFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static public void extract(String zipFile) throws ZipException, IOException {
        System.out.println(zipFile);
        int BUFFER = 2048;
        File file = new File(zipFile);

        ZipFile zip = new ZipFile(file);
        String newPath = zipFile.substring(0, zipFile.length() - 4);

        new File(newPath).mkdir();
        Enumeration zipFileEntries = zip.entries();

        // Process each entry
        while (zipFileEntries.hasMoreElements()) {
            // grab a zip file entry
            ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
            String currentEntry = entry.getName();
            File destFile = new File(newPath, currentEntry);
            //destFile = new File(newPath, destFile.getName());
            File destinationParent = destFile.getParentFile();

            // create the parent directory structure if needed
            destinationParent.mkdirs();

            if (!entry.isDirectory()) {
                BufferedInputStream is = new BufferedInputStream(zip
                        .getInputStream(entry));
                int currentByte;
                // establish buffer for writing file
                byte data[] = new byte[BUFFER];

                // write the current file to disk
                FileOutputStream fos = new FileOutputStream(destFile);
                BufferedOutputStream dest = new BufferedOutputStream(fos,
                        BUFFER);

                // read and write until last byte is encountered
                while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, currentByte);
                }
                dest.flush();
                dest.close();
                is.close();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new File("./upload").mkdir();
        for(FTPFile f : list()) {
            if(f.getName().contains("zip")) {
                FTP.download("/src/", ftp, new FTPFile[] {f});
                extract("./scripts/src/" + f.getName());
                Compiler pilre;
                Compiler.compile();
            }
        }

        /*for (FTPFile file : list()) {
            if (!file.getName().contains(".zip"))
                continue;

            try {
                File f = new File("./scripts/src");
                //System.out.println(f.exists());
                extract("./scripts/src/" + file.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }


}
