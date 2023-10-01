package levelItems;

import city.cs.engine.BodyImage;
import city.cs.engine.BoxShape;
import city.cs.engine.Shape;
import city.cs.engine.World;
import org.jbox2d.common.Vec2;
/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description MassAttackToken collectable.
 */
public class MassAttackToken extends Collectable{
    protected static BodyImage dashTakenAnimation = new BodyImage("data/dashTokenTaken-export.gif", 5);
    protected static BodyImage dashAnimation = new BodyImage("data/newAttackToken.gif", 5);
    protected static Shape collectableShape = new BoxShape(2, 2);

    public MassAttackToken(World w) {
        super(w, dashAnimation, dashTakenAnimation, collectableShape);
        spawner();
    }
    public MassAttackToken(World w, Vec2 startPos)
    {
        super(w, dashAnimation, dashTakenAnimation, collectableShape);
        setPos(startPos);
    }
}
