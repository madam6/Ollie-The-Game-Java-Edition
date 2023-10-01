package game;

import city.cs.engine.UserView;
import game.levels.EntryScreen;
import gameCharacters.Player;

import java.awt.*;
/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description View for entry screen. Contains animated background.
 * and timers etc.
 */
public class EntryScreenView extends UserView {
    private final Image background = EntryScreen.getBackground();

    Player player;
    EntryScreen entryScreen;

    public EntryScreenView(EntryScreen w, int width, int height) {
        super(w, width, height);
        entryScreen = w;

    }
    public void updateEntryScreen(EntryScreen w)
    {
        entryScreen = w;
    }

    @Override
    protected void paintBackground(Graphics2D g) {
        g.drawImage(background, 0, 0, this);
    }
    @Override
    protected void paintForeground(Graphics2D g)
    {

    }
}
