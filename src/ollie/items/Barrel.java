package ollie.items;

import city.cs.engine.BodyImage;
import city.cs.engine.DynamicBody;
import city.cs.engine.PolygonShape;
import city.cs.engine.Shape;
import city.cs.engine.World;
import org.jbox2d.common.Vec2;

/** A barrel Ollie can push around and hide behind. Missiles explode on it. */
public class Barrel extends DynamicBody {

    private static final Shape SHAPE = new PolygonShape(
            1.08f, -3.44f, 1.36f, -0.48f, 0.87f, 0.7f, -0.04f, 0.92f, -1.3f, 0.24f, -1.57f, -2.58f, -1.09f, -3.5f);
    private static final BodyImage SPRITE = new BodyImage("data/barrel.png", 7);

    public Barrel(World world, float x, float y) {
        super(world, SHAPE);
        addImage(SPRITE);
        setPosition(new Vec2(x, y));
    }
}
