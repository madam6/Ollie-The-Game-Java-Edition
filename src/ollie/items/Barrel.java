package ollie.items;

import city.cs.engine.DynamicBody;
import city.cs.engine.PolygonShape;
import city.cs.engine.Shape;
import city.cs.engine.World;
import ollie.ui.Lighting;
import ollie.ui.Sprite;
import org.jbox2d.common.Vec2;

/** A barrel Ollie can push around and hide behind. Missiles explode on it. */
public class Barrel extends DynamicBody implements Lighting.LightSource {

    private static final Shape SHAPE = new PolygonShape(
            1.23f, -3.93f, 1.55f, -0.55f, 0.99f, 0.8f, -0.05f, 1.05f, -1.49f, 0.27f, -1.79f, -2.95f, -1.25f, -4.0f);
    private static final Sprite SPRITE = Sprite.large("barrel.png");
    /** Distance from the body's origin down to the bottom of the barrel when it's standing up. */
    private static final float BOTTOM = 4;

    public Barrel(World world, float x, float y) {
        super(world, SHAPE);
        addImage(SPRITE.bodyImage());
        setPosition(new Vec2(x, y));
    }

    @Override
    public void addLight(Lighting lighting) {
        Vec2 position = getPosition();
        lighting.shadow(position.x, position.y - BOTTOM, 4);
    }
}
