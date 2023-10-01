package levelItems;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;
/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description Barrel as a part of level`s interior.
 */
public class Barrel extends Item{

    protected static Shape barrelShape = new PolygonShape(1.08f,-3.44f, 1.36f,-0.48f, 0.87f,0.7f, -0.04f,0.92f, -1.3f,0.24f, -1.57f,-2.58f, -1.09f,-3.5f);
    BodyImage barrelImage = new BodyImage("data/barrel.png", 7);

    public Barrel(World w, Vec2 position) {
        super(w, barrelShape);
        addImage(barrelImage);
        this.setPosition(position);
    }

    public Barrel(World w, Shape s) {
        super(w, s);
    }
}
