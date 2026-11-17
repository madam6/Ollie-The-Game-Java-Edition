package ollie.bullets;

import city.cs.engine.BodyImage;
import city.cs.engine.CollisionEvent;
import city.cs.engine.PolygonShape;
import city.cs.engine.Shape;
import city.cs.engine.World;
import ollie.characters.Boss;
import ollie.characters.Enemy;
import org.jbox2d.common.Vec2;

/** Ollie's main attack. Kills a regular enemy outright and takes a sixth off the boss. */
public class RainbowBullet extends Bullet {

    private static final Shape SHAPE = new PolygonShape(
            -0.74f, -1.98f, 1.98f, 1.11f, 1.95f, 1.97f, 0.77f, 1.99f, -1.97f, -0.69f, -1.98f, -1.94f);
    private static final BodyImage RIGHT = new BodyImage("data/rainbow-export.png", 4);
    private static final BodyImage LEFT = new BodyImage("data/rainbowL-export.png", 4);

    private static final float SPEED = 40;
    private static final int LIFETIME_MS = 3000;
    private static final int ENEMY_DAMAGE = 1000;
    private static final int BOSS_DAMAGE = 100;

    public RainbowBullet(World world, Vec2 position, boolean right) {
        super(world, SHAPE, position, right ? RIGHT : LEFT);
        setLinearVelocity(new Vec2(right ? SPEED : -SPEED, SPEED));
        after(LIFETIME_MS, this::destroy);
    }

    @Override
    public void collide(CollisionEvent e) {
        if (e.getOtherBody() instanceof Enemy enemy && !enemy.isDead()) {
            enemy.takeHit(enemy instanceof Boss ? BOSS_DAMAGE : ENEMY_DAMAGE);
            destroy();
        }
    }
}
