package gameControllers;

import city.cs.engine.SoundClip;
import game.Game;
import game.levels.Button;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class storyButtonController implements MouseListener, ActionListener {
    Button storyButton;
    Timer clickTimer;
    public SoundClip buttonSound,clickSound;
    Game game;
    public storyButtonController(Button storyButton, Game game) {
        this.game = game;
        this.storyButton = storyButton;
        try {

            buttonSound = new SoundClip("data/music/clickMouse.wav");
            clickSound = new SoundClip("data/music/playendless.wav");

        }
        catch (Exception e)
        {
            System.out.println("ananas");
        }
    }
    public void updateButton(Button sb)
    {
        storyButton=sb;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        clickSound.play();
        storyButton.changeImage(storyButton.buttonClicked);
        clickTimer = new Timer(250, this);
        clickTimer.start();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        clickSound.play();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        buttonSound.play();
        storyButton.changeImage(storyButton.getButtonMiceOnImage());
    }

    @Override
    public void mouseExited(MouseEvent e) {
        storyButton.changeImage(storyButton.getButtonImage());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==clickTimer) {
            clickTimer.stop();
            game.startStoryMode();
        }
    }
}
