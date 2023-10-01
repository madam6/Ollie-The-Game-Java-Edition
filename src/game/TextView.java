package game;

import city.cs.engine.UserView;
import city.cs.engine.World;

import java.awt.*;

public class TextView extends UserView  {

    private Image background;



    public TextView(World w, int width, int height, Image background) {
        super(w, width, height);
        this.background = background;
    }


    @Override
    protected void paintBackground(Graphics2D g) {
        g.drawImage(background, 0, 0, this);
    }

    @Override
    protected void paintForeground(Graphics2D g)
    {

        Font stringFont = new Font( "SansSerif", Font. PLAIN, 24 );
        g.setFont(stringFont);
        g.setColor(Color.yellow);




    }



}

