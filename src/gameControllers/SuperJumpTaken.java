package gameControllers;

import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import city.cs.engine.SoundClip;
import gameCharacters.Player;
import levelItems.SuperJumpToken;

public class SuperJumpTaken implements CollisionListener {
    SuperJumpToken superJumpToken;
    SoundClip tokenTaken;

    public SuperJumpTaken(SuperJumpToken superJumpToken)
    {
        this.superJumpToken = superJumpToken;
        try {
            tokenTaken = new SoundClip("data/music/superJump.wav");
        }
        catch (Exception e)
        {

        }
    }
    @Override
    public void collide(CollisionEvent collisionEvent) {
        if(collisionEvent.getOtherBody() instanceof Player)
        {
            superJumpToken.takeCollectible();
            ((Player) collisionEvent.getOtherBody()).jump(20);
            tokenTaken.play();
        }
        else
        {
            superJumpToken.shapeToNone();
        }
    }
}
