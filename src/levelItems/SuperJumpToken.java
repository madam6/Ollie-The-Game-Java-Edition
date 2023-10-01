package levelItems;

import city.cs.engine.BodyImage;
import city.cs.engine.BoxShape;
import city.cs.engine.Shape;
import city.cs.engine.World;
/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description SuperJump collectable.
 */
public class SuperJumpToken extends Collectable{
    protected static BodyImage jumpTakenAnimation = new BodyImage("data/superJumpToken.gif", 5);
    protected static BodyImage dashAnimation = new BodyImage("data/superJumpToken.gif", 5);
    protected static Shape collectableShape = new BoxShape(2, 2);

    public SuperJumpToken(World w) {
        super(w, dashAnimation, jumpTakenAnimation, collectableShape);
        spawner();
    }
}
