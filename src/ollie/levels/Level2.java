package ollie.levels;

import ollie.Game;
import ollie.audio.Sound;
import ollie.characters.RonaldEnemy;
import ollie.items.Barrel;
import ollie.items.Pickup;
import ollie.ui.Images;

import java.awt.Image;

/** Level 2: two burger-throwing clowns and some barrels for cover. One kill clears it. */
public class Level2 extends GameLevel {

    private static final Image BACKGROUND = Images.load("data/nextBackGround-export.png");

    public Level2(Game game) {
        super(game, 5);
    }

    @Override
    public Image getBackground() {
        return BACKGROUND;
    }

    @Override
    public void begin() {
        playMusic(Sound.LEVEL_MUSIC);
        addFlatGround();
        addSideWalls();
        new Barrel(this, 15, -12);
        new Barrel(this, 15, -7);
        new Barrel(this, -20, -12);
        new Barrel(this, -13, -12);

        spawnEvery(randomBetween(4_000, 10_000), 1_000, 2, () -> new RonaldEnemy(this, 25));
        spawnEvery(randomBetween(5_000, 10_000), Pickup.Kind.DASH);
        spawnEvery(randomBetween(15_000, 20_000), Pickup.Kind.HEALTH);
        spawnEvery(randomBetween(3_000, 5_000), Pickup.Kind.RAINBOW);
        spawnEvery(randomBetween(5_000, 10_000), 1_000, Pickup.Kind.MASS_ATTACK);
    }
}
