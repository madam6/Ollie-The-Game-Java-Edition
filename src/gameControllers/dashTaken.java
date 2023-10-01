package gameControllers;

import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import city.cs.engine.SoundClip;
import gameCharacters.Player;
import levelItems.DashToken;

public class dashTaken implements CollisionListener {

    DashToken dashToken;
    SoundClip tokenTaken;

    public dashTaken(DashToken dashToken)
    {
        this.dashToken=dashToken;
        try {
            tokenTaken = new SoundClip("data/music/dashToken.wav");
        }
        catch (Exception e)
        {

        }
    }
    @Override
    public void collide(CollisionEvent collisionEvent) {
        if(collisionEvent.getOtherBody() instanceof Player)
        {
            dashToken.takeCollectible();
            ((Player) collisionEvent.getOtherBody()).addAmountOfDashes(5);
            tokenTaken.play();
        }
        else
        {
            dashToken.shapeToNone();
        }
    }
}
