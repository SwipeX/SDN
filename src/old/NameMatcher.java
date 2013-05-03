package old;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Tim
 * Date: 1/24/13
 * Time: 10:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class NameMatcher {
    static HashMap<String[], String> matches = new HashMap<>();

    public NameMatcher() {
        matches.put(new String[]{"agility", "obstacle", "run"}, "Agility");
        matches.put(new String[]{"kill", "fight", "attack", "combat", "eat"}, "Combat");
        matches.put(new String[]{"construction", "build", "construct", "larder"}, "Construction");
        matches.put(new String[]{"cook", "fry", "burn", "range"}, "Cooking");
        matches.put(new String[]{"craft", "needle", "thread", "hide"}, "Crafting");
        matches.put(new String[]{"dungeon", "room", "dung"}, "Dungeoneering");
        matches.put(new String[]{"farm", "plant", "livid"}, "Farming");
        matches.put(new String[]{"fire", "making", "log", "burn"}, "Firemaking");
        matches.put(new String[]{"fish", "trout", "salmon", "fisher"}, "Fishing");
        matches.put(new String[]{"fletch", }, "Fletching");
        matches.put(new String[]{"herb", }, "Herblore");
        matches.put(new String[]{"hunt", }, "Hunter");
        matches.put(new String[]{"magic", "mage", }, "Magic");
        matches.put(new String[]{"mini", "soul", "castle", "pest", "control", "pc"}, "Activities");
        matches.put(new String[]{"misc", }, "Activities");
        matches.put(new String[]{"money", "making", "gold", "gp"}, "Money");
        matches.put(new String[]{"pray", "prayer", "bone", "bury"}, "Prayer");
        matches.put(new String[]{"range", "arrow", "bow", "range guild"}, "Ranged");
        matches.put(new String[]{"runecraft", "nat", "rc", "runner"}, "Runecrafting");
        matches.put(new String[]{"summon", "pouch", "shard", "familiar"}, "Summoning");
        matches.put(new String[]{"theif", "theiv", "steal", "lock", "crack"}, "Theiving");
        matches.put(new String[]{"chop", "cut", "wood", "log"}, "Woodcutting");

    }

    public static String getType(String... args) {
        for (String[] keys : matches.keySet()) {
            for (String str : keys) {
                for (String arg : args) {
                    for (String param : arg.split(" ")) {
                        if (param.toLowerCase().startsWith(str.toLowerCase()) || param.toLowerCase().endsWith(str.toLowerCase())) {
                            return matches.get(keys);
                        }
                    }
                }
            }
        }
        return "Activities";
    }
}
