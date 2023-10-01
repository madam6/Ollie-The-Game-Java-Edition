package game;

import city.cs.engine.*;
import game.levels.Button;
import game.levels.*;
import gameCharacters.Player;
import gameControllers.backToMenuButtonController;
import gameControllers.retryButtonController;
import org.jbox2d.common.Vec2;

import java.awt.*;
/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description View for gaming levels. Contains background, in game UI, and buttons for restart and go back.
 * and timers etc.
 */

public class GameView extends UserView implements StepListener {

    private Image background;
    public game.levels.Button retry, back;
    Player player;
    World w;
    private BodyImage retryImage = new BodyImage("data/retryB.png", 12);
    private BodyImage retryImageM = new BodyImage("data/retryBM.png", 12);
    private BodyImage retryImageC = new BodyImage("data/retryBC.png", 12);
    public BodyImage backI = new BodyImage("data/backB.png", 12);
    public BodyImage backIM = new BodyImage("data/backBM.png", 12);
    public BodyImage backIC = new BodyImage("data/backBC.png", 12);
    public static BodyImage label = new BodyImage("data/victory.png", 20);

    public StaticBody playButtonBody, bBB;
    public Game game;
    public GameLevel level;
    SoundClip victory;
    /**
     * Constructor for GameView.
     * <p>
     * This constructor besides regular constructor things also creates and hides buttons for retry and replay.
     * @param
     * @return void.
     */
    public GameView(World w, int width, int height, Player player, Image background, Game game) {
        super(w, width, height);
        this.w = w;
        this.player=player;
        this.background = background;
        this.game = game;
        playButtonBody = new StaticBody(w);
        bBB = new StaticBody(w);
        level = game.level;
        retry = new Button(retryImage, retryImageM, retryImageC, playButtonBody, 218,415, 355, 83);
        retry.setPosition(new Vec2(0, -3));

        back = new Button(backI, backIM, backIC, bBB, 218,515, 355, 83);
        back.setPosition(new Vec2(0, -8));

        retry.setVisible(false);
        back.setVisible(false);
        try {
            victory = new SoundClip("data/music/victory.wav");
        } catch (Exception e)
        {}
    }
    public void setBackground(Image background)
    {
        this.background = background;
    }
    public void updateItself()
    {

    }
    /**
     * Method for setting retry and back buttons.
     * <p>
     * This method sets visability of previously created buttons to true.
     * And stops all current level moves.
     * @param
     * @return void.
     */
    public void setButton()
    {
        System.out.println("supermegatest");
        System.out.println(retry.getButtonsEntity().getImages().toString());
        retry.setVisible(true);
        back.setVisible(true);
        retry.changeImage(retryImage);
        back.changeImage(backI);


        back.setForeground(255, 255, 255, 0);
        back.setBackground(255, 255, 255, 0);
        retry.setForeground(255, 255, 255, 0);
        retry.setBackground(255, 255, 255, 0);
        retryButtonController rbc = new retryButtonController(retry, game, this);
        backToMenuButtonController btmbc = new backToMenuButtonController(back, game, this);
        back.addMouseListener(btmbc);
        retry.addMouseListener(rbc);
        level.stop();

        if(level instanceof Level1)
        {
            level.planeSpawnerTimer.stop();
            level.dashSpawnerTimer.stop();
            level.rainbowSpawnerTimer.stop();
        }
        if(level instanceof Level2)
        {

            level.dashSpawnerTimer.stop();
            level.rainbowSpawnerTimer.stop();
            level.massAttackSpawnerTimer.stop();
            level.ronaldSpawnerTimer.stop();
        }
        if(level instanceof Level3)
        {

            level.dashSpawnerTimer.stop();
            level.rainbowSpawnerTimer.stop();
            level.massAttackSpawnerTimer.stop();

            level.superJumpTimer.stop();
            level.suitSpawnerTimer.stop();
        }
        if(level instanceof FinalLevel) {
            level.dashSpawnerTimer.stop();
            level.rainbowSpawnerTimer.stop();
            level.massAttackSpawnerTimer.stop();
            level.superJumpTimer.stop();
        }
        this.add(back);
        this.add(retry);
    }
    /**
     * Method for updating player when moving between levels
     * <p>
     * Sets passed player as field of this class in order to update Player when going from one level to another.
     * @param newPlayer
     * @return void.
     */
    public void updatePlayer(Player newPlayer)
    {
        System.out.println("testofupdating");
        this.player = newPlayer;

        level = game.level;
        retry = new Button(retryImage, retryImageM, retryImageC, playButtonBody, 218,415, 355, 83);
        retry.setPosition(new Vec2(0, -3));

        back = new Button(backI, backIM, backIC, bBB, 218,515, 355, 83);
        back.setPosition(new Vec2(0, -8));
        retry.addImage(retryImage);
        back.addImage(backI);
        retry.setVisible(true);

    }
    @Override
    protected void paintBackground(Graphics2D g) {
        g.drawImage(background, 0, 0, this);
    }
    /**
     * UI method
     * <p>
     * Method that draws important UI things, like healthBar amount of dashes, bullets and also bosses healthBar.
     * @param
     * @return void.
     */
    @Override
    protected void paintForeground(Graphics2D g)
    {
        
        Font stringFont = new Font( "SansSerif", Font. PLAIN, 24 );
        g.setFont(stringFont);
        g.setColor(Color.yellow);
        g.fillRect(60, 40, (int) (player.getHealthPoints()*1.36), 10);
        g.drawString("Amount of dases: " + String.valueOf(player.getAmountOfDashes()), 50, 100);
        g.drawString("Amount of rainbows: " + String.valueOf(player.ammo), 50, 130);
        g.drawString( String.valueOf(player.getScore()), 390, 100);

        if(level instanceof FinalLevel)
        {
            g.setColor(Color.red);
            g.fillRect(60, 740, (int) (((FinalLevel) level).boss.hp*1.3f), 10);

        }

    }


    @Override
    public void preStep(StepEvent stepEvent) {

    }

    @Override
    public void postStep(StepEvent stepEvent) {


        if(player.isDead)
        {
            System.out.println("supertest");
            setButton();
        }
    }
}

