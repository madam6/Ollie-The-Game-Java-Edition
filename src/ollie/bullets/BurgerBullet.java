package ollie.bullets;

import city.cs.engine.CollisionEvent;
import city.cs.engine.PolygonShape;
import city.cs.engine.Shape;
import city.cs.engine.StepEvent;
import city.cs.engine.StepListener;
import city.cs.engine.World;
import ollie.audio.Sound;
import ollie.characters.Player;
import ollie.ui.Lighting;
import ollie.ui.Sprite;
import org.jbox2d.common.Vec2;

/** Thrown by the clown and the boss. Bounces between the edges of the screen until eaten. */
public class BurgerBullet extends Bullet implements StepListener {

    private static final Shape SHAPE = new PolygonShape(
            -0.06f, -0.6f, 1.24f, -0.04f, 1.22f, 0.86f, 0.5f, 1.42f, -0.6f, 1.38f, -1.26f, 0.85f, -1.22f, -0.1f);
    private static final Sprite BURGER = Sprite.small("burgerBullet.png");
    private static final Sprite EATEN = Sprite.small("burgerBulletEaten.gif");

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

    /** Burgers don't glow, but they need a little light to be seen on the dark levels. */
    @Override
    public void addLight(Lighting lighting) {
        lighting.light(getPosition(), 3, 0.7f);
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
