package bullets;

import city.cs.engine.*;
import gameCharacters.Boss;
import gameCharacters.Enemy;
import gameCharacters.RonaldEnemy;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.awt.event.ActionEvent;
/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description First bullet(appears on second level in original game, burgerBullet for RonaldEnemy.
 */

public class burgerBullet extends Bullet{

    protected static Shape burgerBulletShape = new PolygonShape(-0.05f,-0.5f, 1.03f,-0.03f, 1.02f,0.72f, 0.42f,1.18f, -0.5f,1.15f, -1.05f,0.71f, -1.02f,-0.08f);

    protected static BodyImage burgerBullet = new BodyImage("data/burgerBullet.png", 4);
    protected static BodyImage eaten = new BodyImage("data/burgerBulletEaten.gif", 4);

    /**
     * Timer to destroy this object if it exists for too long.
     */
    protected Timer destroyTimer = new Timer(3333, this);
    /**
     * Timer to play animation before destroying.
     */
    protected Timer eatTimer = new Timer(1000, this);

    protected int speed = 50;

    /**
     * Constructor for burgerBullet
     * <p>
     * Creates a new burger bullet and fires it in the same direction as the one parent is moving to.
     *
     * @param  w,pos,parent
     * @return void.
     */
    public burgerBullet(World w, Vec2 pos, Enemy parent) {
        super(w, burgerBulletShape);
        addImage(burgerBullet);
        setPosition(pos);
        if(parent instanceof Boss)
        {
            fire(new Vec2(speed, -10));
            fire(new Vec2(-speed, -10));
        }
        else {
            if (((RonaldEnemy) parent).initialDirection) {
                fire(new Vec2(speed, -10));
            } else {
                fire(new Vec2(-speed, -10));
            }
        }

        destroyTimer.start();
    }
    /**
     * Sets animation to be eaten.
     * <p>
     * Sets an eaten animation(for destroying) using superclass method changeAnim()
     * Also starts a timer that will destroy this object.
     * @return void.
     */
    public void beEaten()
    {
        changeAnim(eaten);
        eatTimer.start();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==destroyTimer)
        {
            beEaten();
        }
        if(e.getSource()==eatTimer)
        {
            this.setClipped(false);
            destroy();
        }
    }

    public void postStep(StepEvent stepEvent) {
        reflecting2(speed);
    }
}
