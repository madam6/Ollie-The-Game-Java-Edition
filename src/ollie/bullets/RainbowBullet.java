package ollie.bullets;

import city.cs.engine.CollisionEvent;
import city.cs.engine.PolygonShape;
import city.cs.engine.Shape;
import city.cs.engine.World;
import ollie.characters.Boss;
import ollie.characters.Enemy;
import ollie.ui.Lighting;
import ollie.ui.Sprite;
import org.jbox2d.common.Vec2;

import java.awt.Color;

/** Ollie's main attack. Kills a regular enemy outright and takes a sixth off the boss. */
public class RainbowBullet extends Bullet {

    private static final Shape SHAPE = new PolygonShape(
            -0.89f, -2.38f, 2.38f, 1.33f, 2.34f, 2.36f, 0.92f, 2.39f, -2.36f, -0.83f, -2.38f, -2.33f);
    private static final Sprite RIGHT = Sprite.small("rainbow-export.png");
    private static final Sprite LEFT = Sprite.small("rainbowL-export.png");

    private static final float SPEED = 40;
    private static final int LIFETIME_MS = 3000;
    private static final int ENEMY_DAMAGE = 1000;
    private static final int BOSS_DAMAGE = 100;
    /** How long the glow takes to cycle through the colours of the rainbow. */
    private static final long HUE_CYCLE_MS = 600;

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

    @Override
    public void addLight(Lighting lighting) {
        float hue = (System.currentTimeMillis() % HUE_CYCLE_MS) / (float) HUE_CYCLE_MS;
        lighting.glow(getPosition(), 5, Color.getHSBColor(hue, 0.55f, 1), 0.5f);
        lighting.light(getPosition(), 7, 1);
    }
}
