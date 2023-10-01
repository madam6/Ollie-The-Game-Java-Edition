package game.levels;

import city.cs.engine.*;
import city.cs.engine.Shape;
import game.Game;
import game.GameView;
import gameCharacters.PlaneEnemy;
import gameCharacters.RonaldEnemy;
import gameControllers.MassAttackTaken;
import gameControllers.RainbowTaken;
import gameControllers.dashTaken;
import gameControllers.healthTaken;
import levelItems.*;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;
/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description Second level of my game. Contains level`s music, enemy(Plane)
 * level items and collectibles for this level.
 * and timers etc.
 */
public class Level2 extends GameLevel {
    SoundClip background;
    GameView view;
    public static int RonaldCount = 2;
    private Image anim = new ImageIcon("data/level2Anim..gif").getImage();
    private Image SecondLevelBackground = new ImageIcon("data/nextBackGround-export.png").getImage();
    public Level2(Game game, GameView view)
    {
        super(game);
        super.setRegularView(new GameView(this, 800, 800, getPlayer(), SecondLevelBackground, game));
        this.view = view;


    }
    /**
     * Level that actually starts current level.
     * <p>
     * This method initiates level instead of constructor(sets all platforms, timers, items)
     * for the sake of animation before actually going to this level.
     * @param
     * @return void.
     */
    public void Level2Starter()
    {
        view.setBackground(SecondLevelBackground);

        super.dashSpawnTime = new Random().nextInt(5000, 10000);
        super.dashSpawnerTimer = new Timer(dashSpawnTime, this);
        super.healthSpawnTime = new Random().nextInt(15000, 20000);
        super.healthSpawnerTimer = new Timer(healthSpawnTime, this);
        super.ronaldSpawnTime = new Random().nextInt(4000, 10000);
        super.ronaldSpawnerTimer = new Timer(ronaldSpawnTime, this);
        super.rainbowSpawnTime = new Random().nextInt(3000, 5000);
        super.rainbowSpawnerTimer = new Timer(rainbowSpawnTime, this);
        super.massAttackSpawnTime = new Random().nextInt(5000, 10000);
        super.massAttackSpawnerTimer = new Timer(massAttackSpawnTime, this);
        super.massAttackSpawnerTimer.setInitialDelay(1000);
        try {
            background = new SoundClip("data/music/level23.wav");
            background.setVolume(0.25f);
            super.backgoundMusic = background;
            super.backgoundMusic.loop();
        }
        catch (Exception e1)
        {

        }
        super.ronaldSpawnerTimer.setInitialDelay(1000);


        super.ronaldSpawnerTimer.start();
        super.dashSpawnerTimer.start();
        super.healthSpawnerTimer.start();
        super.rainbowSpawnerTimer.start();
        super.massAttackSpawnerTimer.start();

        super.groundImage = new BodyImage("data/dummyImage.png");

        Shape shape = new BoxShape(22, 2f);
        StaticBody ground = new StaticBody(this, shape);
        ground.setPosition(new Vec2(0f, -18.5f));
        ground.addImage(groundImage);


        Barrel b1 = new Barrel(this, new Vec2(15, -12));
        Barrel b2 = new Barrel(this, new Vec2(15, -7));
        Barrel b3 = new Barrel(this, new Vec2(-20, -12));
        Barrel b4 = new Barrel(this, new Vec2(-13, -12));

        invisibleWall wall1 = new invisibleWall(this);
        invisibleWall wall2 = new invisibleWall(this);
        wall1.setPosition(new Vec2(-23, 0));
        wall2.setPosition(new Vec2(23, 0));
    }
    /**
     * Spawning actionPerformed method.
     * <p>
     * Spawns enemies, collectables depending on timers.
     * @param
     * @return void.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(RonaldCount>0) {
            if (e.getSource() == ronaldSpawnerTimer && !getPlayer().isDead && !getPlayer().isCompletedLevel) {
                RonaldEnemy ronald = new RonaldEnemy(this, 25, super.getPlayer());
                this.addStepListener(ronald);
                ronaldSpawnTime -= 250;
                RonaldCount--;
            }
        }
        else
        {
            ronaldSpawnerTimer.stop();
        }
        if(e.getSource()==dashSpawnerTimer && !getPlayer().isDead && !getPlayer().isCompletedLevel)
        {
            DashToken dashToken = new DashToken(this);
            dashTaken taken = new dashTaken(dashToken);
            dashToken.addCollisionListener(taken);
        }
        if(e.getSource()==healthSpawnerTimer && !getPlayer().isDead && !getPlayer().isCompletedLevel)
        {
            HealthToken healthToken = new HealthToken(this);
            healthTaken taken = new healthTaken(healthToken);
            healthToken.addCollisionListener(taken);
        }
        if(e.getSource()==rainbowSpawnerTimer && !getPlayer().isDead && !getPlayer().isCompletedLevel)
        {
            RainbowToken rainbowToken = new RainbowToken(this);
            RainbowTaken taken = new RainbowTaken(rainbowToken);
            rainbowToken.addCollisionListener(taken);
        }
        if(e.getSource()==massAttackSpawnerTimer  && !getPlayer().isDead && !getPlayer().isCompletedLevel)
        {

            MassAttackToken massAttackToken = new MassAttackToken(this);
            MassAttackTaken taken = new MassAttackTaken(massAttackToken, this);
            massAttackToken.addCollisionListener(taken);
        }

    }

    @Override
    public void preStep(StepEvent stepEvent) {

    }

    @Override
    public void postStep(StepEvent stepEvent) {
        if(getPlayer().isCompletedLevel || getPlayer().isDead)
        {

        }
        super.postStep(stepEvent);
    }
}
