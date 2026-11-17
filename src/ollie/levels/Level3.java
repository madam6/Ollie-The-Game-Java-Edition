package ollie.levels;

import ollie.Game;
import ollie.audio.Sound;
import ollie.characters.SuitEnemy;
import ollie.items.Pickup;
import ollie.ui.Images;
import ollie.ui.Lighting;
import org.jbox2d.common.Vec2;

import java.awt.Color;
import java.awt.Image;

/** Level 3: two suits on uneven ground, in a dark cave lit by the volcano. One kill (8 points) clears it. */
public class Level3 extends GameLevel {

    private static final Image BACKGROUND = Images.load("data/nextnextBackGround-export.png");
    private static final Color SHADE = new Color(0x12060e);
    private static final Color LAVA = new Color(0xff5a1e);
    private static final Vec2 CRATER = new Vec2(0.5f, 7.5f);
    /** Points down the lava flows painted on the volcano's sides. */
    private static final Vec2[] LAVA_FLOWS = {new Vec2(2.5f, 2.5f), new Vec2(5.5f, -3.5f), new Vec2(-3, -1)};

    public Level3(Game game) {
        super(game, 8);
    }

    @Override
    public Image getBackground() {
        return BACKGROUND;
    }

    @Override
    public float darkness() {
        return 0.6f;
    }

    @Override
    public Color darknessColour() {
        return SHADE;
    }

    /** The crater and the lava flows glow, with a slow flicker. */
    @Override
    public void addLight(Lighting lighting) {
        super.addLight(lighting);
        double t = System.currentTimeMillis() / 1000.0;
        float flicker = (float) (0.9 + 0.06 * Math.sin(t * 5.3) + 0.04 * Math.sin(t * 13.1));
        lighting.glow(CRATER, 6, LAVA, 0.35f * flicker);
        lighting.light(CRATER, 9, flicker);
        for (Vec2 point : LAVA_FLOWS) {
            lighting.light(point, 4.5f, 0.6f * flicker);
        }
    }

    @Override
    public void begin() {
        playMusic(Sound.LEVEL_MUSIC);
        addHillyGround();
        addSideWalls();
        new SuitEnemy(this, 50);
        new SuitEnemy(this, 50);

        spawnEvery(randomBetween(5_000, 10_000), Pickup.Kind.DASH);
        spawnEvery(randomBetween(20_000, 30_000), Pickup.Kind.HEALTH);
        spawnEvery(randomBetween(3_000, 5_000), Pickup.Kind.RAINBOW);
        spawnEvery(randomBetween(5_000, 10_000), 1_000, Pickup.Kind.SUPER_JUMP);
        spawnEvery(randomBetween(20_000, 25_000), 1_000, Pickup.Kind.MASS_ATTACK);
    }
}
