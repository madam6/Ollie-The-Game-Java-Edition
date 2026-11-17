package ollie.bullets;

import city.cs.engine.BodyImage;
import city.cs.engine.CollisionEvent;
import city.cs.engine.PolygonShape;
import city.cs.engine.Shape;
import city.cs.engine.StaticBody;
import city.cs.engine.World;
import ollie.audio.Sound;
import ollie.characters.Player;
import ollie.items.Barrel;
import org.jbox2d.common.Vec2;

/** Fired by planes and the boss. Explodes on anything solid; a direct hit is fatal. */
public class MissileBullet extends Bullet {

    private static final Shape SHAPE = new PolygonShape(
            0.01f, -1.5f, 0.38f, -1.37f, 0.38f, 1.14f, -0.4f, 1.14f, -0.38f, -1.33f);
    private static final BodyImage MISSILE = new BodyImage("data/missile.gif", 4);
    private static final BodyImage EXPLOSION = new BodyImage("data/explosion.gif", 6);

    private static final int LIFETIME_MS = 6666;
    private static final int EXPLOSION_MS = 1000;
    private static final int DAMAGE = 1000;

    private boolean exploded;

    public MissileBullet(World world, Vec2 position, Vec2 velocity) {
        super(world, SHAPE, position, MISSILE);
        setLinearVelocity(velocity);
        setBullet(true);
        after(LIFETIME_MS, this::explode);
    }

    private void explode() {
        if (exploded) {
            return;
        }
        exploded = true;
        setSprite(EXPLOSION);
        setLinearVelocity(new Vec2(0, 0));
        setGravityScale(0);
        after(EXPLOSION_MS, this::destroy);
    }

    @Override
    public void collide(CollisionEvent e) {
        if (exploded) {
            return;
        }
        if (e.getOtherBody() instanceof Player player) {
            player.damage(DAMAGE);
            player.hurt();
            Sound.PLAYER_DAMAGED.play();
        } else if (!(e.getOtherBody() instanceof StaticBody) && !(e.getOtherBody() instanceof Barrel)) {
            return;
        }
        explode();
        Sound.MISSILE_EXPLOSION.play();
    }
}
