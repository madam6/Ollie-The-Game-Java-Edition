package levelItems;

import city.cs.engine.*;
/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description Invisible wall that blocks player from running away from the level.
 */
public class invisibleWall extends StaticBody {

    private static Shape wallShape = new BoxShape(2, 15);
    private BodyImage wallImage = new BodyImage("data/invisiblewall.png");
    public invisibleWall(World w) {
        super(w, wallShape);
        addImage(wallImage);
    }

    public invisibleWall(World w, Shape s) {
        super(w, s);
    }
}
