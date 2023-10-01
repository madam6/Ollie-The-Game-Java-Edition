package gameControllers;

import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import city.cs.engine.SoundClip;
import city.cs.engine.StaticBody;
import gameCharacters.Player;

public class SmallBulletControllerEnemy implements CollisionListener {
    Player player;
    SoundClip damaged, playerDamaged;
    public SmallBulletControllerEnemy(Player player)
    {
        this.player = player;
        try {
            damaged = new SoundClip("data/music/newAttackHit.wav");
            playerDamaged = new SoundClip("data/music/damaged.wav");
        }
        catch (Exception e)
        {

        }
    }

    @Override
    public void collide(CollisionEvent collisionEvent) {
        if(collisionEvent.getOtherBody() instanceof Player)
        {
            ((Player) collisionEvent.getOtherBody()).reduceHP(10);
            ((Player) collisionEvent.getOtherBody()).setDamaged(true);
            playerDamaged.play();
            damaged.play();
            if(((Player)collisionEvent.getOtherBody()).getHealthPoints()<=0)
            {

            }
            else {
                ((Player)collisionEvent.getOtherBody()).damaged.play();
            }
            collisionEvent.getReportingBody().destroy();

        }
        if(collisionEvent.getOtherBody() instanceof StaticBody)
        {
            damaged.play();
            collisionEvent.getReportingBody().destroy();
        }
    }
}
