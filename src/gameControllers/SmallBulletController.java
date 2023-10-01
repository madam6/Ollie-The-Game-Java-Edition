package gameControllers;

import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import city.cs.engine.SoundClip;
import city.cs.engine.StaticBody;
import gameCharacters.Boss;
import gameCharacters.Enemy;
import gameCharacters.Player;

public class SmallBulletController implements CollisionListener {
    Player player;
    SoundClip damaged;
    public SmallBulletController(Player player)
    {
        this.player = player;
        try {
            damaged = new SoundClip("data/music/newAttackHit.wav");
        }
        catch (Exception e)
        {

        }
    }

    @Override
    public void collide(CollisionEvent collisionEvent) {
        if(collisionEvent.getOtherBody() instanceof Enemy && !(collisionEvent.getOtherBody() instanceof Boss))
        {
            ((Enemy) collisionEvent.getOtherBody()).reduceHitPoints(8);
            ((Enemy) collisionEvent.getOtherBody()).setDamagedAnimation();
            damaged.play();
                if(((Enemy)collisionEvent.getOtherBody()).getHealthPoints()<=0)
                {

                }
                else {
                    ((Enemy)collisionEvent.getOtherBody()).damagedSound.play();
                }
            collisionEvent.getReportingBody().destroy();

        }
        if(collisionEvent.getOtherBody() instanceof Boss)
        {
            collisionEvent.getReportingBody().destroy();
            ((Boss)collisionEvent.getOtherBody()).damagedSound.play();
            ((Boss)collisionEvent.getOtherBody()).setDamagedAnimation();

            ((Boss) collisionEvent.getOtherBody()).hp = ((Boss) collisionEvent.getOtherBody()).hp-50;
            ((Boss) collisionEvent.getOtherBody()).hp = ((Boss) collisionEvent.getOtherBody()).hp-50;
            ((Boss) collisionEvent.getOtherBody()).hp = ((Boss) collisionEvent.getOtherBody()).hp-50;
        }
        if(collisionEvent.getOtherBody() instanceof StaticBody)
        {
            damaged.play();
            collisionEvent.getReportingBody().destroy();
        }
    }
}
