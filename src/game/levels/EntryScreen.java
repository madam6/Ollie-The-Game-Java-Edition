package game.levels;

import city.cs.engine.Shape;
import city.cs.engine.*;
import game.Game;
import gameControllers.backButtonController;
import gameControllers.endlessButtonController;
import gameControllers.storyButtonController;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description The EntryScreen class represents the first screen of the game.
 * This screen contains a play button and other buttons for different levels of the game.
 * It also is a mouseListener for a play button.
 * The class extends the UILevel class and implements the MouseListener and ActionListener interfaces.
 */
public class EntryScreen extends UILevel implements MouseListener, ActionListener {

    protected static Image background = new ImageIcon("data/backgroundnightanim.gif").getImage();
    public BodyImage buttonImage = new BodyImage("data/playButton.png", 12);
    public BodyImage buttonMiceOnImage = new BodyImage("data/playButtonOnMounce.png", 12);
    public BodyImage buttonClicked = new BodyImage("data/playButtonClicked.png", 12);
    public BodyImage stroyI = new BodyImage("data/storyB.png", 12);
    public BodyImage stroyIM = new BodyImage("data/storyBM.png", 12);
    public BodyImage stroyIC = new BodyImage("data/storyBC.png", 12);
    public BodyImage endlessI = new BodyImage("data/endlessB.png", 12);
    public BodyImage endlessIM = new BodyImage("data/endlessBM.png", 12);
    public BodyImage endlessIC = new BodyImage("data/endlessBC.png", 12);
    public BodyImage backI = new BodyImage("data/backB.png", 12);
    public BodyImage backIM = new BodyImage("data/backBM.png", 12);
    public BodyImage backIC = new BodyImage("data/backBC.png", 12);
    protected Shape buttonShape = new BoxShape(2.5f, 1);
    public Button playButton;

    public StaticBody playButtonBody = new StaticBody(this);
    public static BodyImage label = new BodyImage("data/ollieTheGame.png", 20);
    public StaticBody labelBody = new StaticBody(this);
    StaticBody storyBody;
    StaticBody backBody;
    StaticBody endlessBody;
    public Button story;
    public Button endless;
    public Button back;
    Game game;
    Timer clickTimer;
    public storyButtonController sbc;
    public endlessButtonController ebc;
    public backButtonController bbc;

    public SoundClip menuMusic, buttonSound,clickSound;

    /**
     * Constructor for entry screen.
     * <p>
     * Creates a new entry screen with all the sounds, and buttons needed for this
     * entry screen
     * @param  game
     * @return void.
     */
    public EntryScreen(Game game) {
        super(game);
        this.game = game;
        try {
            menuMusic  = new SoundClip("data/music/menu.wav");
            buttonSound = new SoundClip("data/music/clickMouse.wav");
            clickSound = new SoundClip("data/music/playendless.wav");
            menuMusic.loop();
        }
        catch (Exception e)
        {

        }
        playButton = new Button(
                buttonImage,
                buttonMiceOnImage,
                buttonClicked,
                playButtonBody,218,415,355,83);

        labelBody.setPosition(new Vec2(0, 5));
        labelBody.addImage(label);
        storyBody = new StaticBody(this);
        backBody = new StaticBody(this);
        endlessBody = new StaticBody(this);
        story = new Button(stroyI, stroyIM, stroyIC, storyBody, 218,415,355,83);
        endless = new Button(endlessI, endlessIM, endlessIC,endlessBody, 218,515,355,83);
        back = new Button(backI, backIM, backIC, backBody, 218,615,355,83);
        sbc = new storyButtonController(story, game);
        ebc = new endlessButtonController(endless, game);
        bbc = new backButtonController(back, endless, story, playButton, game);
        story.setVisible(false);
        endless.setVisible(false);
        back.setVisible(false);
        playButton.setPosition(new Vec2(0, -3));
        playButton.addImage(buttonImage);

    }

    public static Image getBackground() {
        return background;
    }

    public void setBackground(Image background) {
        this.background = background;
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

    @Override
    public void mouseClicked(MouseEvent e) {
        clickSound.play();
        playButton.changeImage(playButton.buttonClicked);
        clickTimer = new Timer(250, this);
        clickTimer.setRepeats(false);
        clickTimer.start();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(playButton.isVisible()) {

            playButton.changeImage(playButton.buttonClicked);

            mouseClicked(e);
        }
        else
        {

        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(playButton.isVisible()) {
            clickSound.play();
            playButton.changeImage(playButton.buttonImage);
        }
        else
        {

        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(playButton.isVisible()) {
            System.out.println("test1");
            buttonSound.play();

            playButton.changeImage(playButton.getButtonMiceOnImage());
        }
        else
        {

        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if(playButton.isVisible()) {
            System.out.println("test2");

            playButton.changeImage(playButton.getButtonImage());
        }
        else
        {

        }
    }
    /**
     * Proceed further after clicking play.
     * <p>
     * In fact delets play button from the screen, adds new
     * buttons with all their respective controllers/pictures.
     * @param  e - ActionEvent. In fact timer which allows to play buttons animations.
     * @return void.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==clickTimer) {
            clickTimer.stop();

            playButton.getButtonsEntity().removeAllImages();
            playButton.setVisible(false);


            story.setVisible(true);
            endless.setVisible(true);
            back.setVisible(true);

            story.setPosition(new Vec2(0, -3));
            story.addImage(stroyI);
            back.setPosition(new Vec2(0, -13));
            back.addImage(backI);
            endless.setPosition(new Vec2(0, -8));
            endless.addImage(endlessI);


            story.addMouseListener(sbc);
            System.out.println("clicksheck");




            endless.addMouseListener(ebc);





            back.addMouseListener(bbc);

        }
    }
}
