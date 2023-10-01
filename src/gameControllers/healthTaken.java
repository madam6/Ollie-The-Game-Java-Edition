package gameControllers;

import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import city.cs.engine.SoundClip;
import gameCharacters.Player;
import levelItems.HealthToken;

public class healthTaken implements CollisionListener {
    HealthToken healthToken;
    SoundClip tokenTaken;

    public healthTaken(HealthToken healthToken)
    {
        this.healthToken=healthToken;
        try {
            tokenTaken = new SoundClip("data/music/heal.wav");
        }
        catch (Exception e)
        {

        }
    }

    @Override
    public void collide(CollisionEvent collisionEvent) {
        if(collisionEvent.getOtherBody() instanceof Player)
        {
            healthToken.takeCollectible();
            ((Player) collisionEvent.getOtherBody()).addHP(150);
            tokenTaken.play();
        }
        else
        {
            healthToken.shapeToNone();
        }
    }
}
