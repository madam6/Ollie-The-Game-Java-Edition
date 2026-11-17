package ollie.ui;

import city.cs.engine.UserView;
import ollie.Game;
import ollie.characters.Boss;
import ollie.characters.Player;
import ollie.input.PlayerController;
import ollie.levels.FinalLevel;
import ollie.levels.GameLevel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/** Draws a level with the HUD on top, and shows Retry / Back once the run is over. */
public class GameView extends UserView {

    private static final Font HUD_FONT = new Font("SansSerif", Font.PLAIN, 24);
    private static final float HEALTH_BAR_SCALE = 1.36f;
    private static final float BOSS_BAR_SCALE = 1.3f;

    private final GameLevel level;
    private final Image background;
    private final SpriteButton retry;
    private final SpriteButton back;

    public GameView(Game game, GameLevel level) {
        super(level, Game.WIDTH, Game.HEIGHT);
        this.level = level;
        this.background = level.getBackground();

        setLayout(null);
        setFocusable(true);
        retry = new SpriteButton("retryB.png", "retryBM.png", "retryBC.png", 460, game::retry);
        back = new SpriteButton("backB.png", "backBM.png", "backBC.png", 560, game::showMenu);
        retry.setVisible(false);
        back.setVisible(false);
        add(retry);
        add(back);

        addKeyListener(new PlayerController(level.getPlayer()));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                requestFocusInWindow();
            }
        });
    }

    public void showEndButtons() {
        retry.setVisible(true);
        back.setVisible(true);
        // The world has stopped stepping, so nothing else will trigger a repaint.
        repaint();
    }

    @Override
    protected void paintBackground(Graphics2D g) {
        g.drawImage(background, 0, 0, this);
    }

    @Override
    protected void paintForeground(Graphics2D g) {
        Player player = level.getPlayer();
        g.setFont(HUD_FONT);
        g.setColor(Color.YELLOW);
        g.fillRect(60, 40, Math.max(0, (int) (player.getHealth() * HEALTH_BAR_SCALE)), 10);
        g.drawString("Dashes: " + player.getDashes(), 50, 100);
        g.drawString("Rainbows: " + player.getAmmo(), 50, 130);
        g.drawString(String.valueOf(player.getScore()), 390, 100);

        if (level instanceof FinalLevel finalLevel) {
            Boss boss = finalLevel.getBoss();
            if (boss != null) {
                g.setColor(Color.RED);
                g.fillRect(60, 740, Math.max(0, (int) (boss.getHealth() * BOSS_BAR_SCALE)), 10);
            }
        }
    }
}
