package ollie.ui;

import ollie.Game;
import ollie.audio.Sound;
import ollie.util.Timers;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A pixel-art button with idle, hover and pressed sprites. All button sprites share the
 * same layout, so the button is a fixed 480x240 area centred horizontally, and only the
 * drawn part of the sprite reacts to the mouse.
 */
public class SpriteButton extends JButton {

    private static final int WIDTH = 480;
    private static final int HEIGHT = 240;
    private static final Rectangle HIT_AREA = new Rectangle(58, 75, 355, 83);
    private static final int CLICK_DELAY_MS = 250;

    private final ImageIcon idle;
    private final ImageIcon hover;
    private final ImageIcon pressed;
    private boolean clicked;

    /**
     * @param centreY vertical centre of the sprite in window pixels
     * @param action  runs a moment after the click, once the pressed sprite has been seen
     */
    public SpriteButton(String idle, String hover, String pressed, int centreY, Runnable action) {
        this.idle = icon(idle);
        this.hover = icon(hover);
        this.pressed = icon(pressed);

        setBounds((Game.WIDTH - WIDTH) / 2, centreY - HEIGHT / 2, WIDTH, HEIGHT);
        setBorder(null);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setFocusable(false);
        setOpaque(false);
        setRolloverEnabled(true);
        reset();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!clicked) {
                    Sound.BUTTON_HOVER.play();
                }
            }
        });
        addActionListener(e -> {
            if (clicked) {
                return;
            }
            clicked = true;
            Sound.BUTTON_CLICK.play();
            setIcon(this.pressed);
            setRolloverIcon(this.pressed);
            Timers.once(CLICK_DELAY_MS, action);
        });
    }

    /** Back to the idle sprite, ready to be clicked again. */
    public void reset() {
        clicked = false;
        setIcon(idle);
        setRolloverIcon(hover);
        setPressedIcon(pressed);
    }

    @Override
    public boolean contains(int x, int y) {
        return HIT_AREA.contains(x, y);
    }

    private static ImageIcon icon(String file) {
        // SCALE_FAST is nearest-neighbour, which keeps the pixel art sharp.
        Image scaled = Images.load("data/" + file).getScaledInstance(WIDTH, HEIGHT, Image.SCALE_FAST);
        return new ImageIcon(scaled);
    }
}
