package ollie.bullets;

import city.cs.engine.Body;
import city.cs.engine.CollisionEvent;
import city.cs.engine.PolygonShape;
import city.cs.engine.Shape;
import city.cs.engine.StaticBody;
import city.cs.engine.World;
import ollie.audio.Sound;
import ollie.characters.Boss;
import ollie.characters.Enemy;
import ollie.characters.Player;
import ollie.ui.Lighting;
import ollie.ui.Sprite;
import org.jbox2d.common.Vec2;

import java.awt.Color;
import java.util.Random;

/**
 * One shot of a spread attack. The player gets an upward spread from the mass-attack
 * pickup; suits and the boss fire a downward one.
 */
public class BurstBullet extends Bullet {

    private static final Shape SHAPE = new PolygonShape(-1.71f, -1.41f, 1.64f, -0.1f, -1.85f, 1.03f);
    /** The shots are drawn on a 16-pixel canvas. */
    private static final Sprite PLAYER_SHOT = new Sprite("newAttack.png", 16, Sprite.LARGE);
    private static final Sprite ENEMY_SHOT = new Sprite("newAttackEnemy.png", 16, Sprite.LARGE);
    private static final Color PLAYER_GLOW = new Color(0x6fb8ff);
    private static final Color ENEMY_GLOW = new Color(0xff3030);
    private static final Random RANDOM = new Random();

    private static final int RADIUS = 20;
    private static final int LIFETIME_MS = 6000;
    private static final int ENEMY_DAMAGE = 8;
    private static final int BOSS_DAMAGE = 50;
    private static final int PLAYER_DAMAGE = 10;

    private final boolean fromPlayer;

    private BurstBullet(World world, Vec2 position, Vec2 velocity, boolean fromPlayer) {
        super(world, SHAPE, position, fromPlayer ? PLAYER_SHOT : ENEMY_SHOT);
        this.fromPlayer = fromPlayer;
        setLinearVelocity(velocity);
        after(LIFETIME_MS, this::destroy);
    }

    /** Six shots fanned out upwards from a mass-attack pickup. */
    public static void playerBurst(World world, Vec2 origin) {
        Vec2 start = new Vec2(origin.x, origin.y + 13);
        for (Vec2 velocity : fanOut(origin, 6, true, 3)) {
            new BurstBullet(world, start, velocity, true);
        }
    }

    /** Eight shots fanned out downwards from an enemy. */
    public static void enemyBurst(World world, Vec2 origin) {
        Vec2 start = new Vec2(origin.x, origin.y - 1);
        for (Vec2 velocity : fanOut(origin, 8, false, 2)) {
            new BurstBullet(world, start, velocity, false);
        }
    }

    /**
     * Picks {@code count} random points on the upper or lower half of a circle around
     * {@code origin} and stretches them by {@code spread}. The points are used as launch
     * velocities, so shots fired from higher up travel faster.
     */
    static Vec2[] fanOut(Vec2 origin, int count, boolean upperHalf, float spread) {
        Vec2[] points = new Vec2[count];
        double offset = upperHalf ? 0 : Math.PI;
        for (int i = 0; i < count; i++) {
            double angle = offset + Math.PI * RANDOM.nextDouble();
            double x = origin.x + RADIUS * Math.cos(angle) * spread;
            double y = (origin.y + RADIUS * Math.sin(angle)) * spread;
            points[i] = new Vec2((float) x, (float) y);
        }
        return points;
    }

    @Override
    public void collide(CollisionEvent e) {
        Body other = e.getOtherBody();
        if (fromPlayer && other instanceof Enemy enemy) {
            if (!enemy.isDead()) {
                Sound.BURST_HIT.play();
                enemy.takeHit(enemy instanceof Boss ? BOSS_DAMAGE : ENEMY_DAMAGE);
            }
            destroy();
        } else if (!fromPlayer && other instanceof Player player) {
            player.damage(PLAYER_DAMAGE);
            player.hurt();
            Sound.PLAYER_DAMAGED.play();
            Sound.BURST_HIT.play();
            destroy();
        } else if (other instanceof StaticBody) {
            Sound.BURST_HIT.play();
            destroy();
        }
    }

    @Override
    public void addLight(Lighting lighting) {
        lighting.glow(getPosition(), 2.5f, fromPlayer ? PLAYER_GLOW : ENEMY_GLOW, 0.3f);
        lighting.light(getPosition(), 4, 0.8f);
    }
}
