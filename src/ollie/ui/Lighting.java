package ollie.ui;

import city.cs.engine.WorldView;
import org.jbox2d.common.Vec2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

/**
 * Glows, ground shadows and darkness, computed on a grid of 5x5-pixel cells and scaled up
 * without smoothing. Each cell is the size of one art pixel on the characters, so the light
 * falls in the same blocky steps as the art instead of looking like a filter laid over it.
 *
 * <p>Bodies add to it every frame through {@link LightSource}. Glows and shadows are drawn
 * behind the sprites, onto the background: each cell takes the background colour under it
 * and adds the glow's light, so glows brighten what's behind them instead of tinting it.
 * Darkness, on levels that have it, is drawn over the sprites with holes where the lights are.
 */
public final class Lighting {

    /** Something that adds glows, lights or a shadow every frame. */
    public interface LightSource {
        void addLight(Lighting lighting);
    }

    /** Height of the ground's surface at a given x, or NaN where there is no ground. */
    public interface Ground {
        float heightAt(float x);
    }

    public static final int CELL = Sprite.LARGE;
    private static final int MARGIN = (ScreenShake.MAX_PIXELS + CELL - 1) / CELL;
    private static final int GLOW_LEVELS = 6;
    private static final int DARK_LEVELS = 6;
    /** Overlapping glows add up, but only to this much, so a cluster of shots doesn't white out. */
    private static final float MAX_GLOW = 0.6f;
    private static final float SHADOW_ALPHA = 0.4f;
    private static final float SHADOW_HALF_HEIGHT = 0.4f;
    /** The shadow sits a little below the contact point, so feet don't cover all of it. */
    private static final float SHADOW_DROP = 0.15f;
    /** Height above the ground, in world units, at which a body's shadow has faded out. */
    private static final float SHADOW_FADE_HEIGHT = 10;

    private final WorldView view;
    private final Ground ground;
    private final float darkness;
    private final int shadeRgb;
    private final int cols;
    private final int rows;
    /** The background's colour in each cell. */
    private final int[] backdrop;

    private final float[] glowR;
    private final float[] glowG;
    private final float[] glowB;
    private final float[] glowA;
    private final float[] shadow;
    private final float[] light;
    private final BufferedImage under;
    private final BufferedImage over;
    private final int[] underPixels;
    private final int[] overPixels;
    private int minCol;
    private int minRow;
    private int maxCol;
    private int maxRow;

    /**
     * @param background the level's background, drawn at the view's top-left corner
     * @param darkness   how dark unlit areas get, from 0 (no darkness layer) to 1
     * @param shade      colour of the darkness
     */
    public Lighting(WorldView view, Image background, int width, int height, Ground ground,
                    float darkness, Color shade) {
        this.view = view;
        this.ground = ground;
        this.darkness = darkness;
        this.shadeRgb = shade.getRGB() & 0xffffff;
        cols = width / CELL + 2 * MARGIN;
        rows = height / CELL + 2 * MARGIN;
        backdrop = sampleCells(background, width, height);
        int cells = cols * rows;
        glowR = new float[cells];
        glowG = new float[cells];
        glowB = new float[cells];
        glowA = new float[cells];
        shadow = new float[cells];
        light = new float[cells];
        under = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_ARGB);
        over = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_ARGB);
        underPixels = ((DataBufferInt) under.getRaster().getDataBuffer()).getData();
        overPixels = ((DataBufferInt) over.getRaster().getDataBuffer()).getData();
    }

    /** Clears everything added last frame. */
    public void begin() {
        Arrays.fill(glowR, 0);
        Arrays.fill(glowG, 0);
        Arrays.fill(glowB, 0);
        Arrays.fill(glowA, 0);
        Arrays.fill(shadow, 0);
        Arrays.fill(light, 0);
        minCol = cols;
        minRow = rows;
        maxCol = -1;
        maxRow = -1;
    }

    /** A coloured glow drawn on the scenery behind the sprites. Strength is its opacity at the centre. */
    public void glow(Vec2 at, float radius, Color colour, float strength) {
        if (strength <= 0) {
            return;
        }
        float r = colour.getRed() / 255f;
        float g = colour.getGreen() / 255f;
        float b = colour.getBlue() / 255f;
        Point2D.Float p = view.worldToView(at);
        float radiusPx = radius * view.getZoom();
        int[] box = cellsAround(p.x, p.y, radiusPx, radiusPx);
        for (int row = box[1]; row <= box[3]; row++) {
            for (int col = box[0]; col <= box[2]; col++) {
                float a = strength * falloff(col, row, p.x, p.y, radiusPx);
                if (a > 0) {
                    int i = row * cols + col;
                    glowR[i] += a * r;
                    glowG[i] += a * g;
                    glowB[i] += a * b;
                    glowA[i] += a;
                }
            }
        }
        markDirty(box);
    }

    /** Lifts the darkness around a point. Does nothing on levels without darkness. */
    public void light(Vec2 at, float radius, float strength) {
        if (darkness <= 0 || strength <= 0) {
            return;
        }
        Point2D.Float p = view.worldToView(at);
        float radiusPx = radius * view.getZoom();
        int[] box = cellsAround(p.x, p.y, radiusPx, radiusPx);
        for (int row = box[1]; row <= box[3]; row++) {
            for (int col = box[0]; col <= box[2]; col++) {
                light[row * cols + col] += strength * falloff(col, row, p.x, p.y, radiusPx);
            }
        }
    }

    /**
     * An oval shadow on the ground below a body. It shrinks and fades as the body rises.
     *
     * @param bottom y of the body's lowest point
     */
    public void shadow(float x, float bottom, float width) {
        float groundY = ground.heightAt(x);
        if (Float.isNaN(groundY)) {
            return;
        }
        float fade = Math.min(1, 1 - Math.max(0, bottom - groundY) / SHADOW_FADE_HEIGHT);
        if (fade <= 0) {
            return;
        }
        Point2D.Float p = view.worldToView(new Vec2(x, groundY - SHADOW_DROP));
        float halfWidth = width / 2 * (0.6f + 0.4f * fade) * view.getZoom();
        float halfHeight = SHADOW_HALF_HEIGHT * view.getZoom();
        int[] box = cellsAround(p.x, p.y, halfWidth, halfHeight);
        float alpha = SHADOW_ALPHA * fade;
        for (int row = box[1]; row <= box[3]; row++) {
            for (int col = box[0]; col <= box[2]; col++) {
                float dx = (centreX(col) - p.x) / halfWidth;
                float dy = (centreY(row) - p.y) / halfHeight;
                if (dx * dx + dy * dy <= 1) {
                    int i = row * cols + col;
                    shadow[i] = Math.max(shadow[i], alpha);
                }
            }
        }
        markDirty(box);
    }

    /** Draws the shadows and glows. Call after the background and before the bodies. */
    public void paintUnder(Graphics2D g) {
        if (maxCol < 0) {
            return;
        }
        for (int row = minRow; row <= maxRow; row++) {
            for (int col = minCol; col <= maxCol; col++) {
                int i = row * cols + col;
                float s = shadow[i];
                float total = glowA[i];
                float a = total == 0 ? 0 : quantize(Math.min(MAX_GLOW, total), GLOW_LEVELS);
                if (s == 0 && a == 0) {
                    underPixels[i] = 0;
                    continue;
                }
                // Darken the background for the shadow, then add the glow's light with a
                // screen blend: 1 - (1 - background) * (1 - light).
                int bg = backdrop[i];
                float k = a == 0 ? 0 : a / total;
                underPixels[i] = 0xff000000
                        | screen((bg >> 16 & 0xff) * (1 - s), glowR[i] * k) << 16
                        | screen((bg >> 8 & 0xff) * (1 - s), glowG[i] * k) << 8
                        | screen((bg & 0xff) * (1 - s), glowB[i] * k);
            }
        }
        draw(g, under, minCol, minRow, maxCol + 1, maxRow + 1);
    }

    /** Screen blend of a colour channel (0 to 255) with an amount of light (0 to 1). */
    private static int screen(float base, float light) {
        float out = 255 - (255 - base) * (1 - Math.min(1, light));
        return Math.max(0, Math.min(255, Math.round(out)));
    }

    /** The background's colour at the centre of each cell, clamped to the image at the margins. */
    private int[] sampleCells(Image background, int width, int height) {
        BufferedImage copy = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = copy.createGraphics();
        g.drawImage(background, 0, 0, null);
        g.dispose();
        int[] colours = new int[cols * rows];
        for (int row = 0; row < rows; row++) {
            int y = Math.max(0, Math.min(height - 1, (int) centreY(row)));
            for (int col = 0; col < cols; col++) {
                int x = Math.max(0, Math.min(width - 1, (int) centreX(col)));
                colours[row * cols + col] = copy.getRGB(x, y);
            }
        }
        return colours;
    }

    /** Draws the darkness, if the level has any. Call after the bodies. */
    public void paintOver(Graphics2D g) {
        if (darkness <= 0) {
            return;
        }
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int i = row * cols + col;
                float unlit = 1 - Math.min(1, light[i]);
                float alpha = darkness * quantize(unlit, DARK_LEVELS);
                overPixels[i] = ((int) (alpha * 255) << 24) | shadeRgb;
            }
        }
        draw(g, over, 0, 0, cols, rows);
    }

    private void draw(Graphics2D g, BufferedImage image, int col0, int row0, int col1, int row1) {
        Object saved = g.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        int x0 = (col0 - MARGIN) * CELL;
        int y0 = (row0 - MARGIN) * CELL;
        int x1 = (col1 - MARGIN) * CELL;
        int y1 = (row1 - MARGIN) * CELL;
        g.drawImage(image, x0, y0, x1, y1, col0, row0, col1, row1, null);
        if (saved != null) {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, saved);
        }
    }

    /** Grid cells within a box around a point, clipped to the grid: {col0, row0, col1, row1}. */
    private int[] cellsAround(float x, float y, float halfWidth, float halfHeight) {
        return new int[]{
                Math.max(0, (int) Math.floor((x - halfWidth) / CELL) + MARGIN),
                Math.max(0, (int) Math.floor((y - halfHeight) / CELL) + MARGIN),
                Math.min(cols - 1, (int) Math.floor((x + halfWidth) / CELL) + MARGIN),
                Math.min(rows - 1, (int) Math.floor((y + halfHeight) / CELL) + MARGIN)};
    }

    private void markDirty(int[] box) {
        if (box[0] > box[2] || box[1] > box[3]) {
            return;
        }
        minCol = Math.min(minCol, box[0]);
        minRow = Math.min(minRow, box[1]);
        maxCol = Math.max(maxCol, box[2]);
        maxRow = Math.max(maxRow, box[3]);
    }

    private static float centreX(int col) {
        return (col - MARGIN + 0.5f) * CELL;
    }

    private static float centreY(int row) {
        return (row - MARGIN + 0.5f) * CELL;
    }

    /** Smooth falloff from 1 at the centre to 0 at the radius. */
    private static float falloff(int col, int row, float x, float y, float radius) {
        float dx = centreX(col) - x;
        float dy = centreY(row) - y;
        float d2 = (dx * dx + dy * dy) / (radius * radius);
        if (d2 >= 1) {
            return 0;
        }
        float f = 1 - d2;
        return f * f;
    }

    /** Rounds to one of a few levels, so light falls off in flat bands like hand-shaded pixel art. */
    private static float quantize(float value, int levels) {
        return Math.min(1, Math.round(value * levels) / (float) levels);
    }
}
