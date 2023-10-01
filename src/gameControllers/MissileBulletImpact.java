package gameControllers;

import bullets.missileBullet;
import city.cs.engine.*;
import gameCharacters.Player;
import levelItems.Barrel;

public class MissileBulletImpact implements CollisionListener {

    missileBullet missile;
    SoundClip explosion, playerDamaged;
    public MissileBulletImpact(missileBullet bullet)
    {
        missile=bullet;
        try {
            explosion = new SoundClip("data/music/misstleExplosion.wav");
            playerDamaged = new SoundClip("data/music/damaged.wav");
        }
        catch (Exception e)
        {

        }
    }

    @Override
    public void collide(CollisionEvent collisionEvent) {
        if(collisionEvent.getOtherBody() instanceof Player || collisionEvent.getOtherBody() instanceof StaticBody || collisionEvent.getOtherBody() instanceof Barrel)
        {
            if(collisionEvent.getOtherBody() instanceof Player)
            {
                ((Player) collisionEvent.getOtherBody()).reduceHP(1000);
                playerDamaged.play();
                ((Player) collisionEvent.getOtherBody()).setDamaged(true);
                missile.explode();
                explosion.play();
            }
            else
            {
                missile.explode();
                explosion.play();
            }
        }
    }
}

