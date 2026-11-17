package ollie.characters;

import city.cs.engine.BodyImage;
import city.cs.engine.PolygonShape;
import city.cs.engine.Shape;
import ollie.audio.Sound;
import ollie.bullets.BurstBullet;
import ollie.levels.GameLevel;
import ollie.util.Timers;

/** Businessman who fires a downward spread of eight shots every four seconds. */
public class SuitEnemy extends Enemy {

    private static final Shape SHAPE = new PolygonShape(
            2.41f, -0.9f, 0.87f, 2.58f, -1.51f, 2.37f, -2.76f, -1.75f, -0.38f, -2.88f);
    private static final BodyImage LEFT = new BodyImage("data/suitLeft.gif", 7);
    private static final BodyImage RIGHT = new BodyImage("data/suitRight.gif", 7);
    private static final BodyImage HURT_LEFT = new BodyImage("data/suitLeftDamaged.gif", 7);
    private static final BodyImage HURT_RIGHT = new BodyImage("data/suitDamaged.gif", 7);
    private static final BodyImage ATTACK = new BodyImage("data/suitAttacck.gif", 7);
    private static final int ATTACK_ANIMATION_MS = 300;

    public SuitEnemy(GameLevel level, int health) {
        super(level, SHAPE, health, 8, 6, 4000, Sound.SUIT_AMBIENT, Sound.SUIT_DAMAGED, Sound.SUIT_DIED);
    }

    @Override
    protected BodyImage sprite(boolean right) {
        return right ? RIGHT : LEFT;
    }

    @Override
    protected BodyImage hurtSprite(boolean right) {
        return right ? HURT_RIGHT : HURT_LEFT;
    }

    @Override
    protected void attack() {
        BurstBullet.enemyBurst(level, getPosition());
        Sound.SUIT_ATTACK.play();
        setSprite(ATTACK);
        Timers.once(ATTACK_ANIMATION_MS, () -> {
            if (!isDead()) {
                setSprite(sprite(isMovingRight()));
            }
        });
    }
}
