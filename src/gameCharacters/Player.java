package gameCharacters;

import bullets.RainbowBullet;
import city.cs.engine.*;
import game.Game;
import game.GameView;
import game.levels.Level1;
import game.levels.Level2;
import game.levels.Level3;
import gameControllers.RainbowBulletImpact;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description Class for Player.
 */
public class Player extends Walker implements StepListener, ActionListener {

    protected static Shape playerShape = new PolygonShape(1.74f,-3.89f, 1.55f,3.07f, -1.12f,2.99f, -1.95f,-1.57f, -1.81f,-3.84f);

    private BodyImage playerImageGoingRight = new BodyImage("data/ollieWalkingRight-export.gif", 8);
    private BodyImage playerImageGoingLeft = new BodyImage("data/ollieWalkingLeft-export.gif", 8);
    private BodyImage playerImageRainbowL = new BodyImage("data/ollieRainbowL-export.gif", 8);
    private BodyImage playerImageRainbowR = new BodyImage("data/ollieRainbow-export.gif", 8);
    private BodyImage playerImageDamaged = new BodyImage("data/ollieidleDamaged.png", 8);
    private BodyImage playerImageIdle = new BodyImage("data/ollieidle-export.gif", 8);
    private BodyImage playerImageDashRight = new BodyImage("data/dashRight.png", 8);
    private BodyImage playerImageJumpRight = new BodyImage("data/jumpRight.png", 8);
    private BodyImage playerImage = new BodyImage("data/ollieidle-export.gif", 8);
    private BodyImage playerImageJumpLeft = new BodyImage("data/jumpLeft.png", 8);
    private BodyImage playerImageDashLeft = new BodyImage("data/dashLeft.png", 8);

    protected boolean isFiring, movingHelper, canFire = true, isDamaged = false;

    private Timer damagedTimer = new Timer(300, this);


    private Timer coolDownTimer;

    protected int healthPoints;
    private int amountOfDashes;
    public SoundClip damaged, died;
    private int score = 0;
    public int ammo = 5;
    public boolean isDead = false;
    private World playersWorld;
    private UserView gameView;
    Game game;
    String state;
    public boolean isCompletedLevel = false;
    public UserView getGameView() {
        return gameView;
    }

    public void setGameView(UserView gameView) {
        this.gameView = gameView;
    }

    public Player(World world, Game game) {
        super(world, playerShape);
        playersWorld = world;
        this.game = game;

        addImage(playerImageIdle);
        healthPoints = 500;
        coolDownTimer = new Timer(1000, this);
        amountOfDashes = 0;
        try {
            damaged = new SoundClip("data/music/damaged.wav");
            died = new SoundClip("data/music/death.wav");
        }
        catch (Exception e)
        {

        }
    }
    public Player getPlayer()
    {
        return this;
    }

    public  BodyImage getPlayerImage() {
        return playerImage;
    }

    public void setPlayerImage(BodyImage playerImage) {
        this.playerImage = playerImage;
    }

    public boolean getMovingHelper() {
        return movingHelper;
    }

    public void setMovingHelper(boolean movingHelper) {
        this.movingHelper = movingHelper;
    }

    public BodyImage getPlayerImageIdle() {
        return playerImageIdle;
    }

    public void setPlayerImageIdle(BodyImage playerImageReturn) {
        this.playerImageIdle = playerImageReturn;
    }
    public BodyImage getPlayerImageRainbowR() {
        return playerImageRainbowR;
    }

    public BodyImage getPlayerImageRainbowL() {
        return playerImageRainbowL;
    }
    public void setFiring(boolean firing) {
        isFiring = firing;
    }

    public boolean isFiring() {
        return isFiring;
    }

    public World getPlayersWorld() {
        return playersWorld;
    }

    public void setPlayersWorld(World playersWorld) {
        this.playersWorld = playersWorld;
    }
    public int getAmountOfDashes() {
        return amountOfDashes;
    }

    public void setAmountOfDashes(int amountOfDashes) {
        this.amountOfDashes = amountOfDashes;
    }
    public void addAmountOfDashes(int amountOfDashes) {
        this.amountOfDashes += amountOfDashes;
    }
    public void reduceHP(int damage)
    {
        healthPoints-=damage;
    }
    public void addHP(int heal)
    {
        healthPoints+=heal;
    }
    public String getState() {
        return state;
    }

    public int getHealthPoints() {
        return healthPoints;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public void addScore(int addScore)
    {
        score+=addScore;
    }
    public boolean isDamaged() {
        return isDamaged;
    }
    public void setDamaged(boolean damaged) {
        isDamaged = damaged;
    }
    public void changeAnimation(BodyImage newAnimation)
    {
        this.removeAllImages();
        this.addImage(newAnimation);
    }

    /**
     * Method to find out players state
     * <p>
     * Using velocities method finds out in what state the player is in order to give him correct animation.
     * @param
     * @return void.
     */
    public void movementStateMachine()
    {
        state = "none yet";
        if(this.getLinearVelocity().x>0 && this.getLinearVelocity().x<6 && this.getLinearVelocity().y>0)
        {
            state = "flyingToTheRight";
            this.changeAnimation(playerImageJumpRight);
        }
        if(this.getLinearVelocity().x<0 && this.getLinearVelocity().x>-6 && this.getLinearVelocity().y>0.1)
        {
            state = "flyingToTheLeft";
            this.changeAnimation(playerImageJumpLeft);
        }
        if(this.getLinearVelocity().x==0 && this.getLinearVelocity().y>0)
        {
            state = "flyingIdle";
            this.changeAnimation(playerImageIdle);
        }
        if(this.getLinearVelocity().x==0 && this.getLinearVelocity().y==0)
        {
            state = "staying";
            this.changeAnimation(playerImageIdle);
        }
        if(this.getLinearVelocity().x>0 && this.getLinearVelocity().x<6
                && this.getLinearVelocity().y>=0 && this.getLinearVelocity().y<0.1)
        {
            state = "goingRight";
            this.changeAnimation(playerImageGoingRight);
        }
        if(this.getLinearVelocity().x<0 && this.getLinearVelocity().x>-6 &&
                this.getLinearVelocity().y<=0 && this.getLinearVelocity().y>-0.1)
        {
            state = "goingLeft";
            this.changeAnimation(playerImageGoingLeft);
        }
        if(this.getLinearVelocity().x>6 && this.getLinearVelocity().y==0
                || this.getLinearVelocity().x>6 && this.getLinearVelocity().y>0)
        {
            state = "DashRight";
            this.changeAnimation(playerImageDashRight);
        }
        if(this.getLinearVelocity().x<-6 && this.getLinearVelocity().y==0
                || this.getLinearVelocity().x<-6 &&  this.getLinearVelocity().y>0)
        {
            state = "DashLeft";
            this.changeAnimation(playerImageDashLeft);
        }
        //System.out.println(state + " : " + this.getLinearVelocity().y);
    }
    /**
     * Find initial direction for the rainbow bullet.
     * <p>
     * Mehod finds out initial direction for the bullet to be fired.
     * @param
     * @return void.
     */
    public boolean getInitDirection()
    {
        boolean initialDirection = true;
        if(state.equals("flyingIdle") || state.equals("staying") || state.equals("goingRight")
                || state.equals("DashRight") || state.equals("flyingToTheRight"))
        {
            initialDirection = true;
        }
        if(state.equals("flyingToTheLeft") || state.equals("goingLeft") || state.equals("DashLeft"))
        {
            initialDirection = false;
        }
        return initialDirection;
    }

    @Override
    public void preStep(StepEvent stepEvent) {

    }

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }
    /**
     * General player controller method.
     * <p>
     * Checks whether the player is dead and also checks conditions to go to other levels.
     * Also checks whether the player was damaged and if he was sets appropriate animation.
     * @param
     * @return void.
     */
    @Override
    public void postStep(StepEvent stepEvent) {

        if(healthPoints<=0)
        {
            isDead = true;

            ((GameView)gameView).setButton();

            this.destroy();
            died.play();
        }
        if(playersWorld instanceof Level1)
        {
            if(score>=10)
            {
                isCompletedLevel = true;
                game.goToTheNextLevel();
            }
        }
        if(playersWorld instanceof Level2)
        {
            if(score>=5)
            {
                isCompletedLevel = true;
                game.goToTheNextLevel();
            }
        }
        if(playersWorld instanceof Level3)
        {
            if(score>=8)
            {
                isCompletedLevel = true;
                game.goToTheNextLevel();
            }
        }
        if(isFiring)
        {
            if(getInitDirection())
            {
                this.changeAnimation(playerImageRainbowR);
            }
            else
            {
                this.changeAnimation(playerImageRainbowL);
            }
        } else if (isDamaged) {
            this.changeAnimation(playerImageDamaged);
            this.setLinearVelocity(new Vec2(0, 0));
            damagedTimer.start();
        } else {
            movementStateMachine();
        }
    }
    public void raibowFire(boolean direction)
    {
        if(canFire) {
            coolDownTimer.start();
            if (direction) {
                RainbowBullet rb = new RainbowBullet(getWorld(), new Vec2(this.getPosition().x + 2, this.getPosition().y + 2), direction);
                RainbowBulletImpact bp = new RainbowBulletImpact(this);
                rb.addCollisionListener(bp);
                this.setLinearVelocity(new Vec2(-5, 0));
                canFire = false;
            } else {
                RainbowBullet rb = new RainbowBullet(getWorld(), new Vec2(this.getPosition().x - 2, this.getPosition().y + 2), direction);
                RainbowBulletImpact bp = new RainbowBulletImpact(this);
                rb.addCollisionListener(bp);
                this.setLinearVelocity(new Vec2(5, 0));
                canFire = false;
            }
        }
    }
    /**
     * Reset cooldown for firing
     * @param
     * @return void.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==damagedTimer)
        {
            isDamaged=false;
            damagedTimer.stop();

        }
        canFire = true;
        coolDownTimer.stop();
    }
}
