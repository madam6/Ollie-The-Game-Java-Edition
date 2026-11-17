package ollie.levels;

import ollie.Game;
import ollie.audio.Sound;
import ollie.characters.PlaneEnemy;
import ollie.items.Barrel;
import ollie.items.Pickup;
import ollie.ui.Images;

import java.awt.Image;

/** Level 1: up to four planes. One kill (10 points) clears it. */
public class Level1 extends GameLevel {

    private static final Image BACKGROUND = Images.load("data/background1.png");

    public Level1(Game game) {
        super(game, 10);
    }

    @Override
    public Image getBackground() {
        return BACKGROUND;
    }

    @Override
    public void begin() {
        playMusic(Sound.LEVEL1_MUSIC);
        addFlatGround();
        addSideWalls();
        new Barrel(this, 5, -12);

        spawnEvery(randomBetween(8_000, 10_000), 1_000, 4, () -> new PlaneEnemy(this, 40));
        spawnEvery(randomBetween(5_000, 10_000), Pickup.Kind.DASH);
        spawnEvery(randomBetween(3_000, 5_000), Pickup.Kind.RAINBOW);
    }
}
