package game.levels;

import city.cs.engine.Shape;
import city.cs.engine.*;
import game.Game;
import game.GameView;
import gameCharacters.Boss;
import gameControllers.*;
import levelItems.*;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;
/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description Final level of my game. Contains level`s music, enemy(Plane)
 * level items and collectibles for this level.
 * and timers etc.
 */
public class FinalLevel extends GameLevel {
    SoundClip background;
    GameView view;
    Timer specialMassAttackTimer = new Timer(5000, this);
    public Boss boss;
    private Image anim = new ImageIcon("data/toTheFinalLevel.gif").getImage();
    private Image SecondLevelBackground = new ImageIcon("data/finalBackground-export.png").getImage();

    SoundClip victory;
    public FinalLevel(Game game, GameView view) {
        super(game);
        super.setRegularView(new GameView(this, 800, 800, getPlayer(), SecondLevelBackground, game));
        this.view = view;
        try {
            victory = new SoundClip("data/music/victory!.wav");
            super.victory = victory;
        } catch (Exception e)
        {

        }

    }
    /**
     * Level that actually starts current level.
     * <p>
     * This method initiates level instead of constructor(sets all platforms, timers, items)
     * for the sake of animation before actually going to this level.
     * @param
     * @return void.
     */
    public void FinalLevelStarter()
    {
        view.setBackground(SecondLevelBackground);


        super.dashSpawnTime = new Random().nextInt(15000, 15001);
        super.dashSpawnerTimer = new Timer(dashSpawnTime, this);
        super.dashSpawnerTimer.setInitialDelay(1000);

        super.healthSpawnTime = new Random().nextInt(15000, 15001);
        super.healthSpawnerTimer = new Timer(healthSpawnTime, this);
        super.healthSpawnerTimer.setInitialDelay(2000);


        super.rainbowSpawnTime = new Random().nextInt(15000, 15001);
        super.rainbowSpawnerTimer = new Timer(rainbowSpawnTime, this);
        super.rainbowSpawnerTimer.setInitialDelay(3000);

        super.superJumpTime = new Random().nextInt(15000, 15001);
        super.superJumpTimer = new Timer(superJumpTime, this);
        super.superJumpTimer.setInitialDelay(4000);


        super.massAttackSpawnTime = new Random().nextInt(15000, 15001);
        super.massAttackSpawnerTimer = new Timer(massAttackSpawnTime, this);
        super.massAttackSpawnerTimer.setInitialDelay(5000);




        try {
            background = new SoundClip("data/music/boss.wav");
            background.setVolume(0.75f);
            super.backgoundMusic = background;
            super.backgoundMusic.loop();
        }
        catch (Exception e1)
        {

        }


        super.dashSpawnerTimer.start();
        super.healthSpawnerTimer.start();
        super.rainbowSpawnerTimer.start();
        super.massAttackSpawnerTimer.start();
        super.superJumpTimer.start();
        specialMassAttackTimer.start();

        super.groundImage = new BodyImage("data/dummyImage.png");

        Shape shape  = new BoxShape(22, 2f);
        StaticBody ground = new StaticBody(this, shape);
        ground.setPosition(new Vec2(0f, -18.5f));
        ground.addImage(groundImage);

        boss = new Boss(this, 600, getPlayer());
        this.addStepListener(boss);


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

        if(e.getSource()==superJumpTimer && !getPlayer().isDead && !getPlayer().isCompletedLevel)
        {
            SuperJumpToken superJumpToken = new SuperJumpToken(this);
            SuperJumpTaken taken = new SuperJumpTaken(superJumpToken);
            superJumpToken.addCollisionListener(taken);
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
