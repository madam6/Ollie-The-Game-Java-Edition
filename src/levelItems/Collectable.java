package levelItems;

import city.cs.engine.BodyImage;
import city.cs.engine.Shape;
import city.cs.engine.StaticBody;
import city.cs.engine.World;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
/**
 * @author      Artem Korniienko, artem.korniienko@city.ac.uk
 * @version     3.0
 * @description Inheritance class for collectibles.
 * sets all needed animations, pictures.
 */
public class Collectable extends StaticBody implements ActionListener {

    private Timer spawnTimer, destroyTimer = new Timer(10000, this);

    protected BodyImage collectableAnimation;
    protected BodyImage takenAnimation;
    protected Shape collectableShape;

    public Collectable(World w,  BodyImage cA, BodyImage tA, Shape cSh) {
        super(w, cSh);
        collectableAnimation = cA;
        takenAnimation = tA;
        collectableShape = cSh;
        addImage(cA);
        destroyTimer.setRepeats(false);
        destroyTimer.start();
    }
    public void setPos(Vec2 newPos)
    {
        this.setPosition(newPos);
    }
    public void spawner() {

        Random ran = new Random();

        int xPos = ran.nextInt(-16, 16);

        this.setPosition(new Vec2(xPos, -13));

    }
    public void shapeToNone()
    {
        this.collectableShape = null;
    }
    public void takeCollectible()
    {
        this.removeAllImages();
        this.addImage(takenAnimation);
        this.destroy();
        destroyTimer.start();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        this.destroy();
    }
}
