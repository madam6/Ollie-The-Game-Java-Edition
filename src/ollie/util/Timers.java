package ollie.util;

import javax.swing.Timer;

/** Small helpers around Swing timers, which run on the event thread like the physics loop. */
public final class Timers {

    private Timers() {
    }

    /** Runs {@code action} once after {@code delayMs}. */
    public static Timer once(int delayMs, Runnable action) {
        Timer timer = new Timer(delayMs, e -> action.run());
        timer.setRepeats(false);
        timer.start();
        return timer;
    }

    /** Runs {@code action} every {@code periodMs}, starting after {@code initialDelayMs}. */
    public static Timer repeat(int periodMs, int initialDelayMs, Runnable action) {
        Timer timer = new Timer(periodMs, e -> action.run());
        timer.setInitialDelay(initialDelayMs);
        timer.start();
        return timer;
    }
}
