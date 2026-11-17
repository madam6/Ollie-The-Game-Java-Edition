package ollie.characters;

import city.cs.engine.PolygonShape;
import city.cs.engine.Shape;
import city.cs.engine.StepEvent;
import city.cs.engine.StepListener;
import city.cs.engine.Walker;
import city.cs.engine.World;
import ollie.audio.Sound;
import ollie.bullets.RainbowBullet;
import ollie.levels.GameLevel;
import ollie.ui.Flashable;
import ollie.ui.Lighting;
import ollie.ui.Pulse;
import ollie.ui.Sprite;
import ollie.util.Timers;
import org.jbox2d.common.Vec2;

/** Ollie. Movement comes from {@link ollie.input.PlayerController}; this class keeps the stats and sprites. */
public class Player extends Walker implements StepListener, Flashable, Lighting.LightSource {

    public static final int START_HEALTH = 500;
    private static final int START_AMMO = 5;

    private static final Shape SHAPE = new PolygonShape(
            1.74f, -3.89f, 1.55f, 3.07f, -1.12f, 2.99f, -1.95f, -1.57f, -1.81f, -3.84f);

    private static final Sprite IDLE = Sprite.large("ollieidle-export.gif");
    private static final Sprite WALK_RIGHT = Sprite.large("ollieWalkingRight-export.gif");
    private static final Sprite WALK_LEFT = Sprite.large("ollieWalkingLeft-export.gif");
    private static final Sprite JUMP_RIGHT = Sprite.large("jumpRight.png");
    private static final Sprite JUMP_LEFT = Sprite.large("jumpLeft.png");
    private static final Sprite DASH_RIGHT = Sprite.large("dashRight.png");
    private static final Sprite DASH_LEFT = Sprite.large("dashLeft.png");
    private static final Sprite FIRE_RIGHT = Sprite.large("ollieRainbow-export.gif");
    private static final Sprite FIRE_LEFT = Sprite.large("ollieRainbowL-export.gif");
    private static final Sprite HURT = Sprite.large("ollieidleDamaged.png");

    /** Horizontal speed above which Ollie is drawn dashing rather than walking. */
    private static final float DASH_THRESHOLD = 6;
    private static final int HURT_MS = 300;
    private static final int FIRE_WINDUP_MS = 800;
    private static final int FIRE_COOLDOWN_MS = 1000;
    private static final float RECOIL_SPEED = 5;
    /** Distance from the body's centre down to the soles of Ollie's shoes. */
    private static final float FEET = 3.9f;
    private static final int FLASH_MS = 100;

    private final Pulse flash = new Pulse(FLASH_MS);
    private int health = START_HEALTH;
    private int dashes;
    private int ammo = START_AMMO;
    private int score;

    private boolean dead;
    private boolean hurt;
    private boolean firing;
    private boolean canFire = true;
    private boolean facingRight = true;
    private Sprite sprite;

    public Player(World world) {
        super(world, SHAPE);
        setSprite(IDLE);
    }

    public int getHealth() {
        return health;
    }

    public int getDashes() {
        return dashes;
    }

    public int getAmmo() {
        return ammo;
    }

    public int getScore() {
        return score;
    }

    public boolean isDead() {
        return dead;
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public float flashAmount() {
        return flash.value();
    }

    public void damage(int amount) {
        health -= amount;
        flash.trigger();
        if (getWorld() instanceof GameLevel level) {
            level.getScreenShake().shake(4, 150);
        }
    }

    public void heal(int amount) {
        health += amount;
    }

    public void addDashes(int amount) {
        dashes += amount;
    }

    public void addAmmo(int amount) {
        ammo += amount;
    }

    public void addScore(int amount) {
        score += amount;
    }

    /** Health, dashes and ammo carry over between story levels; score does not. */
    public void carryOver(Player previous) {
        health = previous.health;
        dashes = previous.dashes;
        ammo = previous.ammo;
    }

    /** Briefly stuns Ollie and shows the hurt sprite. */
    public void hurt() {
        if (!hurt) {
            hurt = true;
            Timers.once(HURT_MS, () -> hurt = false);
        }
    }

    /** Spends one dash if there is one. */
    public boolean useDash() {
        if (dashes <= 0) {
            return false;
        }
        dashes--;
        return true;
    }

    /** Jumps only when standing on something, so jumping in mid-air does nothing. */
    public boolean tryJump(float speed) {
        if (Math.abs(getLinearVelocity().y) >= 0.01f) {
            return false;
        }
        jump(speed);
        return true;
    }

    /**
     * Starts the rainbow attack: a short wind-up animation, then the shot. Returns false
     * without spending ammo if Ollie is out of ammo or still cooling down.
     */
    public boolean tryFire() {
        if (dead || firing || !canFire || ammo <= 0) {
            return false;
        }
        ammo--;
        firing = true;
        canFire = false;
        Timers.once(FIRE_WINDUP_MS, () -> {
            firing = false;
            if (dead || !getWorld().isRunning()) {
                canFire = true;
                return;
            }
            shootRainbow(facingRight);
            Timers.once(FIRE_COOLDOWN_MS, () -> canFire = true);
        });
        return true;
    }

    private void shootRainbow(boolean right) {
        Vec2 position = getPosition();
        new RainbowBullet(getWorld(), new Vec2(position.x + (right ? 2 : -2), position.y + 2), right);
        setLinearVelocity(new Vec2(right ? -RECOIL_SPEED : RECOIL_SPEED, 0));
    }

    /** A shadow on the ground, and on the dark levels a pool of light around Ollie. */
    @Override
    public void addLight(Lighting lighting) {
        Vec2 position = getPosition();
        lighting.shadow(position.x, position.y - FEET, 4);
        lighting.light(position, 9, 1);
    }

    @Override
    public void preStep(StepEvent e) {
    }

    @Override
    public void postStep(StepEvent e) {
        if (dead) {
            return;
        }
        if (health <= 0) {
            dead = true;
            Sound.PLAYER_DIED.play();
            destroy();
            return;
        }
        if (firing) {
            setSprite(facingRight ? FIRE_RIGHT : FIRE_LEFT);
        } else if (hurt) {
            setSprite(HURT);
            setLinearVelocity(new Vec2(0, 0));
        } else {
            updateMovementSprite();
        }
    }

    /** Picks the sprite from the current velocity. Falling keeps whatever sprite was showing. */
    private void updateMovementSprite() {
        Vec2 v = getLinearVelocity();
        if (v.x > DASH_THRESHOLD && v.y >= 0) {
            show(DASH_RIGHT, true);
        } else if (v.x < -DASH_THRESHOLD && v.y >= 0) {
            show(DASH_LEFT, false);
        } else if (v.x < 0 && v.x > -DASH_THRESHOLD && v.y <= 0 && v.y > -0.1f) {
            show(WALK_LEFT, false);
        } else if (v.x > 0 && v.x < DASH_THRESHOLD && v.y >= 0 && v.y < 0.1f) {
            show(WALK_RIGHT, true);
        } else if (v.x == 0 && v.y >= 0) {
            setSprite(IDLE);
        } else if (v.x < 0 && v.x > -DASH_THRESHOLD && v.y > 0.1f) {
            show(JUMP_LEFT, false);
        } else if (v.x > 0 && v.x < DASH_THRESHOLD && v.y > 0) {
            show(JUMP_RIGHT, true);
        }
    }

    private void show(Sprite image, boolean right) {
        facingRight = right;
        setSprite(image);
    }

    /** Swaps the sprite only when it actually changes, instead of every frame. */
    private void setSprite(Sprite image) {
        if (image != sprite) {
            removeAllImages();
            addImage(image.bodyImage());
            sprite = image;
        }
    }
}
