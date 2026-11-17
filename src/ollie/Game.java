package ollie;

import ollie.audio.Sound;
import ollie.levels.EndlessLevel;
import ollie.levels.FinalLevel;
import ollie.levels.GameLevel;
import ollie.levels.Level1;
import ollie.levels.Level2;
import ollie.levels.Level3;
import ollie.ui.GameView;
import ollie.ui.ImagePanel;
import ollie.ui.Images;
import ollie.ui.MenuPanel;
import ollie.util.Timers;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Entry point. Owns the single game window and moves between the menu, the story levels
 * and endless mode.
 *
 * @author Artem Korniienko
 */
public final class Game {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    private final JFrame frame = new JFrame("Ollie! The Game");
    private GameLevel level;
    private GameView view;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Game::new);
    }

    private Game() {
        frame.setIconImage(Images.load("data/icon.png"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        showMenu();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Sound.preloadInBackground();
    }

    public void showMenu() {
        Sound.stopAll();
        show(new MenuPanel(this));
        Sound.MENU_MUSIC.loop();
    }

    public void startStory() {
        playIntro("data/infoScreen.png", 10_000, new Level1(this));
    }

    public void startEndless() {
        playIntro("data/infinite.png", 3_000, new EndlessLevel(this));
    }

    /** Restarts after a game over: endless mode starts over, story mode goes back to level 1. */
    public void retry() {
        startLevel(level instanceof EndlessLevel ? new EndlessLevel(this) : new Level1(this));
    }

    /** Shows the retry and menu buttons after the player dies or beats the boss. */
    public void showEndScreen() {
        view.showEndButtons();
    }

    /**
     * Called by a story level once its score target is reached. Plays the transition
     * animation, then starts the next level with the player's health, dashes and ammo.
     */
    public void levelCompleted(GameLevel finished) {
        finished.shutdown();
        Sound.LEVEL_COMPLETE.play();

        GameLevel next;
        String animation;
        int duration;
        if (finished instanceof Level1) {
            next = new Level2(this);
            animation = "data/level2Anim..gif";
            duration = 6_000;
        } else if (finished instanceof Level2) {
            next = new Level3(this);
            animation = "data/toTheLevel3.gif";
            duration = 8_000;
        } else if (finished instanceof Level3) {
            next = new FinalLevel(this);
            animation = "data/toTheFinalLevel.gif";
            duration = 9_300;
        } else {
            return;
        }

        next.getPlayer().carryOver(finished.getPlayer());
        // A fresh copy of the GIF so the animation starts from frame one on every playthrough.
        show(new ImagePanel(Images.loadFresh(animation)));
        Timers.once(duration, () -> startLevel(next));
    }

    private void playIntro(String image, int duration, GameLevel first) {
        Sound.MENU_MUSIC.stop();
        show(new ImagePanel(Images.load(image)));
        Timers.once(duration, () -> startLevel(first));
    }

    private void startLevel(GameLevel next) {
        level = next;
        view = new GameView(this, next);
        show(view);
        next.begin();
        next.start();
    }

    private void show(JComponent screen) {
        frame.setContentPane(screen);
        frame.pack();
        screen.requestFocusInWindow();
    }
}
