package gameCharacters;

import bullets.missileBullet;
import city.cs.engine.*;
import gameControllers.MissileBulletImpact;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description PlaneEnemy class.
 */
public class PlaneEnemy extends Enemy implements ActionListener {

    protected static PolygonShape planeShape = new PolygonShape(0.03f,-1.54f, 5.6f,0.45f, 1.88f,3.33f, -4.9f,0.31f);

    private static BodyImage planeDamagedLeft = new BodyImage("data/plandeDamagedLeft.png", 7);
    private static BodyImage planeDamagedRight = new BodyImage("data/plandeDamagedRight.png", 7);

    private BodyImage planeLeft = new BodyImage("data/planeLeft.gif", 7);
    private BodyImage planeRight = new BodyImage("data/planeRight.gif", 7);

    public Timer fireTimer = new Timer(3000, this);

    private missileBullet bullet;

    private Player player;

    World gameWorld;

    private int HP;
    public static SoundClip flying, attack, damaged, died;


    public PlaneEnemy(World world, int healthPoints, Player player)
    {
        super(world, planeShape, healthPoints, planeDamagedLeft, planeDamagedRight, player, 0);
        super.attackTimer = fireTimer;
        gameWorld = world;
        this.player = player;
        HP = healthPoints;
        addImage(planeLeft);
        spawner();
        super.attackTimer.start();
        try {
            flying = new SoundClip("data/music/planeFlying.wav");
            attack = new SoundClip("data/music/planeAttack.wav");
            damaged = new SoundClip("data/music/planeDamaged.wav");
            died = new SoundClip("data/music/planeDied.wav");

            super.deadSound = died;
            super.mySound = flying;
            super.atackSound = attack;
            super.damagedSound=damaged;
            super.mySound.setVolume(0.25f);
            super.mySound.loop();
        }
        catch (Exception e)
        {

        }

    }
    public void stop()
    {
        super.mySound.stop();
        super.attackTimer.stop();
        super.atackSound.stop();
    }

    @Override
    public void postStep(StepEvent stepEvent) {
        if(player.isDead|| player.isCompletedLevel)
        {
            stop();
        }
        super.postStep(stepEvent, 8, planeRight, planeLeft);
        if(!super.isAlive)
        {
            fireTimer.stop();
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        missileBullet missile = new missileBullet(gameWorld, this.getPosition()
                ,new Vec2(-(this.getPosition().x-player.getPosition().x),
                -(this.getPosition().y-player.getPosition().y)), 50);
        MissileBulletImpact mbi = new MissileBulletImpact(missile);
        missile.addCollisionListener(mbi);
        gameWorld.addStepListener(missile);
        super.atackSound.play();
    }
}
