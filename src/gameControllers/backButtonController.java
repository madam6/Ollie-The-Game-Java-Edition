package gameControllers;

import city.cs.engine.SoundClip;
import game.Game;
import game.levels.Button;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description All game controllers are kind the same. Whether it is button controller
 * that changes pictures for button or for example a player controller that allows player to move
 * or a collectable controller that does the needed things after the collectable was picked up or
 * bullet controller that manages appropriate damages. They all kind of the same, therefore in this documentation
 * I will not describe them.
 */
public class backButtonController implements MouseListener, ActionListener {
    Button backButton;
    Button endless;
    Button story;
    Button play;
    Timer clickTimer;
    Game game;
    SoundClip buttonSound, clickSound;
    public backButtonController(Button backButton, Button endless, Button story, Button play, Game game) {
        this.backButton = backButton;
        this.endless = endless;
        this.story = story;
        this.play = play;
        this.game = game;
        try {
            buttonSound = new SoundClip("data/music/clickMouse.wav");
            clickSound = new SoundClip("data/music/playendless.wav");
        }
        catch (Exception e)
        {
            System.out.println("ananas");
        }
    }
    public void updateButton(Button bb, Button sb, Button eb, Button play)
    {
        backButton=bb;
        story=sb;
        endless=eb;
        this.play=play;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        clickSound.play();
        backButton.changeImage(backButton.buttonClicked);
        clickTimer = new Timer(250, this);
        clickTimer.start();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        backButton.changeImage(backButton.buttonClicked);

        mouseClicked(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        clickSound.play();
        backButton.changeImage(backButton.buttonImage);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        buttonSound.play();
        backButton.changeImage(backButton.getButtonMiceOnImage());
    }

    @Override
    public void mouseExited(MouseEvent e) {
        backButton.changeImage(backButton.getButtonImage());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==clickTimer) {
            clickTimer.stop();
            story.setVisible(false);
            endless.setVisible(false);
            backButton.setVisible(false);
            game.resetEntryScreen();

        }
    }
}
