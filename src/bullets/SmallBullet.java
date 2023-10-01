package bullets;

import city.cs.engine.BodyImage;
import city.cs.engine.PolygonShape;
import city.cs.engine.Shape;
import city.cs.engine.World;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.awt.event.ActionEvent;
/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description Small bullet for player`s usage. Part of a big bullet(A collection of small bullets)
 */
public class SmallBullet extends Bullet{

    protected static Shape smallBulletShape = new PolygonShape(-1.71f,-1.41f, 1.64f,-0.1f, -1.85f,1.03f);

    protected static BodyImage smallBullet = new BodyImage("data/newAttack.png", 4);
    /**
     * Timer to destroy bullet after a period of time.
     */
    Timer destroyTimer = new Timer(6000, this);
    /**
     * Bullet speed
     */
    protected int speed = 50;
    public SmallBullet(World w, Shape s, Vec2 start, BodyImage leftAnim, BodyImage rightAnim, boolean initialDirection) {
        super(w, s, start, leftAnim, rightAnim, initialDirection);
        destroyTimer.setRepeats(false);
        destroyTimer.start();
    }

    public SmallBullet(World w, Shape s, Vec2 start) {
        super(w, s, start);
    }

    public SmallBullet(World w, Shape s) {
        super(w, s);
    }
    public void actionPerformed(ActionEvent e) {
        destroy();
    }
}
