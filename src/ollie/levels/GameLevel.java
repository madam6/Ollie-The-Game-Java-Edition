package ollie.levels;

import city.cs.engine.BodyImage;
import city.cs.engine.BoxShape;
import city.cs.engine.DynamicBody;
import city.cs.engine.PolygonShape;
import city.cs.engine.Shape;
import city.cs.engine.StaticBody;
import city.cs.engine.StepEvent;
import city.cs.engine.StepListener;
import city.cs.engine.World;
import ollie.Game;
import ollie.audio.Sound;
import ollie.characters.Enemy;
import ollie.characters.Player;
import ollie.items.Pickup;
import ollie.ui.Lighting;
import ollie.ui.ScreenShake;
import ollie.ui.Sprite;
import ollie.util.Timers;
import org.jbox2d.common.Vec2;

import javax.swing.Timer;
import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A playable level. Subclasses describe the scenery and what spawns when; this class
 * owns the player, the spawn timers and the win/lose checks.
 */
public abstract class GameLevel extends World implements StepListener, Lighting.LightSource {

    private static final int FRAMES_PER_SECOND = 60;
    private static final Random RANDOM = new Random();

    /** Static bodies with no image get a debug fill, so scenery gets a transparent one. */
    private static final BodyImage INVISIBLE = new BodyImage("data/dummyImage.png");
    /** The banner is drawn on an 80-pixel canvas. */
    private static final Sprite VICTORY_LABEL = new Sprite("victory.png", 80, Sprite.LARGE);
    private static final Vec2 VICTORY_LABEL_POSITION = new Vec2(0, 5);
    private static final Shape FLAT_GROUND = new BoxShape(22, 2);
    private static final float[] FLAT_GROUND_TOP = {-22, -16.5f, 22, -16.5f};
    private static final Shape HILLY_GROUND = new PolygonShape(
            20.6f, -18.1f, 5.5f, -17.0f, -6.0f, -17.4f, -17.6f, -19.7f, -20.7f, -20.9f, 20.3f, -20.6f);
    /** The upper edge of {@link #HILLY_GROUND}, left to right, as x, y pairs. */
    private static final float[] HILLY_GROUND_TOP = {
            -20.7f, -20.9f, -17.6f, -19.7f, -6.0f, -17.4f, 5.5f, -17.0f, 20.6f, -18.1f};
    private static final Shape WALL = new BoxShape(2, 15);

    private final Game game;
    private final Player player;
    private final int scoreToWin;
    private final List<Timer> spawners = new ArrayList<>();
    private final ScreenShake screenShake = new ScreenShake();
    private float[] groundTop = {};
    private Sound music;
    private boolean over;
    private boolean won;

    /**
     * @param scoreToWin score that completes the level, or 0 if it never ends on score
     */
    protected GameLevel(Game game, int scoreToWin) {
        super(FRAMES_PER_SECOND);
        this.game = game;
        this.scoreToWin = scoreToWin;
        player = new Player(this);
        player.setPosition(new Vec2(0, -15));
        addStepListener(player);
        addStepListener(this);
    }

    public abstract Image getBackground();

    /** Builds the scenery, starts the music and the spawn timers. Runs when the level appears. */
    public abstract void begin();

    public Player getPlayer() {
        return player;
    }

    public boolean isOver() {
        return over;
    }

    public ScreenShake getScreenShake() {
        return screenShake;
    }

    /** How dark unlit parts of the level are, from 0 (no darkness) to 1. */
    public float darkness() {
        return 0;
    }

    /** Colour of the darkness, if there is any. */
    public Color darknessColour() {
        return Color.BLACK;
    }

    /** Height of the ground's surface at {@code x}, or NaN if there's no ground there. */
    public float groundHeightAt(float x) {
        for (int i = 2; i < groundTop.length; i += 2) {
            float x0 = groundTop[i - 2];
            float x1 = groundTop[i];
            if (x >= x0 && x <= x1) {
                float t = (x - x0) / (x1 - x0);
                return groundTop[i - 1] + t * (groundTop[i + 1] - groundTop[i - 1]);
            }
        }
        return Float.NaN;
    }

    /** Lights fixed to the scenery. The victory banner is lit so it shows on the dark levels. */
    @Override
    public void addLight(Lighting lighting) {
        if (won) {
            lighting.light(VICTORY_LABEL_POSITION, 14, 1);
        }
    }

    /** Ends the level as a win: victory jingle, banner, and the end-screen buttons. */
    public void victory() {
        if (over) {
            return;
        }
        shutdown();
        won = true;
        Sound.VICTORY.play();
        StaticBody label = new StaticBody(this);
        label.setPosition(VICTORY_LABEL_POSITION);
        label.addImage(VICTORY_LABEL.bodyImage());
        game.showEndScreen();
    }

    /** Stops the simulation, spawn timers, enemy attacks and music. Safe to call twice. */
    public void shutdown() {
        if (over) {
            return;
        }
        over = true;
        stop();
        spawners.forEach(Timer::stop);
        if (music != null) {
            music.stop();
        }
        for (DynamicBody body : getDynamicBodies()) {
            if (body instanceof Enemy enemy) {
                enemy.silence();
            }
        }
    }

    @Override
    public void preStep(StepEvent e) {
    }

    @Override
    public void postStep(StepEvent e) {
        if (over) {
            return;
        }
        if (player.isDead()) {
            shutdown();
            game.showEndScreen();
        } else if (scoreToWin > 0 && player.getScore() >= scoreToWin) {
            game.levelCompleted(this);
        }
    }

    // ---- helpers for subclasses ----

    protected void playMusic(Sound track) {
        music = track;
        track.loop();
    }

    protected void addFlatGround() {
        addScenery(FLAT_GROUND, 0, -18.5f);
        groundTop = FLAT_GROUND_TOP;
    }

    protected void addHillyGround() {
        addScenery(HILLY_GROUND, 0, 0);
        groundTop = HILLY_GROUND_TOP;
    }

    protected void addSideWalls() {
        addScenery(WALL, -23, 0);
        addScenery(WALL, 23, 0);
    }

    private void addScenery(Shape shape, float x, float y) {
        StaticBody body = new StaticBody(this, shape);
        body.setPosition(new Vec2(x, y));
        body.addImage(INVISIBLE);
    }

    protected static int randomBetween(int min, int max) {
        return RANDOM.nextInt(min, max);
    }

    /** Spawns a pickup every {@code periodMs}, the first one after a full period. */
    protected void spawnEvery(int periodMs, Pickup.Kind kind) {
        spawnEvery(periodMs, periodMs, kind);
    }

    protected void spawnEvery(int periodMs, int initialDelayMs, Pickup.Kind kind) {
        spawnEvery(periodMs, initialDelayMs, 0, () -> new Pickup(this, kind));
    }

    /**
     * Runs {@code spawn} every {@code periodMs} while the level is in play.
     *
     * @param limit how many times to spawn in total, or 0 for no limit
     */
    protected void spawnEvery(int periodMs, int initialDelayMs, int limit, Runnable spawn) {
        int[] spawned = {0};
        Timer[] timer = new Timer[1];
        timer[0] = Timers.repeat(periodMs, initialDelayMs, () -> {
            if (over) {
                return;
            }
            spawn.run();
            if (limit > 0 && ++spawned[0] >= limit) {
                timer[0].stop();
            }
        });
        spawners.add(timer[0]);
    }
}
