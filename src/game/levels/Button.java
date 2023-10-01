package game.levels;

import city.cs.engine.BodyImage;
import city.cs.engine.Shape;
import city.cs.engine.StaticBody;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.awt.*;
/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description My own button class for this game.
 */
public class Button extends JButton  {
    public BodyImage buttonImage;
    public BodyImage buttonMiceOnImage;
    public BodyImage buttonClicked;
    /**
     * Shape for a static body.
     */
    protected Shape buttonShape;
    /**
     * Static body to which we add buttonImages.
     */
    protected StaticBody buttonsEntity;
    private int width, height;
    /**
     * Constructor for my button.
     * <p>
     * Creates a new JButton, but with a static body to which we attach button images,
     * that we can control with controllers in the future.
     * @param  buttonImage,buttonMiceOnImage,buttonClicked,be,x,y,width,height
     * @return void.
     */
    public Button(BodyImage buttonImage, BodyImage buttonMiceOnImage, BodyImage buttonClicked, StaticBody be, int x, int y, int width, int height)
    {
        setBackground(new Color(255, 255, 255, 0));
        setForeground(new Color(255, 255, 255, 0));
        setBorder(null);
        this.width = width;
        this.height = height;
        this.setBounds(x, y, width, height);
        this.buttonImage = buttonImage;
        this.buttonMiceOnImage = buttonMiceOnImage;
        this.buttonClicked = buttonClicked;
        this.buttonShape = buttonShape;
        buttonsEntity = be;
    }

    public void setPosition(Vec2 position)
    {

        buttonsEntity.setPosition(position);
    }
    public void addImage(BodyImage image)
    {
        buttonsEntity.addImage(image);
    }
    /**
     * Method for changing image of a button.
     * <p>
     * Removes all previous images and attaches new.
     * @param  newImage
     * @return void.
     */
    public void changeImage(BodyImage newImage)
    {
        buttonsEntity.removeAllImages();
        buttonsEntity.addImage(newImage);
    }

    public BodyImage getButtonImage() {
        return buttonImage;
    }

    public void setButtonImage(BodyImage buttonImage) {
        this.buttonImage = buttonImage;
    }

    public BodyImage getButtonMiceOnImage() {
        return buttonMiceOnImage;
    }

    public void setButtonMiceOnImage(BodyImage buttonMiceOnImage) {
        this.buttonMiceOnImage = buttonMiceOnImage;
    }

    public BodyImage getButtonClicked() {
        return buttonClicked;
    }

    public void setButtonClicked(BodyImage buttonClicked) {
        this.buttonClicked = buttonClicked;
    }

    public Shape getButtonShape() {
        return buttonShape;
    }

    public void setButtonShape(Shape buttonShape) {
        this.buttonShape = buttonShape;
    }

    public StaticBody getButtonsEntity() {
        return buttonsEntity;
    }

    public void setButtonsEntity(StaticBody buttonsEntity) {
        this.buttonsEntity = buttonsEntity;
    }


    public void setForeground(int i, int i1, int i2, int i3) {
        super.setForeground(new Color(i, i1, i2, i3));
    }

    public void setBackground(int i, int i1, int i2, int i3) {
        super.setBackground(new Color(i, i1, i2, i3));
    }
}
