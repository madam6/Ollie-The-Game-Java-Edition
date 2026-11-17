package ollie.ui;

import ollie.Game;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.List;

/** Title screen: a Play button that opens the Story / Endless / Back choice. */
public class MenuPanel extends JPanel {

    /** The animated background is 790px tall; the last 10px are filled with its road colour. */
    private static final Image BACKGROUND = Images.load("data/backgroundnightanim.gif");
    private static final Color ROAD = new Color(0x1c1c1c);
    private static final Image TITLE = Images.load("data/ollieTheGame.png");

    private final SpriteButton play;
    private final List<SpriteButton> modeButtons;

    public MenuPanel(Game game) {
        super(null);
        setPreferredSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        setBackground(ROAD);

        play = new SpriteButton("playButton.png", "playButtonOnMounce.png", "playButtonClicked.png",
                460, () -> showModes(true));
        SpriteButton story = new SpriteButton("storyB.png", "storyBM.png", "storyBC.png",
                460, game::startStory);
        SpriteButton endless = new SpriteButton("endlessB.png", "endlessBM.png", "endlessBC.png",
                560, game::startEndless);
        SpriteButton back = new SpriteButton("backB.png", "backBM.png", "backBC.png",
                660, () -> showModes(false));
        modeButtons = List.of(story, endless, back);

        add(play);
        modeButtons.forEach(this::add);
        showModes(false);
    }

    private void showModes(boolean visible) {
        play.reset();
        play.setVisible(!visible);
        for (SpriteButton button : modeButtons) {
            button.reset();
            button.setVisible(visible);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(BACKGROUND, 0, 0, this);
        g.drawImage(TITLE, 133, 100, 533, 400, this);
    }
}
