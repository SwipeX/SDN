import java.io.File;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Tim
 * Date: 5/3/13
 * Time: 12:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class SDN
{
   static ArrayList<CompilableScript> scripts = new ArrayList<>();


    public static void main(String...args){
        //clean
        purge();
        //obtain latest scripts
        Sync.sync();
        for(File f: new File("./scripts").listFiles()){
            //if file is script
            scripts.add(new CompilableScript(f));
        }
        //process
        for(CompilableScript compilableScript: scripts){
            compilableScript.compile();
            compilableScript.jar();
        }
        //upload

    }

    public static void purge() {
        File[] files = new File[]{new File("./scripts"), new File("./bin"), new File("./upload")};
        for (File f : files) {
            if (f.isDirectory()) {
                for (File file : f.listFiles())
                    file.delete();
            }
        }
    }
}
