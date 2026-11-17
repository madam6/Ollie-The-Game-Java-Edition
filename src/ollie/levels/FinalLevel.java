package ollie.levels;

import ollie.Game;
import ollie.audio.Sound;
import ollie.characters.Boss;
import ollie.items.Pickup;
import ollie.ui.Images;

import java.awt.Color;
import java.awt.Image;

/** The boss fight, at night. Every pickup type shows up on a slow, staggered rotation. */
public class FinalLevel extends GameLevel {

    private static final Image BACKGROUND = Images.load("data/finalBackground-export.png");
    private static final Color SHADE = new Color(0x0a0626);
    private static final int PICKUP_PERIOD_MS = 15_000;

    private Boss boss;

    public FinalLevel(Game game) {
        super(game, 0);
    }

    public Boss getBoss() {
        return boss;
    }

    @Override
    public Image getBackground() {
        return BACKGROUND;
    }

    @Override
    public float darkness() {
        return 0.5f;
    }

    @Override
    public Color darknessColour() {
        return SHADE;
    }

    @Override
    public void begin() {
        playMusic(Sound.BOSS_MUSIC);
        addFlatGround();
        addSideWalls();
        boss = new Boss(this);

        spawnEvery(PICKUP_PERIOD_MS, 1_000, Pickup.Kind.DASH);
        spawnEvery(PICKUP_PERIOD_MS, 2_000, Pickup.Kind.HEALTH);
        spawnEvery(PICKUP_PERIOD_MS, 3_000, Pickup.Kind.RAINBOW);
        spawnEvery(PICKUP_PERIOD_MS, 4_000, Pickup.Kind.SUPER_JUMP);
        spawnEvery(PICKUP_PERIOD_MS, 5_000, Pickup.Kind.MASS_ATTACK);
    }
}
