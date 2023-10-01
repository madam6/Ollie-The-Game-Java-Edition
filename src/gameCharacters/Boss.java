package gameCharacters;

import bullets.SuperBulletEnemy;
import bullets.burgerBullet;
import bullets.missileBullet;
import city.cs.engine.*;
import gameControllers.BurgerBulletImpact;
import gameControllers.MissileBulletImpact;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description Class for final boss.
 */
public class Boss extends Enemy implements ActionListener {
    private static Shape bossShape = new PolygonShape(0.58f,0.85f, 3.3f,1.9f, 1.16f,5.1f, -1.46f,4.93f, -3.77f,2.62f, -1.6f,1.16f);

    private static BodyImage bossDamaged1 = new BodyImage("data/bossdamaged.gif", 17);
    private static BodyImage bossDamaged2 = new BodyImage("data/bosssecondPhaseDamaged.gif", 17);
    private static BodyImage bossDamaged3 = new BodyImage("data/bossThirdPhaseDamage.gif", 17);


    private BodyImage boss1 = new BodyImage("data/boss.gif", 17);
    private BodyImage boss2 = new BodyImage("data/bosssecondPhase.gif", 17);
    private BodyImage boss3 = new BodyImage("data/bossThirdPhase.gif", 17);

    public static SoundClip flying, attack1, attack2, attack3, damaged1, died;

    Timer fireTimer = new Timer(1000, this);
    Timer fireTimer1 = new Timer(1000, this);
    Timer fireTimer2 = new Timer(1000, this);
    World gameWorld;
    Vec2 posBefore;
    public int hp;
    public int stage = 1;
    public Boss(World world, int healthPoints, Player player)
    {

        super(world, bossShape, healthPoints, bossDamaged1, bossDamaged1, player, 1);
        hp = healthPoints;
        gameWorld = world;
        addImage(boss1);
        spawner();
        fireTimer.start();
        try {
            flying = new SoundClip("data/music/ronaldFlying.wav");
            attack1 = new SoundClip("data/music/planeAttack.wav");
            attack2 = new SoundClip("data/music/attackRonald.wav");
            attack3 = new SoundClip("data/music/suitAttack.wav");
            damaged1 = new SoundClip("data/music/bossDamaged1.wav");
            died = new SoundClip("data/music/ronaldDead.wav");

            super.deadSound = died;
            super.mySound = flying;
            super.atackSound = attack1;
            super.damagedSound = damaged1;
            super.mySound.setVolume(2);
            super.mySound.loop();
        }
        catch (Exception e)
        {

        }

    }
    /**
     * Appropriate animations for different stages of the boss.
     * <p>
     * Method sets respective animations for different boss stages
     * @param
     * @return void.
     */
    @Override
    public void postStep(StepEvent stepEvent) {
        if(stage == 1)
        {
            super.postStep(stepEvent, 6, boss1, boss1);
        }
        if(stage == 2)
        {
            super.postStep(stepEvent, 6, boss2, boss2);

        }
        if(stage == 3)
        {
            super.postStep(stepEvent, 6, boss3, boss3);
        }
        findStage();
        maintainStage();
        if(!super.isAlive)
        {
            fireTimer.stop();
        }
        if(player.isDead || player.isCompletedLevel)
        {
            stop();
        }

    }
    /**
     * Determine the stage of the boss
     * @param
     * @return void.
     */
    public void findStage()
    {
        if(hp>400 && hp<=600)
        {
            stage = 1;
        }
        if(hp>200 && hp<=400)
        {
            stage= 2;
        }
        if(hp>0 && hp<=200)
        {
            stage =3;
        }
    }
    /**
     * Helps to keep correct animations.
     * <p>
     * This method is called by PostStep, and it helps to keep correct animations of the boss depending on its stage.
     * @param
     * @return void.
     */
    public void maintainStage()
    {
        if(stage==1)
        {
            this.damagedAnimationLeft=bossDamaged1;
            this.damagedAnimationRight=bossDamaged1;
        }
        if(stage==2)
        {

            super.damagedAnimationLeft = bossDamaged2;
            super.damagedAnimationRight = bossDamaged2;

        }
        if(stage==3)
        {

            super.damagedAnimationLeft = bossDamaged3;
            super.damagedAnimationRight = bossDamaged3;
        }
    }
    /**
     * Spawns bullets for boss
     * <p>
     * Depending on stage spawns different boss`s bullets.
     * @param
     * @return void.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (stage == 1) {
            if(e.getSource() == fireTimer) {
                missileBullet missile = new missileBullet(gameWorld, this.getPosition(), new Vec2(this.getPosition().x, this.getPosition().y - 20), 180);
                MissileBulletImpact mbi = new MissileBulletImpact(missile);
                missile.addCollisionListener(mbi);
                gameWorld.addStepListener(missile);
                super.atackSound.play();
            }
        }
        if (stage == 2)
        {
                missileBullet missile = new missileBullet(gameWorld, this.getPosition(), new Vec2(this.getPosition().x, this.getPosition().y - 20), 180);
                MissileBulletImpact mbi = new MissileBulletImpact(missile);
                missile.addCollisionListener(mbi);
                gameWorld.addStepListener(missile);
                super.atackSound.play();

                burgerBullet burger = new burgerBullet(gameWorld, this.getPosition(), this);
                attack2.play();
                BurgerBulletImpact bbi = new BurgerBulletImpact(burger);
                burger.addCollisionListener(bbi);
                gameWorld.addStepListener(burger);

        }
        if(stage == 3)
        {

                missileBullet missile = new missileBullet(gameWorld, this.getPosition(), new Vec2(this.getPosition().x, this.getPosition().y - 20), 180);
                MissileBulletImpact mbi = new MissileBulletImpact(missile);
                missile.addCollisionListener(mbi);
                gameWorld.addStepListener(missile);
                super.atackSound.play();
                burgerBullet burger = new burgerBullet(gameWorld, this.getPosition(), this);
                attack2.play();
                BurgerBulletImpact bbi = new BurgerBulletImpact(burger);
                burger.addCollisionListener(bbi);
                gameWorld.addStepListener(burger);

                SuperBulletEnemy superBulletEnemy = new SuperBulletEnemy(gameWorld, this.getPosition());
                attack3.play();

        }
    }
    public void preStep(StepEvent stepEvent) {
        posBefore = new Vec2(this.getPosition());
    }
}
