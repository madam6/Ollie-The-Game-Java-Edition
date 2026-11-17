package ollie.ui;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Toolkit;

public final class Images {

    private Images() {
    }

    /** Loads an image through the AWT image cache and waits until it is ready to draw. */
    public static Image load(String path) {
        return new ImageIcon(path).getImage();
    }

    /**
     * Loads a separate copy that bypasses the cache. Animated GIFs from the cache keep
     * playing from wherever they were, so one-off animations need their own copy.
     */
    public static Image loadFresh(String path) {
        return new ImageIcon(Toolkit.getDefaultToolkit().createImage(path)).getImage();
    }
}
