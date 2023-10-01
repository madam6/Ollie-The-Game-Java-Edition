package game.levels;

import city.cs.engine.Shape;
import city.cs.engine.*;
import game.Game;
import game.GameView;
import gameCharacters.PlaneEnemy;
import gameControllers.RainbowTaken;
import gameControllers.dashTaken;
import levelItems.Barrel;
import levelItems.DashToken;
import levelItems.RainbowToken;
import levelItems.invisibleWall;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Random;
/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description First level of my game. Contains level`s music, enemy(Plane)
 * level items and collectibles for this level.
 * and timers etc.
 */
public class Level1 extends GameLevel   {
    public SoundClip backgroundMusic;
    Timer level1Timer = new Timer(1000, this);
    int count = 0;
    public static int PlaneCount = 4;
    private Image background = new ImageIcon("data/infoScreen.png").getImage();
    private Image backgroundReg = new ImageIcon("data/background1.png").getImage();
    ArrayList<PlaneEnemy> planeEnemies;

    GameView level1View;

    public Level1(Game game){
        super(game);
        super.setRegularView(new GameView(this, 800, 800, getPlayer(), backgroundReg, game));

        level1Timer.setRepeats(false);
        try {
            backgroundMusic = new SoundClip("data/music/fight122.wav");
            backgroundMusic.setVolume(0.5f);
            super.backgoundMusic = backgroundMusic;

        }
        catch (Exception e)
        {

        }

        super.planeSpawnTime =  new Random().nextInt(8000, 10000);
        super.planeSpawnerTimer = new Timer(planeSpawnTime, this);
        super.dashSpawnTime = new Random().nextInt(5000, 10000);
        super.dashSpawnerTimer = new Timer(dashSpawnTime, this);
        super.healthSpawnTime = new Random().nextInt(5000, 10000);
        super.healthSpawnerTimer = new Timer(healthSpawnTime, this);
        super.rainbowSpawnTime = new Random().nextInt(3000, 5000);
        super.rainbowSpawnerTimer = new Timer(rainbowSpawnTime, this);
        super.backgoundMusic.loop();



        planeSpawnerTimer.setInitialDelay(1000);

        planeSpawnerTimer.start();
        dashSpawnerTimer.start();
        healthSpawnerTimer.start();
        rainbowSpawnerTimer.start();

        super.groundImage = new BodyImage("data/ground.png");

        Shape shape = new BoxShape(22, 2f);
        StaticBody ground = new StaticBody(this, shape);
        ground.setPosition(new Vec2(0f, -18.5f));
        ground.addImage(groundImage);


        Barrel b1 = new Barrel(this, new Vec2(5, -12));

        invisibleWall wall1 = new invisibleWall(this);
        invisibleWall wall2 = new invisibleWall(this);
        wall1.setPosition(new Vec2(-23, 0));
        wall2.setPosition(new Vec2(23, 0));

    }
    public Level1(Game game, int i)
    {
        super(game, i);
        level1Timer.setRepeats(false);
        try {
            backgroundMusic = new SoundClip("data/music/fight122.wav");
            backgroundMusic.setVolume(0.5f);

        }
        catch (Exception e)
        {

        }
    }




    /**
     * Method that stops music for the planes
     * <p>
     * Method sets volume of all enemy`s sound to 0
     * @param
     * @return void.
     */
    public void planeStop()
    {
        for (PlaneEnemy planeEnemy:planeEnemies) {
            PlaneEnemy.attack.setVolume(0);
            PlaneEnemy.died.setVolume(0);
            PlaneEnemy.damaged.setVolume(0);
            PlaneEnemy.flying.setVolume(0);
        }
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
        level1Timer.stop();
        if(PlaneCount>0)
        {
            if(e.getSource()==planeSpawnerTimer)
            {
                count++;
                planeEnemies = new ArrayList<>();
                System.out.println("spawn");
                PlaneEnemy planeEnemy = new PlaneEnemy(this, 40, super.getPlayer());
                this.addStepListener(planeEnemy);
                planeSpawnTime -= 2000;
                PlaneCount--;
            }
        }
        else
        {
            planeSpawnerTimer.stop();
        }
        if(e.getSource()==dashSpawnerTimer)
        {
            DashToken dashToken = new DashToken(this);
            dashTaken taken = new dashTaken(dashToken);
            dashToken.addCollisionListener(taken);
        }

        if(e.getSource()==rainbowSpawnerTimer)
        {
            RainbowToken rainbowToken = new RainbowToken(this);
            RainbowTaken taken = new RainbowTaken(rainbowToken);
            rainbowToken.addCollisionListener(taken);
        }
    }

    @Override
    public void preStep(StepEvent stepEvent) {

    }

    @Override
    public void postStep(StepEvent stepEvent) {

        super.postStep(stepEvent);
    }
}
