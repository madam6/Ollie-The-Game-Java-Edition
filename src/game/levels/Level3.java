package game.levels;

import city.cs.engine.*;
import city.cs.engine.Shape;
import game.Game;
import game.GameView;
import gameCharacters.PlaneEnemy;
import gameCharacters.RonaldEnemy;
import gameCharacters.SuitEnemy;
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
 * @description Third level of my game. Contains level`s music, enemy(Plane)
 * level items and collectibles for this level.
 * and timers etc.
 */
public class Level3 extends GameLevel {
    SoundClip background;
    GameView view;
    Timer specialMassAttackTimer = new Timer(5000, this);
    public static int suitCount = 2;
    private Image anim = new ImageIcon("data/toTheLevel3.gif").getImage();
    private Image SecondLevelBackground = new ImageIcon("data/nextnextBackGround-export.png").getImage();
    public Level3(Game game, GameView view)
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
    public void Level3Starter()
    {
        view.setBackground(SecondLevelBackground);


        super.dashSpawnTime = new Random().nextInt(5000, 10000);
        super.dashSpawnerTimer = new Timer(dashSpawnTime, this);

        super.healthSpawnTime = new Random().nextInt(20000, 30000);
        super.healthSpawnerTimer = new Timer(healthSpawnTime, this);



        super.rainbowSpawnTime = new Random().nextInt(3000, 5000);
        super.rainbowSpawnerTimer = new Timer(rainbowSpawnTime, this);

        super.superJumpTime = new Random().nextInt(5000, 10000);
        super.superJumpTimer = new Timer(superJumpTime, this);
        super.superJumpTimer.setInitialDelay(1000);

        super.massAttackSpawnTime = new Random().nextInt(20000, 25000);
        super.massAttackSpawnerTimer = new Timer(massAttackSpawnTime, this);
        super.massAttackSpawnerTimer.setInitialDelay(1000);


        super.suitSpawnTime = new Random().nextInt(10000, 12000);
        super.suitSpawnerTimer = new Timer(suitSpawnTime, this);
        super.suitSpawnerTimer.setInitialDelay(1000);

        try {
            background = new SoundClip("data/music/level23.wav");
            background.setVolume(0.25f);
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

        SuitEnemy suit = new SuitEnemy(this, 50, super.getPlayer());
        SuitEnemy suit1 = new SuitEnemy(this, 50, super.getPlayer());
        this.addStepListener(suit);
        this.addStepListener(suit1);
        super.groundImage = new BodyImage("data/dummyImage.png");

        Shape shape = new PolygonShape(20.6f,-18.1f, 5.5f,-17.0f, -6.0f,-17.4f, -17.6f,-19.7f, -20.7f,-20.9f, 20.3f,-20.6f);
        StaticBody ground = new StaticBody(this, shape);
        ground.setPosition(new Vec2(0f, 0f));
        ground.addImage(groundImage);




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
