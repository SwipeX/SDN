package old;

import old.Compiler;
import old.NameMatcher;
import old.ScriptEntry;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: Tim
 * Date: 12/29/12
 * Time: 12:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class Sync {
    public static final String USER_AGENT = "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/6.0)";

    public static String CURRENT_USER_AGENT = USER_AGENT;

    public static String useragent(final String program) {
        return USER_AGENT + " " + program + "/1.0";
    }

    static String[] scripters = new String[]{
            "Drizzy,drizzy@powerbuddy.org,ymM!ApDc)nHt",
            "Nations Collapse,nationssw@powerbuddy.org,7I!Ft4rb6BK;",
            "VelvetRevolver,vrevolver@powerbuddy.org,36K&-@eK}6I;",
            "Defeat3d,dft3d@powerbuddy.org,R,Dm?pOmtvU3",
            "EpicPins,epicps@powerbuddy.org,[t}[*EaMqgs{",
            "Falixus,falixus@powerbuddy.org,6{pO!xlv3#}^",
            "Tekno,tekno@powerbuddy.org,qvJesdwUhSr;",
            "not a girl,rawr@powerbuddy.org,(BysPU=8P#P8",
            "bartos,bartos@powerbuddy.org,+u?T=nvSI)=i",
            "Policy,policy@powerbuddy.org,aa.Cm0f3k6qk",

    };

    public static void sync() {
        for (String str : scripters) {
            String[] a = str.split(",");
            new FTP().update(a[1], a[2]);
            System.out.println("synced " + a[0] + "'s scripts");
        }
    }

    /**
     * Created with IntelliJ IDEA.
     * User: Tim
     * Date: 12/20/12
     * Time: 11:43 AM
     * To change this template use File | Settings | File Templates.
     */
    public static class FTP {

        public void update(String user, String pass) {
            String hostName = "192.210.232.20";
            String username = user;
            String password = pass;


            FTPClient ftp = null;

            try {
                ftp = new FTPClient();
                ftp.connect(hostName);
                ftp.login(username, password);
                ftp.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
                ftp.changeWorkingDirectory("/");
                for (FTPFile ftpFile : ftp.listFiles()) {
                    System.out.println(ftpFile.getName());
                    if (ftpFile.isFile() && !ftpFile.getName().startsWith(".")) {
                        OutputStream output;
                        output = new FileOutputStream("./scripts/" + ftpFile.getName());
                        ftp.retrieveFile("/" + ftpFile.getName(), output);
                        System.out.println("Syncing: " + ftpFile.getName());
                    } else if (ftpFile.isDirectory() && !ftpFile.getName().startsWith(".")) {
                        FTPFile[] children = ftp.mlistDir("/" + ftpFile.getName() + "/");
                        download(ftpFile.getName() + "/", ftp, children);
                    }
                }
                ftp.disconnect();
            } catch (Exception e) {

            }


        }

        public void download(String dir, FTPClient ftp, FTPFile[] children) {
            try {
                for (FTPFile ftpFile : children) {
                    if (ftpFile.isFile()) {
                        OutputStream output;
                        File f = new File("./scripts/" + dir);
                        if (!f.exists()) {
                            f.mkdirs();
                        }
                        output = new FileOutputStream("./scripts/" + dir + ftpFile.getName());
                        ftp.retrieveFile("/in/" + dir + ftpFile.getName(), output);
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

        public void up(String dir, FTPClient ftp, File[] children) {
            try {
                for (File file : children) {
                    if (file.isFile() && file.getName().endsWith(".class")) {
                        ftp.storeFile("/out/" + dir + file.getName(), new FileInputStream(file));
                        System.out.println("Syncing: " + dir + file.getName());
                    } else if (file.isDirectory() && !file.getName().startsWith(".")) {
                        dir += "/" + file.getName() + "/";
                        ftp.makeDirectory(dir);
                        System.out.println("Syncing dir: " + dir);
                        up(dir, ftp, file.listFiles());
                    }
                }
            } catch (Exception e) {

            }
        }

        public static boolean hasScript(String name) {
            String hostName = "192.210.232.20";
            String username = "static@powerbuddy.org";
            String password = "staticgonewild!";

            FTPClient ftp = null;
            InputStream in = null;
            try {
                ftp = new FTPClient();
                ftp.connect(hostName);
                ftp.login(username, password);
                ftp.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
                FTPFile[] files = ftp.listFiles("/scripts/jars/");
                for (FTPFile ftpf : files) {
                    if (ftpf.getName().toLowerCase().split(".jar")[0].equals(name.toLowerCase())) {
                        return true;
                    }
                }
            } catch (Exception e) {

            }
            return false;
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
                System.out.println(url + builder.toString());
                final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestProperty("User-Agent", CURRENT_USER_AGENT != null ? CURRENT_USER_AGENT : USER_AGENT);
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

        public static String readLine(String url) {

            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestProperty("User-Agent", CURRENT_USER_AGENT != null ? CURRENT_USER_AGENT : USER_AGENT);
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

        public void upload() {
            new NameMatcher();
            String hostName = "192.210.232.20";
            String username = "static@powerbuddy.org";
            String password = "staticgonewild!";


            FTPClient ftp = null;

            InputStream in = null;
            try {
                for (ScriptEntry se : old.Compiler.entries) {
                    if (se.id == -1) {
                        if (!FTP.hasScript(se.name)) {
                            se.id = Integer.parseInt(readLine("http://www.powerbuddy.org/scripts/next.php"));
                            String type = NameMatcher.getType(se.name, se.desc);
                            String aa = post("http://www.powerbuddy.org/scripts/create.php", "author=" + se.author, "desc=" + se.desc, "name=" + se.name, "thread=google.com", "type=1", "skilltype=" + type, "id=" + se.id);
                            System.out.println(aa);
                        } else {
                            System.out.println("Has " + se.name);
                        }
                    }
                }
                ftp = new FTPClient();
                ftp.connect(hostName);
                ftp.login(username, password);
                ftp.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
                //  ftp.changeWorkingDirectory("/scripts/jars/");
                File f1 = new File("./upload/");
                for (File file : f1.listFiles()) {
                    if (file.isFile() && file.getName().endsWith(".jar")) {
                        System.out.println("Uploaded " + getName(file.getName()));
                        ftp.storeFile("/jars/" + getName(file.getName()), new FileInputStream(file));
                    }
                }

                ftp.logout();
                ftp.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getName(String file) {
        for (ScriptEntry se : Compiler.entries) {
            if (se.original.equals(file))
                return se.name + ".jar";
        }
        return "BADDDD";
    }
}
