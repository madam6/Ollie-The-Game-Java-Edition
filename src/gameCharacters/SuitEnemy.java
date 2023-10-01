package gameCharacters;

import bullets.SuperBulletEnemy;
import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description SuitEnemy class
 */
public class SuitEnemy extends Enemy implements ActionListener {
    private static Shape suitShape = new PolygonShape(2.41f,-0.9f, 0.87f,2.58f, -1.51f,2.37f, -2.76f,-1.75f, -0.38f,-2.88f);

    private static BodyImage suitDamaged = new BodyImage("data/suitLeftDamaged.gif", 7);
    private static BodyImage suitDamagedR = new BodyImage("data/suitDamaged.gif", 7);

    private BodyImage suitL = new BodyImage("data/suitRight.gif", 7);
    private BodyImage suitR = new BodyImage("data/suitLeft.gif", 7);
    private BodyImage suitAttack = new BodyImage("data/suitAttacck.gif", 7);
    public static SoundClip flying, attack, damaged, died;

    Timer fireTimer = new Timer(4000, this);

    World gameWorld;
    Vec2 posBefore;
    boolean isFiring = false;
    private Timer firingTimer = new Timer(300, this);
    public SuitEnemy(World world, int healthPoints, Player player) {
        super(world, suitShape, healthPoints, suitDamaged, suitDamagedR, player, 2);
        super.attackTimer = fireTimer;

        gameWorld = world;
        addImage(suitL);
        spawner();
        super.attackTimer.start();
        try {
            flying = new SoundClip("data/music/suitSound.wav");
            attack = new SoundClip("data/music/suitAttack.wav");
            damaged = new SoundClip("data/music/suitDamaged.wav");
            died = new SoundClip("data/music/suitDied.wav");
            //flying.setVolume(0.5f);

            super.deadSound = died;
            super.mySound = flying;

            super.atackSound = attack;
            super.damagedSound = damaged;
            super.mySound.setVolume(2);
            super.mySound.loop();
        }
        catch (Exception e)
        {

        }
    }

    public boolean isDamged() {
        return isFiring;
    }

    public void setFiring(boolean firing) {
        isFiring = firing;
    }

    public SuitEnemy(World world, Shape shape, int healthPoints, BodyImage damagedAnimationLeft, BodyImage damagedAnimationRight, Player player, int id) {
        super(world, shape, healthPoints, damagedAnimationLeft, damagedAnimationRight, player, id);
    }

    @Override
    public void postStep(StepEvent stepEvent) {
        if(!super.isAlive)
        {
            fireTimer.stop();
        }
        if(player.isDead || player.isCompletedLevel)
        {
            stop();
        }
        if(isFiring)
        {
            changeAnimation(suitAttack);
            firingTimer.start();
        }
        super.postStep(stepEvent, 6, suitL, suitR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == super.attackTimer) {
            SuperBulletEnemy superBulletEnemy = new SuperBulletEnemy(gameWorld, this.getPosition());
            isFiring = true;
            super.atackSound.play();
        }
        if(e.getSource() == firingTimer)
        {
            isFiring = false;
            firingTimer.stop();
        }

    }
}
