package game.levels;

import city.cs.engine.Shape;
import city.cs.engine.World;
import game.Game;

import java.awt.*;
import java.util.List;
/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description Сlass for inheritance that extends worlds and in fact is used
 * for all levels with UI.
 */
public class UILevel extends World {

    protected Image background;
    protected List<Shape> buttonShapes;
    protected List<Image> buttonImages;
    protected List<Image> buttonClickedImage;
    protected List<Image> buttonMiceOnImage;
    Game game;


    public UILevel(Game game, Image background, List<Shape> buttonShapes, List<Image> buttonImages, List<Image> buttonClickedImage, List<Image> buttonMiceOnImage) {
        this.game = game;
        this.background = background;
        this.buttonShapes = buttonShapes;
        this.buttonImages = buttonImages;
        this.buttonClickedImage = buttonClickedImage;
        this.buttonMiceOnImage = buttonMiceOnImage;
    }
    public UILevel(Game game)
    {
        this.game = game;
    }


}
