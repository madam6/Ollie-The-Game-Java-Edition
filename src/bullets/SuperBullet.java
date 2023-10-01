package bullets;

import city.cs.engine.*;
import game.levels.GameLevel;
import gameControllers.SmallBulletController;
import org.jbox2d.common.Vec2;

import java.awt.event.ActionEvent;
import java.util.Random;
/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description Super bullet that spawns small bullets and destroys itself for player`s usage.
 */
public class SuperBullet extends Bullet{

    protected static Shape superBulletShape = new PolygonShape(0.09f,-0.23f, 0.15f,-0.18f, 0.08f,-0.16f, 0.04f,-0.2f);

    protected static BodyImage superBullet = new BodyImage("data/newAttack.png", 4);

    /**
     * Array of small bullets to be spawned.
     */
    SmallBullet[] bullets = new SmallBullet[6];
    /**
     * Array of controllers for spawned bullets.
     */
    SmallBulletController[] controllers = new SmallBulletController[6];
    /**
     * Array of positions to where bullets will fly to.
     */
    Vec2[] positionsToFire;
    /**
     * Bullet speed
     */
    protected int speed = 50;
    /**
     * Constructor for Super bullet.
     * <p>
     * Creates an array of small bullets with stated length. Adds controllers and positions to fire.
     * @param  w, start, initialDirection
     * @return void.
     */
    public SuperBullet(World w, Vec2 pos) {
        super(w, superBulletShape);
        addImage(superBullet);
        positionsToFire = generateCirclePoints(pos.x, pos.y, 20, 6, true);
        for (int i = 0; i < bullets.length; i++) {
            bullets[i] = new SmallBullet(w, SmallBullet.smallBulletShape);
            controllers[i] = new SmallBulletController(((GameLevel)w).getPlayer());
            bullets[i].addImage(superBullet);
            bullets[i].addCollisionListener(controllers[i]);
            bullets[i].setPosition(new Vec2(pos.x, pos.y+13));

        }
        for (int i = 0; i < bullets.length; i++) {
            bullets[i].fire(positionsToFire[i]);
        }
        this.destroy();

    }
    /**
     * Generates position to fire
     * <p>
     * Method about which i`m proud of. It takes current position, radius and amount with information
     * about which half of the circle bullet should spawn. And returns stated amount of positions in stated
     * half of the circle which lay on this circle.
     * @param  x,y,radius,amount,isUpperHalf
     * @return void.
     */
    public static Vec2[] generateCirclePoints(float x, float y, int radius, int amount, boolean isUpperHalf) {
        Vec2[] points = new Vec2[amount];
        Random random = new Random();
        double offset = isUpperHalf ? 0 : Math.PI;

        for (int i = 0; i < amount; i++) {
            double angle = offset + Math.PI * random.nextDouble();
            double xPos = (x + radius * Math.cos(angle)*3);
            double yPos = (y + radius * Math.sin(angle))*3;
            points[i] = new Vec2((float) xPos, (float) yPos);
        }

        return points;
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void postStep(StepEvent stepEvent) {
        reflecting2(speed);
    }
}
