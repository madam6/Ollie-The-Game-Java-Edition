package ollie.levels;

import ollie.Game;
import ollie.audio.Sound;
import ollie.characters.PlaneEnemy;
import ollie.characters.RonaldEnemy;
import ollie.characters.SuitEnemy;
import ollie.items.Pickup;
import ollie.ui.Images;

import java.awt.Image;

/** Endless mode: every enemy type keeps coming until Ollie dies. Plays for score. */
public class EndlessLevel extends GameLevel {

    private static final Image BACKGROUND = Images.load("data/background1.png");

    public EndlessLevel(Game game) {
        super(game, 0);
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

        spawnEvery(10_000, 1_000, 0, () -> new PlaneEnemy(this, 50));
        spawnEvery(20_000, 20_000, 0, () -> new RonaldEnemy(this, 25));
        spawnEvery(30_000, 30_000, 0, () -> new SuitEnemy(this, 40));

        spawnEvery(randomBetween(3_000, 10_000), Pickup.Kind.DASH);
        spawnEvery(randomBetween(5_000, 10_000), Pickup.Kind.HEALTH);
        spawnEvery(randomBetween(3_000, 5_000), Pickup.Kind.RAINBOW);
        spawnEvery(randomBetween(5_000, 10_000), 1_000, Pickup.Kind.SUPER_JUMP);
        spawnEvery(randomBetween(6_000, 7_000), 1_000, Pickup.Kind.MASS_ATTACK);
    }
}
