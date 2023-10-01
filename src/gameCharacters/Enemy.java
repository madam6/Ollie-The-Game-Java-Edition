package gameCharacters;
import city.cs.engine.*;
import city.cs.engine.Shape;
import game.levels.GameLevel;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description Class for inheritance that describes essential mechanics for the enemies.
 */
public abstract class Enemy extends DynamicBody implements StepListener, ActionListener {

    public BodyImage damagedAnimationLeft;
    public BodyImage damagedAnimationRight;

    private boolean movingToRight;
    public boolean isDestroyed = false;
    public static boolean isBossDestroyed = false;
    private boolean movingUp;

    private double around=0.01;

    private float speedX;
    private float bond = 15f;

    private int healthPoints;
    private int id;
    boolean isAlive = true;
    Player player;
    public SoundClip mySound, deadSound, atackSound, damagedSound;
    Timer attackTimer, deathTimer;
    private BodyImage deadBoss = new BodyImage("data/bossThirdPhaseDamageDied.gif", 17);
    public boolean isMovingUp() {
        return movingUp;
    }

    public void setMovingUp(boolean movingUp) {
        this.movingUp = movingUp;
    }


    public Enemy(World world, int healthPoints, BodyImage damagedAnimationLeft, BodyImage damagedAnimationRight) {
        super(world);
        deathTimer  = new Timer(600, this);
        deathTimer.setRepeats(false);
        this.healthPoints = healthPoints;
        this.damagedAnimationLeft=damagedAnimationLeft;
        this.damagedAnimationRight=damagedAnimationRight;
    }
    public Enemy(World world, Shape shape, int healthPoints, BodyImage damagedAnimationLeft, BodyImage damagedAnimationRight, Player player, int id) {
        super(world, shape);
        deathTimer  = new Timer(600, this);
        deathTimer.setRepeats(false);
        this.player = player;
        this.healthPoints = healthPoints;
        this.damagedAnimationLeft=damagedAnimationLeft;
        this.damagedAnimationRight=damagedAnimationRight;
        this.id = id;

    }
    public boolean isMovingToRight() {
        return movingToRight;
    }
    public void setMovingToRight(boolean movingToRight) {
        this.movingToRight = movingToRight;
    }
    public boolean isDestroyed() {
        return isDestroyed;
    }
    public float getSpeedX() {
        return speedX;
    }
    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public void setDamagedAnimationLeft() {
        this.removeAllImages();
        this.addImage(damagedAnimationLeft);
    }
    public void setDamagedAnimationRight()
    {
        this.removeAllImages();
        this.addImage(damagedAnimationRight);
    }
    /**
     * Sets damaged animation for the enemy
     * <p>
     * Method judjing on the side to which enemy was flying to sets animation to respective
     * left or right version of damaged animation
     * @param
     * @return void.
     */
    public void setDamagedAnimation()
    {
        if(this.isMovingToRight())
        {
            setDamagedAnimationRight();
        }
        else
        {
            setDamagedAnimationLeft();
        }
    }
    public void changeAnimation(BodyImage newAnimation)
    {
        this.removeAllImages();
        this.addImage(newAnimation);
    }

    public void reduceHitPoints(int amountToReduce)
    {
        healthPoints-=amountToReduce;
    }
    /**
     * Spawner for enemies
     * <p>
     * Randomly spawns enemies either on the left side or on the right side.
     * @param
     * @return void.
     */
    public void spawner() {

        Random ran = new Random();

        int side = ran.nextInt(2);
        float height = ran.nextFloat(3, 7);

        if (side == 0) {
            this.setPosition(new Vec2(-19, height));
        }
        else
        {
            this.setPosition(new Vec2(19, height));
        }

    }

    @Override
    public void preStep(StepEvent stepEvent) {

    }

    public int getHealthPoints() {
        return healthPoints;
    }
    /**
     * Stops sounds for the enemy
     * <p>
     * Finds out whether this enemy is boss or a regular enemy and
     * stops all the sounds that this enemy can produce as well as
     * timers for attack.
     * @param
     * @return void.
     */
    public void stop()
    {
        if(this instanceof Boss)
        {
            this.mySound.stop();
            this.atackSound.stop();
            this.damagedSound.stop();
            ((Boss)this).fireTimer.stop();
        }
        else {
            this.mySound.stop();
            this.atackSound.stop();
            this.damagedSound.stop();
            this.attackTimer.setRepeats(false);
        }
    }
    /**
     * Main method for this class.
     * <p>
     * This method firstly detects whether this enemy was killed,
     * and if it was it then turns on appropriate animations and sounds.
     * If enemies are alive then it makes them move from left to right and
     * also controls that they don`t go too much downwards or upwards by controlling
     * the gravity scale.
     * @param
     * @return void.
     */
    public void postStep(StepEvent stepEvent, float speed, BodyImage rightAnim, BodyImage leftAnim) {

        if(healthPoints<=0)
        {

            if(this instanceof Boss)
            {

                deathTimer.start();
                this.changeAnimation(deadBoss);
                ((Boss) this).fireTimer.stop();
                isBossDestroyed = true;
                this.destroy();
                deadSound.play();
                ((GameLevel)getWorld()).victory();
                mySound.stop();

                atackSound.stop();
            }
            else {
                attackTimer.setRepeats(false);
                mySound.stop();
                attackTimer.stop();
                atackSound.stop();
                isAlive = false;
                this.destroy();
                if (id == 0) {
                    player.addScore(10);
                }
                if (id == 1) {
                    player.addScore(5);
                }
                if (id == 2) {
                    player.addScore(8);
                }

                deadSound.play();
            }

        }
        this.setAngleDegrees(0);
        speedX = speed;
        if ((this.getPosition().x < -bond + around && this.getPosition().x > -bond - around) || this.getPosition().x < -bond) {
            this.changeAnimation(rightAnim);
            movingToRight = true;

        }
        if ((this.getPosition().x < bond + around && this.getPosition().x > bond - around) || this.getPosition().x > bond) {
            this.changeAnimation(leftAnim);
            movingToRight = false;
        }
        if (movingToRight) {
            this.setLinearVelocity(new Vec2(speedX, 0));
        }
        if (!movingToRight) {
            this.setLinearVelocity(new Vec2(-speedX, 0));
        }
        if (this.getPosition().y < 0) {
            this.setGravityScale(-10);
        }
        if (this.getPosition().y > 10) {
            this.setGravityScale(20);
        }

    }


    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == deathTimer) {
            System.out.println("anans");
            isAlive = false;

        }
    }

}
