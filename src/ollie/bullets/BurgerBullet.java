package ollie.bullets;

import city.cs.engine.BodyImage;
import city.cs.engine.CollisionEvent;
import city.cs.engine.PolygonShape;
import city.cs.engine.Shape;
import city.cs.engine.StepEvent;
import city.cs.engine.StepListener;
import city.cs.engine.World;
import ollie.audio.Sound;
import ollie.characters.Player;
import org.jbox2d.common.Vec2;

/** Thrown by the clown and the boss. Bounces between the edges of the screen until eaten. */
public class BurgerBullet extends Bullet implements StepListener {

    private static final Shape SHAPE = new PolygonShape(
            -0.05f, -0.5f, 1.03f, -0.03f, 1.02f, 0.72f, 0.42f, 1.18f, -0.5f, 1.15f, -1.05f, 0.71f, -1.02f, -0.08f);
    private static final BodyImage BURGER = new BodyImage("data/burgerBullet.png", 4);
    private static final BodyImage EATEN = new BodyImage("data/burgerBulletEaten.gif", 4);

    private static final float SPEED = 50;
    private static final float BOUNCE_X = 19;
    private static final int LIFETIME_MS = 3333;
    private static final int EATEN_MS = 1000;
    private static final int DAMAGE = 10;

    private boolean eaten;

    public BurgerBullet(World world, Vec2 position, boolean right) {
        super(world, SHAPE, position, BURGER);
        setLinearVelocity(new Vec2(right ? SPEED : -SPEED, -10));
        world.addStepListener(this);
        after(LIFETIME_MS, this::beEaten);
    }

    private void beEaten() {
        if (eaten) {
            return;
        }
        eaten = true;
        setSprite(EATEN);
        after(EATEN_MS, this::destroy);
    }

    @Override
    public void collide(CollisionEvent e) {
        if (!eaten && e.getOtherBody() instanceof Player player) {
            player.damage(DAMAGE);
            beEaten();
            Sound.BURGER_EATEN.play();
            Sound.PLAYER_DAMAGED.play();
        }
    }

    @Override
    public void preStep(StepEvent e) {
    }

    @Override
    public void postStep(StepEvent e) {
        float x = getPosition().x;
        if (x < -BOUNCE_X) {
            setLinearVelocity(new Vec2(SPEED, getLinearVelocity().y));
        } else if (x > BOUNCE_X) {
            setLinearVelocity(new Vec2(-SPEED, getLinearVelocity().y));
        }
    }
}
