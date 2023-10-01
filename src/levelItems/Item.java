package levelItems;
import city.cs.engine.*;
import city.cs.engine.Shape;
/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description Item for inheritance.
 */
public class Item extends DynamicBody{
    public Item(World w) {
        super(w);
    }

    public Item(World w, Shape s) {
        super(w, s);
    }
}
