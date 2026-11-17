package ollie.items;

import city.cs.engine.BoxShape;
import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import city.cs.engine.Shape;
import city.cs.engine.StaticBody;
import city.cs.engine.World;
import ollie.audio.Sound;
import ollie.bullets.BurstBullet;
import ollie.characters.Player;
import ollie.ui.Lighting;
import ollie.ui.Sprite;
import ollie.util.Timers;
import org.jbox2d.common.Vec2;

import javax.swing.Timer;
import java.awt.Color;
import java.util.Random;

/** A token that appears at a random spot on the ground and vanishes after ten seconds. */
public class Pickup extends StaticBody implements CollisionListener, Lighting.LightSource {

    public enum Kind {
        DASH("dashToken.gif", Sound.DASH_PICKUP, 0x7fbf4a),
        HEALTH("healthToken.gif", Sound.HEAL_PICKUP, 0xff5050),
        RAINBOW("RainbowToken.gif", Sound.RAINBOW_PICKUP, 0xff3cc0),
        SUPER_JUMP("superJumpToken.gif", Sound.SUPER_JUMP_PICKUP, 0xff9a3c),
        MASS_ATTACK("newAttackToken.gif", Sound.MASS_ATTACK_PICKUP, 0x6f86ff);

        private final Sprite sprite;
        private final Sound sound;
        private final Color glow;

        Kind(String file, Sound sound, int glow) {
            this.sprite = Sprite.small(file);
            this.sound = sound;
            this.glow = new Color(glow);
        }
    }

    private static final float HALF_SIZE = 1.92f;
    private static final Shape SHAPE = new BoxShape(HALF_SIZE, HALF_SIZE);
    private static final Random RANDOM = new Random();
    private static final int LIFETIME_MS = 10_000;

    private final Kind kind;
    private final Timer expiry;
    private boolean destroyed;

    public Pickup(World world, Kind kind) {
        super(world, SHAPE);
        this.kind = kind;
        addImage(kind.sprite.bodyImage());
        setPosition(new Vec2(RANDOM.nextInt(-16, 16), -13));
        addCollisionListener(this);
        expiry = Timers.once(LIFETIME_MS, this::destroy);
    }

    @Override
    public void collide(CollisionEvent e) {
        if (!(e.getOtherBody() instanceof Player player) || player.isDead()) {
            return;
        }
        Vec2 position = getPosition();
        destroy();
        kind.sound.play();
        switch (kind) {
            case DASH -> player.addDashes(5);
            case HEALTH -> player.heal(150);
            case RAINBOW -> player.addAmmo(2);
            case SUPER_JUMP -> player.jump(20);
            case MASS_ATTACK -> BurstBullet.playerBurst(getWorld(), position);
        }
    }

    /** A glow in the token's colour and a shadow on the ground, so it reads as hovering. */
    @Override
    public void addLight(Lighting lighting) {
        Vec2 position = getPosition();
        lighting.shadow(position.x, position.y - HALF_SIZE, 3);
        lighting.glow(position, 3.5f, kind.glow, 0.3f);
        lighting.light(position, 4, 0.8f);
    }

    /** Safe to call twice: the pickup can be collected and then have its expiry timer fire. */
    @Override
    public void destroy() {
        if (destroyed) {
            return;
        }
        destroyed = true;
        expiry.stop();
        super.destroy();
    }
}
