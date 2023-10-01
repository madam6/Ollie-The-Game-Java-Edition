package gameControllers;

import bullets.burgerBullet;
import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import city.cs.engine.SoundClip;
import gameCharacters.Player;

public class BurgerBulletImpact implements CollisionListener {

    burgerBullet burger;
    SoundClip eaten, playerDamaged;
    public BurgerBulletImpact(burgerBullet bullet)
    {
        burger=bullet;
        try {
            eaten = new SoundClip("data/music/eaten.wav");
            playerDamaged = new SoundClip("data/music/damaged.wav");
        }
        catch (Exception e)
        {

        }
    }
    @Override
    public void collide(CollisionEvent collisionEvent) {
        if(collisionEvent.getOtherBody() instanceof Player )
        {
            if(collisionEvent.getOtherBody() instanceof Player)
            {
                ((Player) collisionEvent.getOtherBody()).reduceHP(10);
                burger.beEaten();
                eaten.play();
                playerDamaged.play();
            }
            else
            {
                burger.beEaten();
                eaten.play();
            }
        }
    }
}
