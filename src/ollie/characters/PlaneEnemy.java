package ollie.characters;

import city.cs.engine.BodyImage;
import city.cs.engine.PolygonShape;
import city.cs.engine.Shape;
import ollie.audio.Sound;
import ollie.bullets.MissileBullet;
import ollie.levels.GameLevel;
import org.jbox2d.common.Vec2;

/** Plane that fires missiles straight at Ollie every three seconds. */
public class PlaneEnemy extends Enemy {

    private static final Shape SHAPE = new PolygonShape(0.03f, -1.54f, 5.6f, 0.45f, 1.88f, 3.33f, -4.9f, 0.31f);
    private static final BodyImage LEFT = new BodyImage("data/planeLeft.gif", 7);
    private static final BodyImage RIGHT = new BodyImage("data/planeRight.gif", 7);
    private static final BodyImage HURT_LEFT = new BodyImage("data/plandeDamagedLeft.png", 7);
    private static final BodyImage HURT_RIGHT = new BodyImage("data/plandeDamagedRight.png", 7);

    public PlaneEnemy(GameLevel level, int health) {
        super(level, SHAPE, health, 10, 8, 3000, Sound.PLANE_FLYING, Sound.PLANE_DAMAGED, Sound.PLANE_DIED);
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
        Vec2 from = getPosition();
        Vec2 towardsPlayer = player.getPosition().sub(from);
        new MissileBullet(level, from, towardsPlayer);
        Sound.PLANE_ATTACK.play();
    }
}
