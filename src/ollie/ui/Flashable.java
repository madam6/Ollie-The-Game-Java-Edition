package ollie.ui;

import org.jbox2d.common.Vec2;

/** A body that flashes white for a moment when it gets hit. */
public interface Flashable {

    Vec2 getPosition();

    Sprite getSprite();

    /** 0 when not flashing, up to 1 right after a hit. */
    float flashAmount();
}
