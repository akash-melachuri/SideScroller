import java.awt.*;

public class Item extends Entity {
    String itemId = "TriAttack";
    protected int attack = 20;
    protected int firerate = 20;
    protected int duration = 100;
    protected int maxDuration = 100;
    public Item(float x, float y) {
        super(x, y);
        width = 32;
        height = 32;
        imgKey = "staff";
        updateRect();
    }

    double count = 0.0f;

    public void update(World world){
        rect.y=(int)(Math.sin(count)*25)+(int)y;
        count+=0.015f;
    }

    public void render(Graphics g, Camera camera){
        g.drawImage(Game.imgManager.getImage(imgKey), rect.x-camera.getX(), rect.y-camera.getY(), width, height, null);
    }

    public void attack(){
        Game.player.projectiles.add(new Projectile(Game.player.getX()+Game.player.width/2, Game.player.getY()+Game.player.height/2, Math.atan2(Game.toWorldY(Game.mousePos.y) - (Game.player.y+Game.player.height/2),Game.toWorldX(Game.mousePos.x)-(Game.player.x+Game.player.width/2))));
        Game.player.projectiles.add(new Projectile(Game.player.getX()+Game.player.width/2, Game.player.getY()+Game.player.height/2, Math.atan2(Game.toWorldY(Game.mousePos.y) - (Game.player.y+Game.player.height/2),Game.toWorldX(Game.mousePos.x)-(Game.player.x+Game.player.width/2))+Math.toRadians(20)));
        Game.player.projectiles.add(new Projectile(Game.player.getX()+Game.player.width/2, Game.player.getY()+Game.player.height/2, Math.atan2(Game.toWorldY(Game.mousePos.y) - (Game.player.y+Game.player.height/2),Game.toWorldX(Game.mousePos.x)-(Game.player.x+Game.player.width/2))-Math.toRadians(20)));
    }
}
