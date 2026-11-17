package ollie.characters;

import city.cs.engine.BodyImage;
import city.cs.engine.PolygonShape;
import city.cs.engine.Shape;
import ollie.audio.Sound;
import ollie.bullets.BurgerBullet;
import ollie.levels.GameLevel;

/** Clown that throws a burger every second in the direction it is flying. */
public class RonaldEnemy extends Enemy {

    private static final Shape SHAPE = new PolygonShape(-0.28f, -0.71f, 3.11f, 0.03f, -1.67f, 2.27f, -3.33f, 0.08f);
    private static final BodyImage LEFT = new BodyImage("data/ronaldL.gif", 7);
    private static final BodyImage RIGHT = new BodyImage("data/ronaldR.gif", 7);
    private static final BodyImage HURT_LEFT = new BodyImage("data/ronaldLDamaged.gif", 7);
    private static final BodyImage HURT_RIGHT = new BodyImage("data/ronaldRDamaged.gif", 7);

    public RonaldEnemy(GameLevel level, int health) {
        super(level, SHAPE, health, 5, 10, 1000, Sound.RONALD_FLYING, Sound.RONALD_DAMAGED, Sound.RONALD_DIED);
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
        new BurgerBullet(level, getPosition(), isMovingRight());
        Sound.RONALD_ATTACK.play();
    }
}
