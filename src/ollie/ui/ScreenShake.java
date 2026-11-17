package ollie.ui;

import java.awt.Point;
import java.util.Random;

/** Jolts the view by a few pixels after explosions and hits, fading out over the shake's duration. */
public final class ScreenShake {

    /** The lighting layer extends this far past the screen edges, so a shake never uncovers its edge. */
    public static final int MAX_PIXELS = 15;

    private final Random random = new Random();
    private float strength;
    private long start;
    private long durationNanos = 1;

    /** Starts a shake, unless a stronger one is still running. */
    public void shake(float pixels, int durationMs) {
        if (pixels >= currentStrength()) {
            strength = Math.min(pixels, MAX_PIXELS);
            start = System.nanoTime();
            durationNanos = durationMs * 1_000_000L;
        }
    }

    /** Offset to draw the scene at this frame. */
    public Point offset() {
        float s = currentStrength();
        if (s < 1) {
            return new Point();
        }
        return new Point(Math.round((random.nextFloat() * 2 - 1) * s), Math.round((random.nextFloat() * 2 - 1) * s));
    }

    private float currentStrength() {
        float t = (float) (System.nanoTime() - start) / durationNanos;
        return t >= 1 ? 0 : strength * (1 - t);
    }
}
