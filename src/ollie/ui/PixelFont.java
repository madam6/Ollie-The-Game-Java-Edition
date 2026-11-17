package ollie.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Map;

/**
 * A 3x5 block font for the HUD, drawn as squares so the text sits on the same pixel grid as
 * the sprites. It only has the characters the HUD uses.
 */
final class PixelFont {

    private static final int WIDTH = 3;
    private static final int HEIGHT = 5;

    private static final Map<Character, String[]> GLYPHS = Map.ofEntries(
            glyph('0', "###", "#.#", "#.#", "#.#", "###"),
            glyph('1', ".#.", "##.", ".#.", ".#.", "###"),
            glyph('2', "###", "..#", "###", "#..", "###"),
            glyph('3', "###", "..#", "###", "..#", "###"),
            glyph('4', "#.#", "#.#", "###", "..#", "..#"),
            glyph('5', "###", "#..", "###", "..#", "###"),
            glyph('6', "###", "#..", "###", "#.#", "###"),
            glyph('7', "###", "..#", ".#.", ".#.", ".#."),
            glyph('8', "###", "#.#", "###", "#.#", "###"),
            glyph('9', "###", "#.#", "###", "..#", "###"),
            glyph('B', "##.", "#.#", "##.", "#.#", "##."),
            glyph('C', ".##", "#..", "#..", "#..", ".##"),
            glyph('E', "###", "#..", "##.", "#..", "###"),
            glyph('O', ".#.", "#.#", "#.#", "#.#", ".#."),
            glyph('R', "##.", "#.#", "##.", "#.#", "#.#"),
            glyph('S', ".##", "#..", ".#.", "..#", "##."),
            glyph('x', "...", "#.#", ".#.", "#.#", "..."),
            glyph(' ', "...", "...", "...", "...", "..."));

    private PixelFont() {
    }

    /** Width of {@code text} in screen pixels when each font pixel is {@code scale} pixels. */
    static int width(String text, int scale) {
        return text.isEmpty() ? 0 : (text.length() * (WIDTH + 1) - 1) * scale;
    }

    static int height(int scale) {
        return HEIGHT * scale;
    }

    /**
     * Draws the text with its top-left corner at (x, y), outlined one font pixel wide so it
     * reads on any background.
     */
    static void draw(Graphics2D g, String text, int x, int y, int scale, Color colour, Color outline) {
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                if (dx != 0 || dy != 0) {
                    drawPlain(g, text, x + dx * scale, y + dy * scale, scale, outline);
                }
            }
        }
        drawPlain(g, text, x, y, scale, colour);
    }

    private static void drawPlain(Graphics2D g, String text, int x, int y, int scale, Color colour) {
        g.setColor(colour);
        for (int i = 0; i < text.length(); i++) {
            String[] rows = GLYPHS.get(text.charAt(i));
            if (rows == null) {
                throw new IllegalArgumentException("No glyph for '" + text.charAt(i) + "'");
            }
            int left = x + i * (WIDTH + 1) * scale;
            for (int row = 0; row < HEIGHT; row++) {
                for (int col = 0; col < WIDTH; col++) {
                    if (rows[row].charAt(col) == '#') {
                        g.fillRect(left + col * scale, y + row * scale, scale, scale);
                    }
                }
            }
        }
    }

    private static Map.Entry<Character, String[]> glyph(char c, String... rows) {
        return Map.entry(c, rows);
    }
}
