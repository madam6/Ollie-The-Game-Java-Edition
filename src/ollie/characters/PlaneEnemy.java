package ollie.characters;

import city.cs.engine.PolygonShape;
import city.cs.engine.Shape;
import ollie.audio.Sound;
import ollie.bullets.MissileBullet;
import ollie.levels.GameLevel;
import ollie.ui.Sprite;
import org.jbox2d.common.Vec2;

/** Plane that fires missiles straight at Ollie every three seconds. */
public class PlaneEnemy extends Enemy {

    private static final Shape SHAPE = new PolygonShape(0.03f, -1.76f, 6.4f, 0.51f, 2.15f, 3.81f, -5.6f, 0.35f);
    private static final Sprite LEFT = Sprite.large("planeLeft.gif");
    private static final Sprite RIGHT = Sprite.large("planeRight.gif");
    private static final Sprite HURT_LEFT = Sprite.large("plandeDamagedLeft.png");
    private static final Sprite HURT_RIGHT = Sprite.large("plandeDamagedRight.png");

    public PlaneEnemy(GameLevel level, int health) {
        super(level, SHAPE, health, 10, 8, 3000, Sound.PLANE_FLYING, Sound.PLANE_DAMAGED, Sound.PLANE_DIED);
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
        Vec2 from = getPosition();
        Vec2 towardsPlayer = player.getPosition().sub(from);
        new MissileBullet(level, from, towardsPlayer);
        Sound.PLANE_ATTACK.play();
    }
}
