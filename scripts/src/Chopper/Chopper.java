import org.powerbuddy.api.input.Mouse;
import org.powerbuddy.api.methods.Camera;
import org.powerbuddy.api.methods.GameObjects;
import org.powerbuddy.api.methods.Players;
import org.powerbuddy.api.util.Time;
import org.powerbuddy.api.wrapper.GameObject;
import org.powerbuddy.api.listeners.Paintable;
import org.powerbuddy.script.Manifest;
import org.powerbuddy.script.Script;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Tim
 * Date: 4/13/13
 * Time: 12:21 AM
 * To change this template use File | Settings | File Templates.
 */
@Manifest(author = "Swipe", name = "Chopper", description = "Chops", version = 1.0)
public class Chopper extends Script implements Paintable {
    final int TREE = 1276;

    @Override
    public void paint(Graphics g) {
        g.drawString("Chopper By: Swipe", 100, 100);
        g.drawRect(Mouse.getX(), Mouse.getY(), 2, 2);
    }

    @Override
    public void onStart() {

    }

    @Override
    public int loop() {
        if (Players.getLocal().getAnimation() != -1)
            sleep(500);
        //if at trees
        GameObject tree = GameObjects.getNearest(TREE);
        if (tree == null)
            return 10;
        if (tree != null & !tree.isOnScreen())
            Camera.turnTo(tree);
        if (tree.isOnScreen()) {
            tree.interact("examine");
            Time.sleep(3500);
        }
        return 10;
    }

    @Override
    public void onEnd() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
