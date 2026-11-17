package ollie.items;

import city.cs.engine.BodyImage;
import city.cs.engine.BoxShape;
import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import city.cs.engine.Shape;
import city.cs.engine.StaticBody;
import city.cs.engine.World;
import ollie.audio.Sound;
import ollie.bullets.BurstBullet;
import ollie.characters.Player;
import ollie.util.Timers;
import org.jbox2d.common.Vec2;

import javax.swing.Timer;
import java.util.Random;

/** A token that appears at a random spot on the ground and vanishes after ten seconds. */
public class Pickup extends StaticBody implements CollisionListener {

    public enum Kind {
        DASH("dashToken.gif", Sound.DASH_PICKUP),
        HEALTH("healthToken.gif", Sound.HEAL_PICKUP),
        RAINBOW("RainbowToken.gif", Sound.RAINBOW_PICKUP),
        SUPER_JUMP("superJumpToken.gif", Sound.SUPER_JUMP_PICKUP),
        MASS_ATTACK("newAttackToken.gif", Sound.MASS_ATTACK_PICKUP);

        private final BodyImage sprite;
        private final Sound sound;

        Kind(String file, Sound sound) {
            this.sprite = new BodyImage("data/" + file, 5);
            this.sound = sound;
        }
    }

    private static final Shape SHAPE = new BoxShape(2, 2);
    private static final Random RANDOM = new Random();
    private static final int LIFETIME_MS = 10_000;

    private final Kind kind;
    private final Timer expiry;
    private boolean destroyed;

    public Pickup(World world, Kind kind) {
        super(world, SHAPE);
        this.kind = kind;
        addImage(kind.sprite);
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
