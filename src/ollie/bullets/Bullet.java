package ollie.bullets;

import city.cs.engine.CollisionListener;
import city.cs.engine.DynamicBody;
import city.cs.engine.Shape;
import city.cs.engine.World;
import ollie.levels.GameLevel;
import ollie.ui.Lighting;
import ollie.ui.Sprite;
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
public abstract class Bullet extends DynamicBody implements CollisionListener, Lighting.LightSource {

    private final List<Timer> timers = new ArrayList<>(2);
    private boolean destroyed;

    protected Bullet(World world, Shape shape, Vec2 position, Sprite sprite) {
        super(world, shape);
        setPosition(position);
        addImage(sprite.bodyImage());
        addCollisionListener(this);
    }

    protected void setSprite(Sprite sprite) {
        removeAllImages();
        addImage(sprite.bodyImage());
    }

    protected void after(int delayMs, Runnable action) {
        timers.add(Timers.once(delayMs, action));
    }

    protected void shakeScreen(float pixels, int durationMs) {
        if (getWorld() instanceof GameLevel level) {
            level.getScreenShake().shake(pixels, durationMs);
        }
    }

    /** Bullets give off no light unless a subclass says otherwise. */
    @Override
    public void addLight(Lighting lighting) {
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
