package ollie.ui;

import ollie.Game;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

/** A full-window picture, used for the intro screens and the animations between levels. */
public class ImagePanel extends JPanel {

    private final Image image;

    public ImagePanel(Image image) {
        this.image = image;
        setPreferredSize(new Dimension(Game.WIDTH, Game.HEIGHT));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }
}
