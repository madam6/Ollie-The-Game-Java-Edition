package ollie.characters;

import city.cs.engine.BodyImage;
import city.cs.engine.PolygonShape;
import city.cs.engine.Shape;
import ollie.audio.Sound;
import ollie.bullets.BurgerBullet;
import ollie.bullets.BurstBullet;
import ollie.bullets.MissileBullet;
import ollie.levels.GameLevel;
import org.jbox2d.common.Vec2;

/**
 * Final boss with three phases. Each phase changes the sprite and adds an attack:
 * missiles, then burgers, then the spread shot.
 */
public class Boss extends Enemy {

    public static final int MAX_HEALTH = 600;

    private static final Shape SHAPE = new PolygonShape(
            0.58f, 0.85f, 3.3f, 1.9f, 1.16f, 5.1f, -1.46f, 4.93f, -3.77f, 2.62f, -1.6f, 1.16f);
    private static final BodyImage[] SPRITES = {
            sprite("boss.gif"), sprite("bosssecondPhase.gif"), sprite("bossThirdPhase.gif")};
    private static final BodyImage[] HURT_SPRITES = {
            sprite("bossdamaged.gif"), sprite("bosssecondPhaseDamaged.gif"), sprite("bossThirdPhaseDamage.gif")};
    private static final BodyImage DEATH = sprite("bossThirdPhaseDamageDied.gif");

    public Boss(GameLevel level) {
        super(level, SHAPE, MAX_HEALTH, 0, 6, 1000, Sound.RONALD_FLYING, Sound.BOSS_DAMAGED, Sound.RONALD_DIED);
    }

    private static BodyImage sprite(String file) {
        return new BodyImage("data/" + file, 17);
    }

    /** 1 above 400 health, 2 above 200, 3 below that. */
    public int phase() {
        int health = getHealth();
        return health > 400 ? 1 : health > 200 ? 2 : 3;
    }

    @Override
    protected BodyImage sprite(boolean right) {
        return SPRITES[phase() - 1];
    }

    @Override
    protected BodyImage hurtSprite(boolean right) {
        return HURT_SPRITES[phase() - 1];
    }

    @Override
    protected void attack() {
        Vec2 position = getPosition();
        new MissileBullet(level, position, new Vec2(position.x, position.y - 20));
        Sound.PLANE_ATTACK.play();
        if (phase() >= 2) {
            new BurgerBullet(level, position, false);
            Sound.RONALD_ATTACK.play();
        }
        if (phase() >= 3) {
            BurstBullet.enemyBurst(level, position);
            Sound.SUIT_ATTACK.play();
        }
    }

    /** The boss stays on screen playing its death animation while the victory screen shows. */
    @Override
    protected void onDeath() {
        setSprite(DEATH);
        setLinearVelocity(new Vec2(0, 0));
        setGravityScale(0);
        level.victory();
    }
}
