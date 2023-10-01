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
 * @description Second bullet(appears on first level in original game) missile whose parent is PlaneEnemy.
 */

public class missileBullet extends Bullet{

    protected static Shape missleBulletShape = new PolygonShape(0.01f,-1.5f, 0.38f,-1.37f, 0.38f,1.14f, -0.4f,1.14f, -0.38f,-1.33f);

    protected static BodyImage missileBullet = new BodyImage("data/missile.gif", 4);
    protected static BodyImage explosion = new BodyImage("data/explosion.gif", 6);

    /**
     * Timer to destroy this object if it exists for too long.
     */
    protected Timer destroyTimer = new Timer(6666, this);
    /**
     * Timer to play animation before destroying.
     */
    protected Timer explodeTimer = new Timer(1000, this);


    public missileBullet(World w, Vec2 start, Vec2 playerPos, float degrees) {
        super(w, missleBulletShape);
        addImage(missileBullet);
        setPosition(start);
        super.fire(playerPos);
        this.setBullet(true);
        destroyTimer.start();
    }
    /**
     * Sets explosion animation.
     * <p>
     * Sets the destroy animation(for destroying) using superclass method changeAnim()
     * Also starts a timer that will destroy this object.
     * @return void.
     */
    public void explode()
    {
        changeAnim(explosion);
        explodeTimer.start();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==destroyTimer)
        {
            explode();
        }
        if(e.getSource()==explodeTimer)
        {
            this.setClipped(false);
            destroy();
        }
    }
}
