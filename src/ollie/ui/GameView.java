package ollie.ui;

import city.cs.engine.UserView;
import ollie.Game;
import ollie.characters.Boss;
import ollie.input.PlayerController;
import ollie.levels.FinalLevel;
import ollie.levels.GameLevel;
import ollie.util.Timers;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * Draws a level: background, shadows and glows, the bodies, darkness, hit flashes, then the
 * HUD on top. Shows Retry / Back once the run is over.
 */
public class GameView extends UserView {

    /** Long enough for the last shake and flash to fade after the world stops. */
    private static final int SETTLE_MS = 800;

    private final GameLevel level;
    private final Image background;
    private final Lighting lighting;
    private final SpriteButton retry;
    private final SpriteButton back;
    private BufferedImage silhouette = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

    public GameView(Game game, GameLevel level) {
        super(level, Game.WIDTH, Game.HEIGHT);
        this.level = level;
        this.background = level.getBackground();
        this.lighting = new Lighting(this, background, Game.WIDTH, Game.HEIGHT, level::groundHeightAt,
                level.darkness(), level.darknessColour());

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
        // The world has stopped stepping, so nothing else triggers a repaint. Paint again
        // once the last shake and flash have faded, so the end screen doesn't freeze on them.
        repaint();
        Timers.once(SETTLE_MS, this::repaint);
    }

    @Override
    public void paintComponent(Graphics g) {
        Point shake = level.getScreenShake().offset();
        Graphics2D scene = (Graphics2D) g.create();
        try {
            if (shake.x != 0 || shake.y != 0) {
                // Shifting the scene uncovers a strip at the edge; fill it with the unshifted background.
                scene.drawImage(background, 0, 0, this);
                scene.translate(shake.x, shake.y);
            }
            super.paintComponent(scene);
        } finally {
            scene.dispose();
        }
        Hud.paint((Graphics2D) g, getWidth(), getHeight(), level.getPlayer(), boss());
    }

    @Override
    protected void paintBackground(Graphics2D g) {
        g.drawImage(background, 0, 0, this);
        lighting.begin();
        level.addLight(lighting);
        for (Object body : level.getStaticBodies()) {
            if (body instanceof Lighting.LightSource source) {
                source.addLight(lighting);
            }
        }
        for (Object body : level.getDynamicBodies()) {
            if (body instanceof Lighting.LightSource source) {
                source.addLight(lighting);
            }
        }
        lighting.paintUnder(g);
    }

    @Override
    protected void paintForeground(Graphics2D g) {
        lighting.paintOver(g);
        for (Object body : level.getDynamicBodies()) {
            if (body instanceof Flashable flashing) {
                paintFlash(g, flashing);
            }
        }
    }

    private Boss boss() {
        return level instanceof FinalLevel finalLevel ? finalLevel.getBoss() : null;
    }

    /** Draws a white silhouette of the body's current sprite over it, fading out after a hit. */
    private void paintFlash(Graphics2D g, Flashable body) {
        float amount = body.flashAmount();
        Sprite sprite = body.getSprite();
        if (amount <= 0 || sprite == null) {
            return;
        }
        Image image = sprite.image();
        int sourceWidth = image.getWidth(this);
        int sourceHeight = image.getHeight(this);
        if (sourceWidth <= 0 || sourceHeight <= 0) {
            return;
        }
        float scale = sprite.height() * getZoom() / sourceHeight;
        int width = Math.round(sourceWidth * scale);
        int height = Math.round(sourceHeight * scale);
        BufferedImage white = silhouette(image, width, height);

        // Same placement as the engine uses: the image centred on the body's position.
        Point2D.Float centre = worldToView(body.getPosition());
        Composite saved = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.min(1, amount)));
        g.drawImage(white, AffineTransform.getTranslateInstance(centre.x - width / 2f, centre.y - height / 2f), null);
        g.setComposite(saved);
    }

    /** The image's current frame at the given size, with every visible pixel turned white. */
    private BufferedImage silhouette(Image image, int width, int height) {
        if (silhouette.getWidth() != width || silhouette.getHeight() != height) {
            silhouette = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }
        Graphics2D g = silhouette.createGraphics();
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, width, height);
        g.setComposite(AlphaComposite.SrcOver);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.drawImage(image, 0, 0, width, height, this);
        g.setComposite(AlphaComposite.SrcIn);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.dispose();
        return silhouette;
    }
}
