package levelItems;

import city.cs.engine.BodyImage;
import city.cs.engine.BoxShape;
import city.cs.engine.Shape;
import city.cs.engine.World;
/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description HealthToken collectable.
 */
public class HealthToken extends Collectable{
    protected static BodyImage dashTakenAnimation = new BodyImage("data/dashTokenTaken-export.gif", 5);
    protected static BodyImage healthAnimation = new BodyImage("data/healthToken.gif", 5);
    protected static Shape collectableShape = new BoxShape(2, 2);

    public HealthToken(World w) {
        super(w, healthAnimation, dashTakenAnimation, collectableShape);
        spawner();
    }
}
