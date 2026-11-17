package ollie.ui;

/** A value that jumps to 1 when triggered and falls back to 0 over a fixed time. */
public final class Pulse {

    private final long durationNanos;
    private long start;
    private boolean active;

    public Pulse(int durationMs) {
        durationNanos = durationMs * 1_000_000L;
    }

    public void trigger() {
        start = System.nanoTime();
        active = true;
    }

    public float value() {
        if (!active) {
            return 0;
        }
        float t = (float) (System.nanoTime() - start) / durationNanos;
        if (t >= 1) {
            active = false;
            return 0;
        }
        return 1 - t;
    }
}
