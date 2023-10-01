package gameControllers;

import bullets.SuperBullet;
import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import city.cs.engine.SoundClip;
import city.cs.engine.World;
import gameCharacters.Player;
import levelItems.MassAttackToken;

public class MassAttackTaken implements CollisionListener {
    MassAttackToken massAttackToken;
    SoundClip tokenTaken;
    World world;
    public MassAttackTaken(MassAttackToken massAttackToken, World world)
    {
        this.massAttackToken = massAttackToken;
        this.world = world;
        try {
            tokenTaken = new SoundClip("data/music/massAttackLoad.wav");
            tokenTaken.setVolume(2);
        }
        catch (Exception e)
        {

        }
    }
    @Override
    public void collide(CollisionEvent collisionEvent) {
        if(collisionEvent.getOtherBody() instanceof Player)
        {
            massAttackToken.takeCollectible();
            SuperBullet superBullet = new SuperBullet(world, massAttackToken.getPosition());
            tokenTaken.play();
        }
        else
        {
            massAttackToken.shapeToNone();
        }
    }
}
