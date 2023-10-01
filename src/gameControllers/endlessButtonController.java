package gameControllers;

import city.cs.engine.SoundClip;
import game.Game;
import game.levels.Button;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class endlessButtonController implements MouseListener, ActionListener {
    Button endlessButton;
    Timer clickTimer;
    SoundClip buttonSound, clickSound;
    Game game;
    public endlessButtonController(Button endlessButton, Game game) {
        this.game = game;
        this.endlessButton = endlessButton;
        try {
            buttonSound = new SoundClip("data/music/clickMouse.wav");
            clickSound = new SoundClip("data/music/playendless.wav");
        }
        catch (Exception e)
        {
            System.out.println("ananas");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        clickSound.play();
        endlessButton.changeImage(endlessButton.buttonClicked);
        clickTimer = new Timer(250, this);
        clickTimer.start();
    }
    public void updateButton(Button eb)
    {
        endlessButton=eb;
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
        endlessButton.changeImage(endlessButton.getButtonMiceOnImage());
    }

    @Override
    public void mouseExited(MouseEvent e) {
        endlessButton.changeImage(endlessButton.getButtonImage());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==clickTimer) {
            clickTimer.stop();
            game.startEndless();
        }
    }
}
