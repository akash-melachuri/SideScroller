import java.awt.*;

public class TreasureTile extends Tile {
    boolean open = false;
    boolean empty = false;
    protected int drop = 0;
    public TreasureTile(int x, int y) {
        super(x, y);
        interactable = true;
        collides = false;
        breaks = false;
        imgKey = "treasureChest";
        rect.height = 21;
        rect.y +=11;
        drop = (int)(Math.random()*2);
    }
    public void interact(World world){
        if(!empty){
            if(drop==0)world.entities.add(new Item(getPixelX(), getPixelY()));
            else if(drop==1)world.entities.add(new PiercingStaff(getPixelX(), getPixelY()));
        }
        empty = true;
        open = !open;
    }

    @Override
    public void render(Graphics g, Camera camera) {
        if(open){
            imgKey = "treasureChestOpen";
        }
        else {
            imgKey = "treasureChest";
        }
        super.render(g, camera);
    }
}
