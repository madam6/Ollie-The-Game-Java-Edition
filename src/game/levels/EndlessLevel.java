

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

public class EndlessLevel extends GameLevel {
    SoundClip background;
    GameView view;
    Timer specialMassAttackTimer = new Timer(5000, this);
    private Image anim = new ImageIcon("data/toTheLevel3.gif").getImage();
    private Image SecondLevelBackground = new ImageIcon("data/background1.png").getImage();
    public EndlessLevel (Game game)
    {
        super(game);
        super.setRegularView(new GameView(this, 800, 800, getPlayer(), SecondLevelBackground, game));



    }
    public void setView1(GameView view)
    {
        this.view = view;
    }
    public void EndlessStarter()
    {
        view.setBackground(SecondLevelBackground);

        super.planeSpawnTime =  new Random().nextInt(10000, 10001);
        super.planeSpawnerTimer = new Timer(planeSpawnTime, this);
        super.planeSpawnerTimer.setInitialDelay(1000);

        super.dashSpawnTime = new Random().nextInt(3000, 10000);
        super.dashSpawnerTimer = new Timer(dashSpawnTime, this);

        super.healthSpawnTime = new Random().nextInt(5000, 10000);
        super.healthSpawnerTimer = new Timer(healthSpawnTime, this);

        super.ronaldSpawnTime = new Random().nextInt(20000, 20001);
        super.ronaldSpawnerTimer = new Timer(ronaldSpawnTime, this);

        super.rainbowSpawnTime = new Random().nextInt(3000, 5000);
        super.rainbowSpawnerTimer = new Timer(rainbowSpawnTime, this);

        super.superJumpTime = new Random().nextInt(5000, 10000);
        super.superJumpTimer = new Timer(superJumpTime, this);
        super.superJumpTimer.setInitialDelay(1000);

        super.massAttackSpawnTime = new Random().nextInt(6000, 7000);
        super.massAttackSpawnerTimer = new Timer(massAttackSpawnTime, this);
        super.massAttackSpawnerTimer.setInitialDelay(1000);


        super.suitSpawnTime = new Random().nextInt(30000, 30001);
        super.suitSpawnerTimer = new Timer(suitSpawnTime, this);


        try {
            background = new SoundClip("data/music/level23.wav");
            background.setVolume(0.25f);
            super.backgoundMusic = background;
            super.backgoundMusic.loop();
        }
        catch (Exception e1)
        {

        }

        super.planeSpawnerTimer.start();

        super.ronaldSpawnerTimer.start();
        super.dashSpawnerTimer.start();
        super.healthSpawnerTimer.start();
        super.rainbowSpawnerTimer.start();
        super.massAttackSpawnerTimer.start();
        super.suitSpawnerTimer.start();
        super.superJumpTimer.start();


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

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==ronaldSpawnerTimer && !getPlayer().isDead && !getPlayer().isCompletedLevel)
        {
            RonaldEnemy ronald = new RonaldEnemy(this, 25, super.getPlayer());
            this.addStepListener(ronald);
            ronaldSpawnTime -= 250;
        }
        if(e.getSource()==planeSpawnerTimer&&!getPlayer().isDead && !getPlayer().isCompletedLevel)
        {
            PlaneEnemy plane = new PlaneEnemy(this, 50, super.getPlayer());
            this.addStepListener(plane);
            planeSpawnTime-=500;
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
        if(e.getSource()==specialMassAttackTimer && !getPlayer().isDead && !getPlayer().isCompletedLevel)
        {
            //MassAttackToken massAttackToken = new MassAttackToken(this, new Vec2(-2, 2));
            //MassAttackTaken taken = new MassAttackTaken(massAttackToken, this);
            //massAttackToken.addCollisionListener(taken);
        }
        if(e.getSource()==superJumpTimer && !getPlayer().isDead && !getPlayer().isCompletedLevel)
        {
            SuperJumpToken superJumpToken = new SuperJumpToken(this);
            SuperJumpTaken taken = new SuperJumpTaken(superJumpToken);
            superJumpToken.addCollisionListener(taken);
        }
        if(e.getSource()==suitSpawnerTimer && !getPlayer().isDead && !getPlayer().isCompletedLevel)
        {
            SuitEnemy suit = new SuitEnemy(this, 40, super.getPlayer());
            this.addStepListener(suit);
            suitSpawnTime-=500;
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

