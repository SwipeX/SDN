import org.powerbuddy.api.events.MessageEvent;
import org.powerbuddy.api.listeners.MessageListener;
import org.powerbuddy.api.methods.Calculations;
import org.powerbuddy.api.methods.Game;
import org.powerbuddy.api.methods.Players;
import org.powerbuddy.api.wrapper.Entity;
import org.powerbuddy.api.wrapper.Player;
import org.powerbuddy.api.wrapper.Tile;
import org.powerbuddy.api.listeners.Paintable;
import org.powerbuddy.gui.logging.TextLogger;
import org.powerbuddy.script.Manifest;
import org.powerbuddy.script.Script;

import java.awt.*;
import java.awt.event.*;

/**
 * Created with IntelliJ IDEA.
 * User: Jasper
 * Date: 30-4-13
 * Time: 14:18
 * To change this template use File | Settings | File Templates.
 */
@Manifest(author = "Parameter", name = "PKBuddy", description = "PKBuddies?", version = 0.2)
public class PKBuddy extends Script implements Paintable, MouseMotionListener, MouseListener, KeyListener, MessageListener {

    private Player local = null;
    private Entity interacting = null;

    @Override
    public void onStart() {
        TextLogger.log("Welcome to PKBuddy v" + ((Manifest)getClass().getAnnotations()[0]).version());

    }

    @Override
    public int loop() {
        if(Game.isLoggedIn()) {
            if(local == null) {
                local = Players.getLocal();
            } else {
                interacting = local.getInteracting();
            }
        } else {
            if(local != null) {
                local = null;
            }
        }

        return 50;
    }

    @Override
    public void onEnd() {

        TextLogger.log("Thanks for using PKBuddy, if an error occured please post on the thread ^^");
    }


    @Override
    public void paint(Graphics g) {
        if(interacting != null) {
            draw(g, Color.RED, interacting.getX(), interacting.getY());
        } else {
            for(Entity entity : Game.getLoadedEntities()) {

            }
        }
    }

    public static void draw(final Graphics g, final Color color, int x, int y) {
        final Point pn = Calculations.tileToScreen(new Tile(x, y), 0, 0, 0);
        final Point px = Calculations.tileToScreen(new Tile(x + 1, y), 0, 0, 0);
        final Point py = Calculations.tileToScreen(new Tile(x, y + 1), 0, 0, 0);
        final Point pxy = Calculations.tileToScreen(new Tile(x + 1, y + 1), 0, 0, 0);
        g.setColor(color);
        g.drawPolygon(new int[] { py.x, pxy.x, px.x, pn.x }, new int[] { py.y, pxy.y, px.y, pn.y }, 4);
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 100));
        g.fillPolygon(new int[] { py.x, pxy.x, px.x, pn.x }, new int[] { py.y, pxy.y, px.y, pn.y }, 4);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        System.out.println("Mouse d");
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        System.out.println("Mouse m");
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Mouse c");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("Mouse p");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("Mouse r");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        System.out.println("Mouse e");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        System.out.println("Mouse x");
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("Key t");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Key p");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("Key r");
    }

    @Override
    public void onMessage(MessageEvent event) {
        System.out.println("Message");
    }
}
