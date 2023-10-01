package gameControllers;

import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import gameCharacters.Boss;
import gameCharacters.Enemy;
import gameCharacters.PlaneEnemy;
import gameCharacters.Player;

public class RainbowBulletImpact implements CollisionListener {
    Player player;
    public RainbowBulletImpact(Player player)
    {
        this.player = player;
    }

    @Override
    public void collide(CollisionEvent collisionEvent) {
        if(collisionEvent.getOtherBody() instanceof Enemy && !(collisionEvent.getOtherBody() instanceof Boss))
        {


            ((Enemy) collisionEvent.getOtherBody()).reduceHitPoints(1000);
            ((Enemy) collisionEvent.getOtherBody()).setDamagedAnimation();

            if (((Enemy) collisionEvent.getOtherBody()).getHealthPoints() <= 0) {

            } else {
                ((Enemy) collisionEvent.getOtherBody()).damagedSound.play();
            }

            collisionEvent.getReportingBody().destroy();

        }
        if(collisionEvent.getOtherBody() instanceof Boss)
        {
            collisionEvent.getReportingBody().destroy();
            ((Boss)collisionEvent.getOtherBody()).damagedSound.play();
            ((Boss)collisionEvent.getOtherBody()).setDamagedAnimation();

            ((Boss) collisionEvent.getOtherBody()).hp = ((Boss) collisionEvent.getOtherBody()).hp-100;
            ((Boss) collisionEvent.getOtherBody()).reduceHitPoints(100);

        }
    }
}
