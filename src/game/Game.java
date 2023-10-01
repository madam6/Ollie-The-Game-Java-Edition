package game;

import city.cs.engine.DebugViewer;
import city.cs.engine.SoundClip;
import city.cs.engine.UserView;
import game.levels.*;
import gameControllers.GiveFocus;
import gameControllers.playerController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description Main class with actual game. It manages frames, worlds and transitions between them.
 */
public class Game implements ActionListener {
    public GameLevel level;
    private EntryScreen entryScreen;
    UserView view;
    playerController playerContr;
    private EntryScreenView esw;
    private Image firstBackground = new ImageIcon("data/infoScreen.png").getImage();
    private Image infiniteBackground = new ImageIcon("data/infinite.png").getImage();
    private Image regBackground = new ImageIcon("data/background1.png").getImage();
    private Image SecondLevelBackground = new ImageIcon("data/nextBackGround-export.png").getImage();
    private Image SecondLevelBackground1 = new ImageIcon("data/level2Anim..gif").getImage();
    private Image ThirdLevelBackground1 = new ImageIcon("data/toTheLevel3.gif").getImage();
    private Image FinalLevelBackground1 = new ImageIcon("data/toTheFinalLevel.gif").getImage();
    private Image FinalLevelBackground = new ImageIcon("data/finalBackground-export.gif").getImage();
    ImageIcon icon = new ImageIcon("data/icon.png");
    Timer level1StartTimer = new Timer(10000, this);
    Timer level2StartTimer = new Timer(6000, this);
    Timer endlessStartTimer = new Timer(3000, this);
    Timer level3StartTimer = new Timer(8000, this);
    Timer level4StartTimer = new Timer(9300, this);

     JFrame frame;
     JFrame frame1;
     JFrame frame2;
     JFrame frame3;
     int playersHP, playersDashes, playersAmmo;

     SoundClip nextLvlSound;

    /**
     * Constructor for the game.
     * <p>
     * Creates all needed frames, sets their properties with views and respective levels.
     * Also starts a music.
     * @param
     * @return void.
     */
    public Game() {

        //Hi! Welcome to my game!
        //Controls: A,D - Moving; E - fire; Q - dash; Space - jump;
        //Goal is to kill enemies and collect score. Dodge their attacks to survive!
        //Good luck!
        try {
            nextLvlSound = new SoundClip("data/music/planeDied.wav");
        }
        catch (Exception e)
        {

        }
        endlessStartTimer.setRepeats(false);
        level1StartTimer.setRepeats(false);
        level = new EndlessLevel(this);
        view = new TextView(level, 800, 800, infiniteBackground);
        level.setView(view);
        frame3 = new JFrame("Ollie! The game.");
        frame3.add(view);
        frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame3.setLocation(250, 20);
        frame3.setResizable(false);
        frame3.pack();
        frame3.setVisible(false);
        frame3.setIconImage(icon.getImage());

        int temp = 0;
        level = new TextLevel(this, temp);
        view = new TextView(level, 800, 800, firstBackground);
        level.setView(view);

        frame2 = new JFrame("Ollie! The game.");
        frame2.add(view);
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame2.setLocation(250, 20);
        frame2.setResizable(false);
        frame2.pack();
        frame2.setVisible(false);
        frame2.setIconImage(icon.getImage());



        entryScreen = new EntryScreen(this);
        entryScreen.playButton.addMouseListener(entryScreen);
        esw = new EntryScreenView(entryScreen, 800, 800);
        esw.setLayout(null);
        esw.add(entryScreen.playButton);
        esw.add(entryScreen.story);
        esw.add(entryScreen.endless);
        esw.add(entryScreen.back);

        frame = new JFrame("Ollie! The game.");

        frame.add(esw);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(250, 20);

        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.setIconImage(icon.getImage());
        entryScreen.start();


    }

    /**
     * Method for uplevelling.
     * <p>
     * Checks current level, and sets next, while stopping current.
     * In fact changes animation to transition animation and starts a timer
     * that will actually initiate new level after transition animation ended.
     * @param
     * @return void.
     */
    public void goToTheNextLevel()
    {
        if(level instanceof Level1)
        {
            level.stop();
            ((Level1)level).backgroundMusic.stop();
            ((Level1)level).planeStop();
            ((Level1)level).planeSpawnerTimer.stop();
            ((GameView)view).setBackground(SecondLevelBackground1);
            level2StartTimer.setRepeats(false);
            level2StartTimer.start();
            playersHP = level.getPlayer().getHealthPoints();
            playersDashes = level.getPlayer().getAmountOfDashes();
            playersAmmo = level.getPlayer().getAmmo();
        }
        if(level instanceof Level2)
        {
            level.stop();
            nextLvlSound.play();
            ((Level2)level).backgoundMusic.stop();
            ((Level2)level).ronaldSpawnerTimer.stop();
            ((Level2)level).dashSpawnerTimer.stop();
            ((Level2)level).healthSpawnerTimer.stop();
            ((Level2)level).massAttackSpawnerTimer.stop();
            ((Level2)level).rainbowSpawnerTimer.stop();
            ((Level2)level).healthSpawnerTimer.stop();

            ((GameView)view).setBackground(ThirdLevelBackground1);
            level3StartTimer.setRepeats(false);
            level3StartTimer.start();
            playersHP = level.getPlayer().getHealthPoints();
            playersDashes = level.getPlayer().getAmountOfDashes();
            playersAmmo = level.getPlayer().getAmmo();
        }
        if(level instanceof Level3)
        {
            level.stop();
            nextLvlSound.play();
            ((Level3)level).backgoundMusic.stop();

            ((Level3)level).dashSpawnerTimer.stop();
            ((Level3)level).healthSpawnerTimer.stop();
            ((Level3)level).massAttackSpawnerTimer.stop();
            ((Level3)level).rainbowSpawnerTimer.stop();
            ((Level3)level).healthSpawnerTimer.stop();

            ((Level3)level).suitSpawnerTimer.stop();
            ((Level3)level).superJumpTimer.stop();
            ((GameView)view).setBackground(FinalLevelBackground1);
            level4StartTimer.setRepeats(false);
            level4StartTimer.start();
            playersHP = level.getPlayer().getHealthPoints();
            playersDashes = level.getPlayer().getAmountOfDashes();
            playersAmmo = level.getPlayer().getAmmo();
        }
    }
    /**
     * Method for starting endless mode.
     * <p>
     * This method is called by endless button, and it starts the endless mode.
     * @param
     * @return void.
     */
    public void startEndless()
    {
        endlessStartTimer.start();

        esw.remove(entryScreen.story);
        esw.remove(entryScreen.endless);
        esw.remove(entryScreen.back);
        esw.remove(entryScreen.playButton);
        entryScreen.menuMusic.stop();
        entryScreen.stop();
        frame.remove(esw);

        frame.setVisible(false);
        frame3.setVisible(true);


    }
    /**
     * Method for going back to main menu.
     * <p>
     * This method is called by back button, and it goes back to main menu.
     * @param
     * @return void.
     */
    public void backToMenu()
    {
        int i = 0;
        ((GameLevel)level).backgoundMusic.stop();
        frame1.dispose();
        level.stop();
        level = new TextLevel(this, 0);
        view = new TextView(level, 800, 800, firstBackground);
        level.setView(view);
        frame2 = new JFrame("Ollie! The game.");
        frame2.add(view);
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame2.setLocation(250, 20);
        frame2.setResizable(false);
        frame2.pack();
        frame2.setVisible(false);
        entryScreen = new EntryScreen(this);
        entryScreen.playButton.addMouseListener(entryScreen);
        esw = new EntryScreenView(entryScreen, 800, 800);
        esw.setLayout(null);
        esw.add(entryScreen.playButton);
        esw.add(entryScreen.story);
        esw.add(entryScreen.endless);
        esw.add(entryScreen.back);

        frame = new JFrame("Ollie! The game.");

        frame.add(esw);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(250, 20);

        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);

        entryScreen.start();
    }
    /**
     * Method for retrying from the first level or endless level.
     * <p>
     * This method is called by retry button, and it restarts level either to level1 or endless level.
     * @param
     * @return void.
     */
    public void retryLevel()
    {
        frame1.dispose();
        level.stop();

        ((GameLevel)level).backgoundMusic.stop();
        if(level instanceof Level3)
        {
            ((Level3)level).suitSpawnerTimer.stop();
        }

        if(level instanceof EndlessLevel)
        {


            ((GameLevel)level).backgoundMusic.stop();

            level = new EndlessLevel(this);
            view = new GameView(level, 800, 800, level.getPlayer(), regBackground, this);

            ((EndlessLevel)level).setView1((GameView) view);
            ((EndlessLevel)level).EndlessStarter();
            level.getPlayer().setGameView(view);
            playerContr  = new playerController(level.getPlayer());
            view.addKeyListener(playerContr);
            GiveFocus gf = new GiveFocus(view);
            view.addMouseListener(gf);
            frame1 = new JFrame("Ollie! The game.");
            frame1.add(view);
            frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame1.setLocation(250, 20);
            frame1.setResizable(false);
            frame1.pack();
            frame1.setVisible(true);
            level.start();
        }
        else {
            level = new Level1(this);
            view = new GameView(level, 800, 800, level.getPlayer(), regBackground, this);
            level.setView(view);
            level.getPlayer().setGameView(view);
            playerContr = new playerController(level.getPlayer());
            view.addKeyListener(playerContr);
            GiveFocus gf = new GiveFocus(view);
            view.addMouseListener(gf);
            frame1 = new JFrame("Ollie! The game.");
            frame1.add(view);
            frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame1.setLocation(250, 20);
            frame1.setResizable(false);
            frame1.pack();
            frame1.setVisible(true);
            level.start();
        }

    }
    /**
     * Method for starting story mode.
     * <p>
     * This method is called by story button, and it starts the story mode.
     * @param
     * @return void.
     */
    public void startStoryMode()
    {

        level1StartTimer.start();
        esw.remove(entryScreen.story);
        esw.remove(entryScreen.endless);
        esw.remove(entryScreen.back);
        esw.remove(entryScreen.playButton);
        entryScreen.menuMusic.stop();
        entryScreen.stop();
        frame.remove(esw);

        frame.setVisible(false);
        frame2.setVisible(true);



    }
    /**
     * Method helper to initiate level1.
     * <p>
     * This method helps to start level1 after animation.
     * @param
     * @return void.
     */
    public void startLeve1()
    {

        frame2.dispose();
        level.stop();
        level = new Level1(this);
        view = new GameView(level, 800, 800, level.getPlayer(), regBackground, this);
        level.setView(view);
        level.getPlayer().setGameView(view);
        playerContr  = new playerController(level.getPlayer());
        view.addKeyListener(playerContr);
        GiveFocus gf = new GiveFocus(view);
        view.addMouseListener(gf);
        frame1 = new JFrame("Ollie! The game.");
        frame1.add(view);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame1.setLocation(250, 20);
        frame1.setResizable(false);
        frame1.pack();
        frame1.setVisible(true);
        frame1.setIconImage(icon.getImage());


        level.start();
    }
    /**
     * Helper method for starting endless mode.
     * <p>
     * This method helps main startEndless. It "initiates" an actual level after animation.
     * @param
     * @return void.
     */
    public void startEndlessMode()
    {
        frame3.dispose();
        level.stop();
        level = new EndlessLevel(this);

        view = new GameView(level, 800, 800, level.getPlayer(), regBackground, this);
        ((EndlessLevel)level).setView1((GameView) view);
        ((EndlessLevel)level).EndlessStarter();
        level.getPlayer().setGameView(view);
        playerContr  = new playerController(level.getPlayer());
        view.addKeyListener(playerContr);
        GiveFocus gf = new GiveFocus(view);
        view.addMouseListener(gf);
        frame1 = new JFrame("Ollie! The game.");
        frame1.add(view);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setLocation(250, 20);
        frame1.setResizable(false);
        frame1.pack();
        frame1.setVisible(true);




        level.start();
    }
    /**
     * Method for going back from main menu to main menu.
     * <p>
     * This method is called by back button in EntryScreen, and it goes back to start.
     * @param
     * @return void.
     */
    public void resetEntryScreen()
    {
        esw.remove(entryScreen.story);
        esw.remove(entryScreen.endless);
        esw.remove(entryScreen.back);
        esw.remove(entryScreen.playButton);
        entryScreen.menuMusic.stop();
        entryScreen.stop();

        entryScreen = new EntryScreen(this);
        entryScreen.playButton.addMouseListener(entryScreen);
        esw.add(entryScreen.playButton);
        esw.add(entryScreen.story);
        esw.add(entryScreen.endless);
        esw.add(entryScreen.back);
        entryScreen.sbc.updateButton(entryScreen.story);
        entryScreen.ebc.updateButton(entryScreen.endless);
        entryScreen.bbc.updateButton(entryScreen.back, entryScreen.story, entryScreen.endless, entryScreen.playButton);
        esw.setWorld(entryScreen);
        entryScreen.start();





    }
    /** Run the game. */
    public static void main(String[] args) {

        new Game();
    }

    /**
     * Timer manager.
     * <p>
     * This method manages all the timer for animations between levels.
     * @param
     * @return void.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == level1StartTimer)
        {
            startLeve1();
        }
        if(e.getSource() == endlessStartTimer)
        {
            startEndlessMode();
        }
        if(e.getSource()==level2StartTimer)
        {
            level = new Level2(this, (GameView) view);
            ((Level2)level).Level2Starter();
            playerContr.updatePlayer(level.getPlayer());
            level.getPlayer().setGameView(view);
            ((GameView)view).updatePlayer(level.getPlayer());

            level.getPlayer().setHealthPoints(playersHP);
            level.getPlayer().setAmountOfDashes(playersDashes);
            level.getPlayer().setAmmo(playersAmmo);
            view.setWorld(level);
            level.start();
        }
        if(e.getSource()==level3StartTimer)
        {

            level = new Level3(this, (GameView) view);
            ((Level3)level).Level3Starter();
            playerContr.updatePlayer(level.getPlayer());
            level.getPlayer().setGameView(view);
            ((GameView)view).updatePlayer(level.getPlayer());
            level.getPlayer().setHealthPoints(playersHP);
            level.getPlayer().setAmountOfDashes(playersDashes);
            level.getPlayer().setAmmo(playersAmmo);
            view.setWorld(level);
            level.start();
        }
        if(e.getSource()==level4StartTimer)
        {
            level = new FinalLevel(this, (GameView) view);
            ((FinalLevel)level).FinalLevelStarter();
            playerContr.updatePlayer(level.getPlayer());
            level.getPlayer().setGameView(view);
            ((GameView)view).updatePlayer(level.getPlayer());
            level.getPlayer().setHealthPoints(playersHP);
            level.getPlayer().setAmountOfDashes(playersDashes);
            level.getPlayer().setAmmo(playersAmmo);
            view.setWorld(level);
            level.start();
        }
    }
}