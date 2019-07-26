import java.awt.*;

public class Particle extends Entity {

    int lifeSpan;
    float velx;
    float vely;
    Color color;

    public Particle(float x, float y){
        super(x, y);
        int s = (int)(Math.random()*10);
        rect.width = s;
        rect.height = s;
        int r = (int)(Math.random()*256);
        int g = (int)(Math.random()*256);
        int b = (int)(Math.random()*256);
        color = new Color(r, g, b);
        velx = ((float)(Math.random()*3))-1.5f;
        vely = ((float)(Math.random()*3))-1.5f;
        lifeSpan = (int)(Math.random()*100);
    }

    public void update(World world){
        lifeSpan--;
        if(lifeSpan<=0)dead = true;
        x+=velx;
        y+=vely;
        super.update(world);
    }

    public void render(Graphics g, Camera camera){
        g.setColor(color);
        g.fillRect(rect.x-camera.getX(), rect.y-camera.getY(), rect.width, rect.height);
    }


}
