package game.levels;

import city.cs.engine.StepEvent;
import game.Game;

import java.awt.event.ActionEvent;
/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description Class that is used as text layers in some transitions between
 * screens/layers.
 */
public class TextLevel extends GameLevel{
    public TextLevel(Game game, int i) {
        super(game, i);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void preStep(StepEvent stepEvent) {

    }

    @Override
    public void postStep(StepEvent stepEvent) {

    }
}
