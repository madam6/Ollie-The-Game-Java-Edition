package gameControllers;

import city.cs.engine.SoundClip;
import gameCharacters.Player;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class playerController implements KeyListener, ActionListener {
    Player player;
    Timer animationTimer;
    int tempCounter = 0;
    boolean canFire = true;
    SoundClip  fire, dash, jump;
    public playerController(Player player)
    {
        this.player = player;
        try {

            fire = new SoundClip("data/music/fire1.wav");
            dash = new SoundClip("data/music/dash.wav");
            jump = new SoundClip("data/music/jump.wav");
            dash.setVolume(2);
            jump.setVolume(1.4f);
        }
        catch (Exception e)
        {

        }
    }
    public void updatePlayer(Player newPlayer)
    {
        this.player = newPlayer;
    }
    public void keyPressed(KeyEvent e) {
        //System.out.println("kp");
        int code = e.getKeyCode();
        String state;
        if(code == KeyEvent.VK_E)
        {
            if(player.ammo>0) {
                animationTimer = new Timer(800, this);
                animationTimer.start();
                player.ammo = player.ammo - 1;

                state = player.getState();
                if (state.equals("flyingIdle") || state.equals("staying") || state.equals("goingRight")
                        || state.equals("DashRight") || state.equals("flyingToTheRight")) {
                    player.setFiring(true);
                }
                if (state.equals("flyingToTheLeft") || state.equals("goingLeft") || state.equals("DashLeft")) {
                    player.setFiring(true);

                }
                canFire = false;
                fire.play();

            }
        }

        if (code == KeyEvent.VK_A)
        {

            player.startWalking(-4);

        }
        if (code == KeyEvent.VK_D)
        {

            player.startWalking(4);

        }
        if(code == KeyEvent.VK_SPACE)
        {

            player.jump(9);
            jump.play();
        }
        if(code == KeyEvent.VK_Q)
        {
            if(player.getAmountOfDashes()>0) {

                if (player.getLinearVelocity().x < 0) {
                    player.startWalking(-14);
                }
                if (player.getLinearVelocity().x > 0) {
                    player.startWalking(14);
                }
                player.setAmountOfDashes(player.getAmountOfDashes()-1);
                dash.play();
            }
        }


    }
    public  void keyTyped(KeyEvent e)
    {
    }
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_A && player.getLinearVelocity().x>0 || player.getLinearVelocity().x<0 )
        {

            player.stopWalking();


        }
        if (code == KeyEvent.VK_D && player.getLinearVelocity().x>0 || player.getLinearVelocity().x<0)
        {

            player.stopWalking();

        }
        if(code == KeyEvent.VK_Q)
        {

            player.stopWalking();

        }
        if(code == KeyEvent.VK_E)
        {

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String state = player.getState();
        if (e.getSource() == animationTimer) {

            if (state.equals("flyingIdle") || state.equals("staying") || state.equals("goingRight")
                    || state.equals("DashRight") || state.equals("flyingToTheRight")) {
                player.raibowFire(true);
            }
            if (state.equals("flyingToTheLeft") || state.equals("goingLeft") || state.equals("DashLeft")) {
                player.raibowFire(false);
            }
            animationTimer.stop();
            player.setFiring(false);
        }

    }

}

