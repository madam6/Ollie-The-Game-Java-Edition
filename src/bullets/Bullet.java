package bullets;
import city.cs.engine.*;
import city.cs.engine.Shape;
import org.jbox2d.common.Vec2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description Inheritance class for creating further bullets - contains basic bullet informatiom like animations
 * speed, directions etc. Also contains some behaviour methods like reflecting.
 */
public class Bullet extends DynamicBody implements StepListener, ActionListener {
    /**
     * Left animation for the bullet
     */
    BodyImage leftAnim;
    /**
     * Right animation for the bullet
     */
    BodyImage rightAnim;

    /**
     * Variable for detecting moving directions.
     */
    private double around=0.01;
    /**
     * Variable for detecting whether bullet is moving to right. -1 - left, 0 - waiting, 1 - right
     */
    private int movingToRight;
    /**
     * Bond for reflecting.
     */
    private float bond = 15f;
    /**
     * Initial direction for the bullet. True - right, false - left.
     */
    boolean initialDirection;

    public Bullet(World w,Shape s, Vec2 start, BodyImage leftAnim, BodyImage rightAnim, boolean initialDirection) {
        super(w, s);
        this.leftAnim = leftAnim;
        this.rightAnim = rightAnim;
        this.initialDirection = initialDirection;
        setPosition(start);
    }

    public Bullet(World w,Shape s, Vec2 start) {
        super(w, s);
        setPosition(start);
    }

    public Bullet(World w, Shape s) {
        super(w, s);

    }
    /**
     * Fire bullet.
     * <p>
     * Method sets linear velocity in a given direction - "fires a bullet"
     *
     * @param  powerDir
     * @return void.
     */
    public void fire(Vec2 powerDir)
    {
        setLinearVelocity(powerDir);
    }
    /**
     * Set angle for the picture of bullet
     * <p>
     * Sets angle for the picture of bullet using setAngleDegrees() method
     *
     * @param  degrees
     * @return void.
     */
    public void setAngle(float degrees)
    {
        this.setAngleDegrees(degrees);
    }

    /**
     * Method for reflecting bullets from side walls.
     * <p>
     * Method takes current speed and analyses current position,
     * check whether it is bigger then side walls, if so
     * sets opposite values to speed
     * @param  speed value of current speed.
     * @return void.
     */
    public void reflecting2(float speed)
    {
        if(this.getPosition().x<-19)
        {
            this.setLinearVelocity(new Vec2(speed, this.getLinearVelocity().y));
        }
        if(this.getPosition().x>19)
        {
            this.setLinearVelocity(new Vec2(-speed, this.getLinearVelocity().y));
        }
    }
    /**
     * Changes animation for the bullet
     * <p>
     * Changes animation removing previous and adding new.
     *
     * @param  bi = new Animation
     * @return void.
     */
    public void changeAnim(BodyImage bi)
    {
        this.removeAllImages();
        addImage(bi);
    }

    @Override
    public void preStep(StepEvent stepEvent) {

    }

    @Override
    public void postStep(StepEvent stepEvent) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
