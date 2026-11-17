package ollie.levels;

import ollie.Game;
import ollie.audio.Sound;
import ollie.characters.SuitEnemy;
import ollie.items.Pickup;
import ollie.ui.Images;

import java.awt.Image;

/** Level 3: two suits on uneven ground. One kill (8 points) clears it. */
public class Level3 extends GameLevel {

    private static final Image BACKGROUND = Images.load("data/nextnextBackGround-export.png");

    public Level3(Game game) {
        super(game, 8);
    }

    @Override
    public Image getBackground() {
        return BACKGROUND;
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
