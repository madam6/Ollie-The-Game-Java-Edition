package bullets;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description Class for player`s rainbow bullet.
 */

public class RainbowBullet extends Bullet implements ActionListener {

    protected static Shape rainbowBulletShape = new PolygonShape(-0.74f,-1.98f, 1.98f,1.11f, 1.95f,1.97f, 0.77f,1.99f, -1.97f,-0.69f, -1.98f,-1.94f);

    /**
     * Right side of the bullet.
     */
    protected static BodyImage rainbowBulletR = new BodyImage("data/rainbow-export.png", 4);
    /**
     * Left side of the bullet.
     */
    protected static BodyImage rainbowBulletL = new BodyImage("data/rainbowL-export.png", 4);
    protected Timer destroyTimer = new Timer(3000, this);
    /**
     * Initial direction of this bullet. false - left. true - right.
     */
    protected boolean initialDirection;
    /**
     * Speed of the bullet.
     */
    protected int speed = 40;

    /**
     * Constructor for Rainbow bullet
     * <p>
     * Creates a new rainbow bullet and fires it in the same direction as the one parent is moving to.
     * @param  w,start,initialDirection
     * @return void.
     */
    public RainbowBullet(World w, Vec2 start, boolean initialDirection) {
        super(w, rainbowBulletShape, start, rainbowBulletR, rainbowBulletL, initialDirection);
        this.initialDirection = initialDirection;
        if(this.initialDirection)
        {
            addImage(rainbowBulletR);
            super.fire(new Vec2(speed, speed));
        }
        else {
            addImage(rainbowBulletL);
            super.fire(new Vec2(-speed, speed));
        }
        destroyTimer.start();
    }

    @Override
    public void postStep(StepEvent stepEvent) {
        /*super.reflecting(initialDirection);*/
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.destroy();
    }
}
