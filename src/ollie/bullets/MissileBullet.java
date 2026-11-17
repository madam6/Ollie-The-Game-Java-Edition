package ollie.bullets;

import city.cs.engine.CollisionEvent;
import city.cs.engine.PolygonShape;
import city.cs.engine.Shape;
import city.cs.engine.StaticBody;
import city.cs.engine.World;
import ollie.audio.Sound;
import ollie.characters.Player;
import ollie.items.Barrel;
import ollie.ui.Lighting;
import ollie.ui.Pulse;
import ollie.ui.Sprite;
import org.jbox2d.common.Vec2;

import java.awt.Color;

/** Fired by planes and the boss. Explodes on anything solid; a direct hit is fatal. */
public class MissileBullet extends Bullet {

    private static final Shape SHAPE = new PolygonShape(
            0.01f, -1.8f, 0.46f, -1.64f, 0.46f, 1.37f, -0.48f, 1.37f, -0.46f, -1.6f);
    private static final Sprite MISSILE = Sprite.small("missile.gif");
    private static final Sprite EXPLOSION = Sprite.large("explosion.gif");
    private static final Color FLAME = new Color(0xffa040);
    private static final Color FIRE = new Color(0xff7a20);

    private static final int LIFETIME_MS = 6666;
    private static final int EXPLOSION_MS = 1000;
    private static final int DAMAGE = 1000;

    private final Pulse explosion = new Pulse(EXPLOSION_MS);
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
        explosion.trigger();
        setSprite(EXPLOSION);
        setLinearVelocity(new Vec2(0, 0));
        setGravityScale(0);
        shakeScreen(9, 350);
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

    /** A small glow from the exhaust in flight; a big one that fades out when it explodes. */
    @Override
    public void addLight(Lighting lighting) {
        Vec2 position = getPosition();
        if (!exploded) {
            lighting.glow(position, 2, FLAME, 0.35f);
            lighting.light(position, 3, 0.7f);
            return;
        }
        float strength = explosion.value();
        lighting.glow(position, 8, FIRE, 0.6f * strength);
        lighting.light(position, 11, strength);
    }
}
