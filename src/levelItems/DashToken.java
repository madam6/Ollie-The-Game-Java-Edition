package levelItems;

import city.cs.engine.BodyImage;
import city.cs.engine.BoxShape;
import city.cs.engine.Shape;
import city.cs.engine.World;
/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description Dashtoken collectable.
 */
public class DashToken extends Collectable{

    protected static BodyImage dashTakenAnimation = new BodyImage("data/dashTokenTaken-export.gif", 5);
    protected static BodyImage dashAnimation = new BodyImage("data/dashToken.gif", 5);
    protected static Shape collectableShape = new BoxShape(2, 2);

    public DashToken(World w) {
        super(w, dashAnimation, dashTakenAnimation, collectableShape);
        spawner();
    }
}
