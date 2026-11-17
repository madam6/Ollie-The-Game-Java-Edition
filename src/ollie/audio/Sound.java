package ollie.audio;

import city.cs.engine.SoundClip;

/**
 * Every sound in the game. Each file is decoded once and the clip is reused, so playing a
 * sound never touches the disk. A background thread loads them all while the menu is up.
 *
 * <p>Run with {@code -Dollie.mute=true} to skip audio entirely.
 */
public enum Sound {
    // menu
    MENU_MUSIC("menu.wav"),
    BUTTON_HOVER("clickMouse.wav"),
    BUTTON_CLICK("playendless.wav"),

    // music
    LEVEL1_MUSIC("fight122.wav", 0.5),
    LEVEL_MUSIC("level23.wav", 0.25),
    BOSS_MUSIC("boss.wav", 0.75),
    LEVEL_COMPLETE("planeDied.wav"),
    VICTORY("victory!.wav"),

    // player
    FIRE("fire1.wav"),
    DASH("dash.wav", 2),
    JUMP("jump.wav", 1.4),
    PLAYER_DAMAGED("damaged.wav"),
    PLAYER_DIED("death.wav"),

    // pickups
    DASH_PICKUP("dashToken.wav"),
    HEAL_PICKUP("heal.wav"),
    RAINBOW_PICKUP("rainbowToken.wav"),
    SUPER_JUMP_PICKUP("superJump.wav"),
    MASS_ATTACK_PICKUP("massAttackLoad.wav", 2),

    // projectiles
    MISSILE_EXPLOSION("misstleExplosion.wav"),
    BURGER_EATEN("eaten.wav"),
    BURST_HIT("newAttackHit.wav"),

    // enemies
    PLANE_FLYING("planeFlying.wav", 0.25),
    PLANE_ATTACK("planeAttack.wav"),
    PLANE_DAMAGED("planeDamaged.wav"),
    PLANE_DIED("planeDied.wav"),
    RONALD_FLYING("ronaldFlying.wav", 2),
    RONALD_ATTACK("attackRonald.wav"),
    RONALD_DAMAGED("ronaldDamaged.wav"),
    RONALD_DIED("ronaldDead.wav"),
    SUIT_AMBIENT("suitSound.wav", 2),
    SUIT_ATTACK("suitAttack.wav"),
    SUIT_DAMAGED("suitDamaged.wav"),
    SUIT_DIED("suitDied.wav"),
    BOSS_DAMAGED("bossDamaged1.wav");

    private static final String DIRECTORY = "data/music/";
    private static final boolean MUTED = Boolean.getBoolean("ollie.mute");

    private final String file;
    private final double volume;
    private volatile SoundClip clip;
    private boolean failed;
    private int loopOwners;

    Sound(String file) {
        this(file, 1.0);
    }

    Sound(String file, double volume) {
        this.file = file;
        this.volume = volume;
    }

    /** Plays the sound from the start. If it is already playing it restarts. */
    public void play() {
        SoundClip c = clip();
        if (c != null) {
            c.play();
        }
    }

    public void loop() {
        SoundClip c = clip();
        if (c != null) {
            c.loop();
        }
    }

    public void stop() {
        SoundClip c = clip;
        if (c != null) {
            c.stop();
        }
    }

    /**
     * Joins a loop shared by several owners, such as the engine noise of every plane on
     * screen. The loop starts with the first owner and stops when the last one leaves.
     */
    public void acquireLoop() {
        if (loopOwners++ == 0) {
            loop();
        }
    }

    public void releaseLoop() {
        if (loopOwners > 0 && --loopOwners == 0) {
            stop();
        }
    }

    public static void stopAll() {
        for (Sound sound : values()) {
            sound.loopOwners = 0;
            sound.stop();
        }
    }

    /** Decodes every sound on a low-priority thread so the first spawn of each enemy doesn't stall. */
    public static void preloadInBackground() {
        if (MUTED) {
            return;
        }
        Thread loader = new Thread(() -> {
            for (Sound sound : values()) {
                sound.clip();
            }
        }, "sound-preload");
        loader.setDaemon(true);
        loader.setPriority(Thread.MIN_PRIORITY);
        loader.start();
    }

    private synchronized SoundClip clip() {
        if (clip == null && !failed && !MUTED) {
            try {
                SoundClip loaded = new SoundClip(DIRECTORY + file);
                loaded.setVolume(volume);
                clip = loaded;
            } catch (Exception e) {
                failed = true;
                System.err.println("Could not load sound " + file + ": " + e.getMessage());
            }
        }
        return clip;
    }
}
