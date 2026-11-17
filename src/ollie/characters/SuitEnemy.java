package ollie.characters;

import city.cs.engine.PolygonShape;
import city.cs.engine.Shape;
import ollie.audio.Sound;
import ollie.bullets.BurstBullet;
import ollie.levels.GameLevel;
import ollie.ui.Sprite;
import ollie.util.Timers;

/** Businessman who fires a downward spread of eight shots every four seconds. */
public class SuitEnemy extends Enemy {

    private static final Shape SHAPE = new PolygonShape(
            2.75f, -1.03f, 0.99f, 2.95f, -1.73f, 2.71f, -3.15f, -2.0f, -0.43f, -3.29f);
    private static final Sprite LEFT = Sprite.large("suitLeft.gif");
    private static final Sprite RIGHT = Sprite.large("suitRight.gif");
    private static final Sprite HURT_LEFT = Sprite.large("suitLeftDamaged.gif");
    private static final Sprite HURT_RIGHT = Sprite.large("suitDamaged.gif");
    private static final Sprite ATTACK = Sprite.large("suitAttacck.gif");
    private static final int ATTACK_ANIMATION_MS = 300;

    public SuitEnemy(GameLevel level, int health) {
        super(level, SHAPE, health, 8, 6, 4000, Sound.SUIT_AMBIENT, Sound.SUIT_DAMAGED, Sound.SUIT_DIED);
    }

    @Override
    protected Sprite sprite(boolean right) {
        return right ? RIGHT : LEFT;
    }

    @Override
    protected Sprite hurtSprite(boolean right) {
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
