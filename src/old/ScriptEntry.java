package old;

/**
 * Created with IntelliJ IDEA.
 * User: Tim
 * Date: 1/22/13
 * Time: 10:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScriptEntry {

    public String name;
    public String desc;
    public double version;
    public int id;
    public String author;
    public String thread;
    public String original;

    public ScriptEntry(String or, String name, String desc, String author, double version, String thread, int id) {
        original = or;
        this.name = name;
        this.desc = desc;
        this.version = version;
        this.id = id;
        this.author = author;
        this.thread = thread;
    }
}
