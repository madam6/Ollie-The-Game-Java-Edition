package ollie.characters;

import city.cs.engine.PolygonShape;
import city.cs.engine.Shape;
import ollie.audio.Sound;
import ollie.bullets.BurgerBullet;
import ollie.bullets.BurstBullet;
import ollie.bullets.MissileBullet;
import ollie.levels.GameLevel;
import ollie.ui.Lighting;
import ollie.ui.Sprite;
import org.jbox2d.common.Vec2;

/**
 * Final boss with three phases. Each phase changes the sprite and adds an attack:
 * missiles, then burgers, then the spread shot.
 */
public class Boss extends Enemy {

    public static final int MAX_HEALTH = 600;
    /** Phase 2 starts once health drops to this or below. */
    public static final int PHASE_2_HEALTH = 400;
    public static final int PHASE_3_HEALTH = 200;

    private static final Shape SHAPE = new PolygonShape(
            0.55f, 0.8f, 3.11f, 1.79f, 1.09f, 4.8f, -1.37f, 4.64f, -3.55f, 2.47f, -1.51f, 1.09f);
    private static final Sprite[] SPRITES = {
            sprite("boss.gif"), sprite("bosssecondPhase.gif"), sprite("bossThirdPhase.gif")};
    private static final Sprite[] HURT_SPRITES = {
            sprite("bossdamaged.gif"), sprite("bosssecondPhaseDamaged.gif"), sprite("bossThirdPhaseDamage.gif")};
    private static final Sprite DEATH = sprite("bossThirdPhaseDamageDied.gif");

    public Boss(GameLevel level) {
        super(level, SHAPE, MAX_HEALTH, 0, 6, 1000, Sound.RONALD_FLYING, Sound.BOSS_DAMAGED, Sound.RONALD_DIED);
    }

    /** The boss is drawn on a 64-pixel canvas, twice the size of the other characters. */
    private static Sprite sprite(String file) {
        return new Sprite(file, 64, Sprite.LARGE);
    }

    /** 1 above 400 health, 2 above 200, 3 below that. */
    public int phase() {
        int health = getHealth();
        return health > PHASE_2_HEALTH ? 1 : health > PHASE_3_HEALTH ? 2 : 3;
    }

    @Override
    protected Sprite sprite(boolean right) {
        return SPRITES[phase() - 1];
    }

    @Override
    protected Sprite hurtSprite(boolean right) {
        return HURT_SPRITES[phase() - 1];
    }

    @Override
    public void takeHit(int damage) {
        super.takeHit(damage);
        if (!isDead()) {
            level.getScreenShake().shake(3, 120);
        }
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

    /**
     * Lights up the area around the boss. No coloured glow: the boss flies against black space,
     * where a glow only shows as a dark disc. The jellyfish sits above the body's origin.
     */
    @Override
    public void addLight(Lighting lighting) {
        lighting.light(getPosition().add(new Vec2(0, 3)), 12, 1);
    }

    /** The boss stays on screen playing its death animation while the victory screen shows. */
    @Override
    protected void onDeath() {
        setSprite(DEATH);
        setLinearVelocity(new Vec2(0, 0));
        setGravityScale(0);
        level.getScreenShake().shake(14, 700);
        level.victory();
    }
}
