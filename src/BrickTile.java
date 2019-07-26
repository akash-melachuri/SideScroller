import java.awt.*;

public class BrickTile extends Tile {
    public BrickTile(int x, int y) {
        super(x, y);
        collides = true;
        breaks = true;
        hits = 5;
        imgKey = "brick";
    }

    public void render(Graphics g, Camera camera){
        if(imgKey=="shadowTile"){
            super.render(g, camera);
            return;
        }
        g.setColor(Color.gray);
        g.fillRect(getPixelX()-camera.getX(), getPixelY()-camera.getY(), WIDTH*SCALE, HEIGHT*SCALE);
    }
}
