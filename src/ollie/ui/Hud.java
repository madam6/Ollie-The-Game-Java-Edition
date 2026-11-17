package ollie.ui;

import ollie.characters.Boss;
import ollie.characters.Player;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Health bar, dash and rainbow counters, score, and the boss's health bar. Everything is laid
 * out on the same 5-pixel grid as the characters' art pixels.
 */
final class Hud {

    private static final int P = Sprite.LARGE;
    private static final int BAR_HEIGHT = 3 * P;
    private static final int HEALTH_BAR_WIDTH = 60 * P;
    private static final int BOSS_BAR_WIDTH = 126 * P;

    private static final Color INK = new Color(0x140c1c);
    private static final Color EMPTY = new Color(0x3a2a40);
    private static final Color TEXT = new Color(0xfff4c4);
    private static final Color[] HEALTH = {new Color(0xffd83a), new Color(0xfff3a0), new Color(0xd99a12)};
    private static final Color[] BONUS_HEALTH = {new Color(0xf4fff0), new Color(0xffffff), new Color(0xbfe8c0)};
    private static final Color[] BOSS_HEALTH = {new Color(0xe8384f), new Color(0xff8a96), new Color(0x9c1a35)};

    /** Pickup art shown at 2 screen pixels per art pixel, a bit finer than in the world. */
    private static final int ICON_SCALE = 2;
    private static final BufferedImage HEALTH_ICON = icon("healthToken.gif");
    private static final BufferedImage DASH_ICON = icon("dashToken.gif");
    private static final BufferedImage RAINBOW_ICON = icon("RainbowToken.gif");

    private Hud() {
    }

    static void paint(Graphics2D g, int width, int height, Player player, Boss boss) {
        int health = Math.max(0, player.getHealth());
        float base = Math.min(health, Player.START_HEALTH) / (float) Player.START_HEALTH;
        float bonus = Math.min(Math.max(0, health - Player.START_HEALTH), Player.START_HEALTH) / (float) Player.START_HEALTH;

        int barY = 7 * P;
        iconCentredOn(g, HEALTH_ICON, 6 * P, barY + BAR_HEIGHT / 2 + P);
        bar(g, 16 * P, barY, HEALTH_BAR_WIDTH, base, HEALTH);
        if (bonus > 0) {
            fill(g, 17 * P, barY + P, snap(bonus * HEALTH_BAR_WIDTH), BONUS_HEALTH);
        }

        int rowY = 18 * P;
        int x = counter(g, DASH_ICON, player.getDashes(), 6 * P, rowY);
        counter(g, RAINBOW_ICON, player.getAmmo(), x + 6 * P, rowY);

        String score = "SCORE " + player.getScore();
        PixelFont.draw(g, score, width - 6 * P - PixelFont.width(score, P), barY, P, TEXT, INK);

        if (boss != null) {
            int bossY = height - 11 * P;
            PixelFont.draw(g, "BOSS", 6 * P, bossY, P, TEXT, INK);
            int bossX = 26 * P;
            bar(g, bossX, bossY, BOSS_BAR_WIDTH, Math.max(0, boss.getHealth()) / (float) Boss.MAX_HEALTH, BOSS_HEALTH);
            // Notches where the boss changes phase.
            g.setColor(INK);
            for (int phaseStart : new int[]{Boss.PHASE_2_HEALTH, Boss.PHASE_3_HEALTH}) {
                int notchX = bossX + P + snap(BOSS_BAR_WIDTH * phaseStart / (float) Boss.MAX_HEALTH);
                g.fillRect(notchX, bossY + P, P, BAR_HEIGHT);
            }
        }
    }

    /** Icon, then "x" and the count. Returns the x where the text ends. */
    private static int counter(Graphics2D g, BufferedImage icon, int count, int x, int y) {
        int centreY = y + PixelFont.height(P) / 2;
        iconCentredOn(g, icon, x + icon.getWidth() / 2, centreY);
        String text = "x" + count;
        int textX = x + icon.getWidth() + 2 * P;
        PixelFont.draw(g, text, textX, y, P, TEXT, INK);
        return textX + PixelFont.width(text, P);
    }

    private static void bar(Graphics2D g, int x, int y, int innerWidth, float fraction, Color[] palette) {
        g.setColor(INK);
        g.fillRect(x, y, innerWidth + 2 * P, BAR_HEIGHT + 2 * P);
        g.setColor(EMPTY);
        g.fillRect(x + P, y + P, innerWidth, BAR_HEIGHT);
        fill(g, x + P, y + P, snap(fraction * innerWidth), palette);
    }

    /** A filled bar segment with a light top row and a dark bottom row. */
    private static void fill(Graphics2D g, int x, int y, int width, Color[] palette) {
        if (width <= 0) {
            return;
        }
        g.setColor(palette[0]);
        g.fillRect(x, y, width, BAR_HEIGHT);
        g.setColor(palette[1]);
        g.fillRect(x, y, width, P);
        g.setColor(palette[2]);
        g.fillRect(x, y + BAR_HEIGHT - P, width, P);
    }

    /** Rounds a length to whole grid cells. Anything above zero shows at least one cell. */
    private static int snap(float pixels) {
        if (pixels <= 0) {
            return 0;
        }
        return Math.max(P, Math.round(pixels / P) * P);
    }

    private static void iconCentredOn(Graphics2D g, BufferedImage icon, int x, int y) {
        g.drawImage(icon, x - icon.getWidth() / 2, y - icon.getHeight() / 2, null);
    }

    /** First frame of a pickup GIF, with the empty border cropped off, at {@link #ICON_SCALE}. */
    private static BufferedImage icon(String file) {
        BufferedImage source;
        try {
            source = ImageIO.read(new File("data/" + file));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        int left = source.getWidth();
        int top = source.getHeight();
        int right = -1;
        int bottom = -1;
        for (int y = 0; y < source.getHeight(); y++) {
            for (int x = 0; x < source.getWidth(); x++) {
                if ((source.getRGB(x, y) >>> 24) != 0) {
                    left = Math.min(left, x);
                    top = Math.min(top, y);
                    right = Math.max(right, x);
                    bottom = Math.max(bottom, y);
                }
            }
        }
        // The files are exported at 10 pixels per art pixel.
        int width = (right - left + 1) / 10 * ICON_SCALE;
        int height = (bottom - top + 1) / 10 * ICON_SCALE;
        BufferedImage icon = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = icon.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.drawImage(source, 0, 0, width, height, left, top, right + 1, bottom + 1, null);
        g.dispose();
        return icon;
    }
}
