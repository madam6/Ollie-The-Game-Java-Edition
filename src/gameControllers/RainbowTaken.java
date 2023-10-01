package gameControllers;

import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import city.cs.engine.SoundClip;
import gameCharacters.Player;
import levelItems.RainbowToken;

public class RainbowTaken implements CollisionListener {
    RainbowToken rainbowToken;
    SoundClip tokenTaken;

    public RainbowTaken(RainbowToken rainbowToken)
    {
        this.rainbowToken = rainbowToken;
        try {
            tokenTaken = new SoundClip("data/music/rainbowToken.wav");
        }
        catch (Exception e)
        {

        }
    }
    @Override
    public void collide(CollisionEvent collisionEvent) {
        if(collisionEvent.getOtherBody() instanceof Player)
        {
            rainbowToken.takeCollectible();
            ((Player) collisionEvent.getOtherBody()).ammo = ((Player) collisionEvent.getOtherBody()).ammo+2;
            tokenTaken.play();
        }
        else
        {
            rainbowToken.shapeToNone();
        }
    }
}
