package ollie.ui;

import city.cs.engine.BodyImage;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * An image for a physics body, sized so that each pixel of the source art covers a whole
 * number of screen pixels. At sizes that don't divide evenly some art pixels come out wider
 * than others, which looks lumpy and shimmers as the sprite moves.
 *
 * <p>The art is drawn on canvases 32 pixels tall (the boss is 64) and exported at 10x. The
 * view draws 20 screen pixels per world unit, so a 32-pixel canvas at 5 screen pixels per
 * art pixel is 32 * 5 / 20 = 8 units tall.
 */
public final class Sprite {

    public static final float PIXELS_PER_UNIT = 20;

    /** Characters, barrels and explosions: half the backgrounds' 10 pixels per art pixel. */
    public static final int LARGE = 5;
    /** Pickups, burgers, missiles and rainbows. */
    public static final int SMALL = 3;
    private static final int CANVAS = 32;

    private final BodyImage bodyImage;
    private final Image image;
    private final float height;

    /**
     * @param file      file name in {@code data/}
     * @param artHeight height of the canvas in art pixels
     * @param scale     screen pixels per art pixel
     */
    public Sprite(String file, int artHeight, int scale) {
        String path = "data/" + file;
        height = artHeight * scale / PIXELS_PER_UNIT;
        bodyImage = new BodyImage(path, height);
        // BodyImage loads through the same URL, and the toolkit caches images by URL, so this is
        // the Image the engine draws, on the same animation frame.
        image = new ImageIcon(url(path)).getImage();
    }

    public static Sprite large(String file) {
        return new Sprite(file, CANVAS, LARGE);
    }

    public static Sprite small(String file) {
        return new Sprite(file, CANVAS, SMALL);
    }

    public BodyImage bodyImage() {
        return bodyImage;
    }

    public Image image() {
        return image;
    }

    /** Height in world units. */
    public float height() {
        return height;
    }

    private static URL url(String path) {
        try {
            return new URL("file", "localhost", -1, new File(path).getAbsolutePath());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(path, e);
        }
    }
}
