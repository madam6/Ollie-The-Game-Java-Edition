package game.levels;

import city.cs.engine.*;
import game.Game;
import gameCharacters.PlaneEnemy;
import gameCharacters.Player;
import gameCharacters.RonaldEnemy;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.awt.event.ActionListener;
/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description Big class for inheritance that is used when creating
 * actual game levels. Contains all enemies fields, their spawn times
 * and timers etc.
 */
public abstract class GameLevel extends World implements ActionListener, StepListener {

    protected int planeSpawnTime;
    protected int ronaldSpawnTime;
    protected int dashSpawnTime;
    /**
     * Checks whether player have won the game.
     */
    public boolean victoryB = false;
    protected int healthSpawnTime;
    protected int rainbowSpawnTime;
    protected int massAttackSpawnTime;
    protected int suitSpawnTime;
    protected int superJumpTime;
    BodyImage groundImage;
    public Timer planeSpawnerTimer, ronaldSpawnerTimer, suitSpawnerTimer;
    private Player player;
    private PlaneEnemy plane;
    private RonaldEnemy ronald;

    SoundClip victory;
    public Timer dashSpawnerTimer, healthSpawnerTimer, rainbowSpawnerTimer, massAttackSpawnerTimer, superJumpTimer;
    Game game;
    public boolean isComplete;
    /**
     * Labels for victory.
     */
    public static BodyImage label = new BodyImage("data/victory.png", 20);
    /**
     * Body to which you can attach victory picture.
     */
    public StaticBody labelBody = new StaticBody(this);
    UserView view, regularView;
    public SoundClip backgoundMusic;
    public GameLevel(Game game)
    {
        this.game = game;
        player = new Player(this, game);
        player.setPosition(new Vec2(0, -15));
        this.addStepListener(player);
        try {
            victory = new SoundClip("data/music/victory!.wav");
        } catch (Exception e)
        {

        };
    }
    public GameLevel(Game game, int i)
    {
        this.game = game;
        try {
            victory = new SoundClip("data/music/victory!.wav");
        } catch (Exception e)
        {

        }
    }
    /**
     * Method to show victory screen.
     * <p>
     * Shows victory picture, kills player so the user can restart the game or go back.
     * @param
     * @return void.
     */
    public void victory()
    {
        this.backgoundMusic.stop();
        victoryB = true;
        getPlayer().isDead=true;
        getPlayer().isCompletedLevel=true;
        getPlayer().reduceHP(500000);
        victory.play();
        labelBody.setPosition(new Vec2(0, 5));
        labelBody.addImage(label);
    }

    public void setView(UserView view) {
        this.view = view;
    }
    public void setRegularView(UserView view)
    {
        regularView = view;
    }

    public UserView getView() {
        return view;
    }

    public UserView getRegularView() {
        return regularView;
    }

    public boolean getIsComplete()
    {
        return isComplete;
    }
    public GameLevel returnLevel()
    {
        return this;
    }


    public Player getPlayer() {
        return player;
    }

    public PlaneEnemy getPlanes() {
        return plane;
    }

    public void preStep(StepEvent stepEvent) {

    }
    /**
     * Post step method to check whether mc was killed of have completed level
     * <p>
     * If mc was killed of have completed level stops all timers and items to spawn.
     * @param stepEvent - Regular step event.
     * @return void.
     */
    @Override
    public void postStep(StepEvent stepEvent) {

        if(player.isDead || player.isCompletedLevel)
        {
            planeSpawnerTimer.stop();
            ronaldSpawnerTimer.stop();
            healthSpawnerTimer.stop();
            dashSpawnerTimer.stop();
            rainbowSpawnerTimer.stop();
            suitSpawnerTimer.stop();
            superJumpTimer.stop();
        }
    }
}
