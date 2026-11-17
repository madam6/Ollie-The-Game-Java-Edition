package ollie.bullets;

import city.cs.engine.BodyImage;
import city.cs.engine.CollisionListener;
import city.cs.engine.DynamicBody;
import city.cs.engine.Shape;
import city.cs.engine.World;
import ollie.util.Timers;
import org.jbox2d.common.Vec2;

import javax.swing.Timer;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for projectiles. Timers started with {@link #after} are cancelled when the
 * bullet is destroyed, and destroying twice is a no-op. Destroying a JBox2D body twice
 * corrupts the world's body list, and the old code did it whenever a timer fired after a hit.
 */
public abstract class Bullet extends DynamicBody implements CollisionListener {

    private final List<Timer> timers = new ArrayList<>(2);
    private boolean destroyed;

    protected Bullet(World world, Shape shape, Vec2 position, BodyImage sprite) {
        super(world, shape);
        setPosition(position);
        addImage(sprite);
        addCollisionListener(this);
    }

    protected void setSprite(BodyImage sprite) {
        removeAllImages();
        addImage(sprite);
    }

    protected void after(int delayMs, Runnable action) {
        timers.add(Timers.once(delayMs, action));
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public void destroy() {
        if (destroyed) {
            return;
        }
        destroyed = true;
        timers.forEach(Timer::stop);
        super.destroy();
    }
}
