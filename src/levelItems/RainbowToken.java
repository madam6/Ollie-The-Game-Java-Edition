package levelItems;

import city.cs.engine.BodyImage;
import city.cs.engine.BoxShape;
import city.cs.engine.Shape;
import city.cs.engine.World;
/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description Rainbow token collectable.
 */
public class RainbowToken extends Collectable{
    protected static BodyImage dashTakenAnimation = new BodyImage("data/dashTokenTaken-export.gif", 5);
    protected static BodyImage dashAnimation = new BodyImage("data/RainbowToken.gif", 5);
    protected static Shape collectableShape = new BoxShape(2, 2);

    public RainbowToken(World w) {
        super(w, dashAnimation, dashTakenAnimation, collectableShape);
        spawner();
    }
}
