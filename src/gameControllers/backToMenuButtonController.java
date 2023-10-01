package gameControllers;

import city.cs.engine.SoundClip;
import game.Game;
import game.GameView;
import game.levels.Button;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class backToMenuButtonController implements MouseListener, ActionListener {
    Button retryButtom;
    Timer clickTimer;
    SoundClip buttonSound, clickSound;
    Game game;
    GameView gameView;
    public backToMenuButtonController(Button rb, Game game, GameView gameView) {
        this.game = game;
        this.retryButtom = rb;
        this.gameView = gameView;
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
        retryButtom.changeImage(retryButtom.buttonClicked);
        clickTimer = new Timer(250, this);
        clickTimer.start();
    }
    public void updateButton(Button eb)
    {
        retryButtom =eb;
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
        retryButtom.changeImage(retryButtom.getButtonMiceOnImage());
    }

    @Override
    public void mouseExited(MouseEvent e) {
        retryButtom.changeImage(retryButtom.getButtonImage());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==clickTimer) {
            clickTimer.stop();
            game.backToMenu();
        }
    }
}
