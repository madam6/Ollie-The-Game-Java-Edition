package ollie.characters;

import city.cs.engine.PolygonShape;
import city.cs.engine.Shape;
import ollie.audio.Sound;
import ollie.bullets.BurgerBullet;
import ollie.levels.GameLevel;
import ollie.ui.Sprite;

/** Clown that throws a burger every second in the direction it is flying. */
public class RonaldEnemy extends Enemy {

    private static final Shape SHAPE = new PolygonShape(-0.32f, -0.81f, 3.55f, 0.03f, -1.91f, 2.59f, -3.81f, 0.09f);
    private static final Sprite LEFT = Sprite.large("ronaldL.gif");
    private static final Sprite RIGHT = Sprite.large("ronaldR.gif");
    private static final Sprite HURT_LEFT = Sprite.large("ronaldLDamaged.gif");
    private static final Sprite HURT_RIGHT = Sprite.large("ronaldRDamaged.gif");

    public RonaldEnemy(GameLevel level, int health) {
        super(level, SHAPE, health, 5, 10, 1000, Sound.RONALD_FLYING, Sound.RONALD_DAMAGED, Sound.RONALD_DIED);
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
        new BurgerBullet(level, getPosition(), isMovingRight());
        Sound.RONALD_ATTACK.play();
    }
}
