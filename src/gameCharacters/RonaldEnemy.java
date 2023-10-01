package gameCharacters;

import bullets.burgerBullet;
import city.cs.engine.*;
import gameControllers.BurgerBulletImpact;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description RonaldEnemy class.
 */
public class RonaldEnemy extends Enemy implements ActionListener {
    private static Shape ronaldShape = new PolygonShape(-0.28f,-0.71f, 3.11f,0.03f, -1.67f,2.27f, -3.33f,0.08f);

    private static BodyImage ronaldLDamaged = new BodyImage("data/ronaldLDamaged.gif", 7);
    private static BodyImage ronaldRDamaged = new BodyImage("data/ronaldRDamaged.gif", 7);

    private BodyImage ronaldL = new BodyImage("data/ronaldL.gif", 7);
    private BodyImage ronaldR = new BodyImage("data/ronaldR.gif", 7);
    public static SoundClip flying, attack, damaged, died;

    Timer fireTimer = new Timer(1000, this);
    public boolean initialDirection;
    World gameWorld;
    Vec2 posBefore;

    public RonaldEnemy(World world, int healthPoints, Player player)
    {
        super(world, ronaldShape, healthPoints, ronaldLDamaged, ronaldRDamaged, player, 1);
        super.attackTimer = fireTimer;

        gameWorld = world;
        addImage(ronaldL);
        spawner();
        super.attackTimer.start();
        try {
            flying = new SoundClip("data/music/ronaldFlying.wav");
            attack = new SoundClip("data/music/attackRonald.wav");
            damaged = new SoundClip("data/music/ronaldDamaged.wav");
            died = new SoundClip("data/music/ronaldDead.wav");

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
    @Override
    public void postStep(StepEvent stepEvent) {
        if(this.getPosition().x-posBefore.x<0)
        {
            initialDirection = false;
        }
        else
        {
            initialDirection = true;
        }
        if(!super.isAlive)
        {
            fireTimer.stop();
        }
        if(player.isDead || player.isCompletedLevel)
        {
            stop();
        }
        super.postStep(stepEvent, 10, ronaldR, ronaldL);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        burgerBullet burger = new burgerBullet(gameWorld, this.getPosition(), this);
        super.atackSound.play();
        BurgerBulletImpact bbi = new BurgerBulletImpact(burger);
        burger.addCollisionListener(bbi);
        gameWorld.addStepListener(burger);
    }
    public void preStep(StepEvent stepEvent) {
        posBefore = new Vec2(this.getPosition());
    }

}
