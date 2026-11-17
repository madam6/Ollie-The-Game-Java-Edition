package ollie.characters;

import city.cs.engine.BodyImage;
import city.cs.engine.DynamicBody;
import city.cs.engine.Shape;
import city.cs.engine.StepEvent;
import city.cs.engine.StepListener;
import ollie.audio.Sound;
import ollie.levels.GameLevel;
import ollie.util.Timers;
import org.jbox2d.common.Vec2;

import javax.swing.Timer;
import java.util.Random;

/**
 * Shared behaviour for flying enemies: spawn at a random side, fly back and forth across
 * the screen while bobbing up and down, attack on a timer, and die when out of health.
 */
public abstract class Enemy extends DynamicBody implements StepListener {

    private static final Random RANDOM = new Random();
    private static final float SPAWN_X = 19;
    private static final float TURN_X = 14.99f;
    private static final float LOW_Y = 0;
    private static final float HIGH_Y = 10;

    protected final GameLevel level;
    protected final Player player;
    private final float speed;
    private final int scoreValue;
    private final Sound ambient;
    private final Sound hurtSound;
    private final Sound deathSound;
    private final Timer attackTimer;

    private int health;
    private boolean movingRight;
    private boolean dead;
    private boolean silenced;
    private BodyImage sprite;

    protected Enemy(GameLevel level, Shape shape, int health, int scoreValue, float speed, int attackPeriodMs,
                    Sound ambient, Sound hurtSound, Sound deathSound) {
        super(level, shape);
        this.level = level;
        this.player = level.getPlayer();
        this.health = health;
        this.scoreValue = scoreValue;
        this.speed = speed;
        this.ambient = ambient;
        this.hurtSound = hurtSound;
        this.deathSound = deathSound;

        float x = RANDOM.nextBoolean() ? -SPAWN_X : SPAWN_X;
        setPosition(new Vec2(x, RANDOM.nextFloat(3, 7)));
        setSprite(sprite(false));
        level.addStepListener(this);
        ambient.acquireLoop();
        attackTimer = Timers.repeat(attackPeriodMs, attackPeriodMs, this::attack);
    }

    /** Sprite while flying in the given direction. */
    protected abstract BodyImage sprite(boolean right);

    /** Sprite shown after taking a hit, until the enemy next turns around. */
    protected abstract BodyImage hurtSprite(boolean right);

    /** Fires this enemy's attack. Called by the attack timer. */
    protected abstract void attack();

    public int getHealth() {
        return health;
    }

    public boolean isDead() {
        return dead;
    }

    public boolean isMovingRight() {
        return movingRight;
    }

    public void takeHit(int damage) {
        if (dead) {
            return;
        }
        health -= damage;
        setSprite(hurtSprite(movingRight));
        if (health <= 0) {
            dead = true;
            silence();
            deathSound.play();
            onDeath();
        } else {
            hurtSound.play();
        }
    }

    /** Default death: award points and disappear. */
    protected void onDeath() {
        player.addScore(scoreValue);
        destroy();
    }

    /** Stops attacking and lets go of the ambient sound loop. Safe to call more than once. */
    public void silence() {
        attackTimer.stop();
        if (!silenced) {
            silenced = true;
            ambient.releaseLoop();
        }
    }

    protected void setSprite(BodyImage image) {
        if (image != sprite) {
            removeAllImages();
            addImage(image);
            sprite = image;
        }
    }

    @Override
    public void preStep(StepEvent e) {
    }

    @Override
    public void postStep(StepEvent e) {
        if (dead) {
            return;
        }
        setAngleDegrees(0);
        Vec2 position = getPosition();
        if (position.x < -TURN_X) {
            movingRight = true;
            setSprite(sprite(true));
        } else if (position.x > TURN_X) {
            movingRight = false;
            setSprite(sprite(false));
        }
        setLinearVelocity(new Vec2(movingRight ? speed : -speed, 0));

        // Flipping gravity keeps the enemy bobbing between the two heights.
        if (position.y < LOW_Y) {
            setGravityScale(-10);
        } else if (position.y > HIGH_Y) {
            setGravityScale(20);
        }
    }
}
