import java.awt.*;

public class Projectile extends Entity {

    public static int WIDTH = 5, HEIGHT = 5;
    protected float speed = 3.0f;
    protected double angle;
    protected String imgKey = "projectile";
    protected boolean destructable = true;
    protected int blockDamage = 1;

    public Projectile(float x, float y, double angle){
        super(x, y);
        rect = new Rectangle((int)x, (int)y, WIDTH, HEIGHT);
        this.angle = angle;
    }

    @Override
    public void update(World world) {
        if(isColliding(world)){
            dead = true;
            if(collidingTile.collides&&destructable&&collidingTile.breaks){
                world.getTile(collidingTile.getX(),collidingTile.getY()).hit(blockDamage);
                if(world.getTile(collidingTile.getX(), collidingTile.getY()).getHits()<=0){
                    world.setTile(collidingTile.getX(), collidingTile.getY(), new Tile(collidingTile.getX(), collidingTile.getY()));
                    if(world.getTile(collidingTile.getX(), collidingTile.getY()+1)!=null&&world.getTile(collidingTile.getX(), collidingTile.getY()+1).imgKey=="shadowTile")
                        world.getTile(collidingTile.getX(), collidingTile.getY()+1).imgKey="tile";
                    if(world.getTile(collidingTile.getX(), collidingTile.getY()-1)!=null&&world.getTile(collidingTile.getX(), collidingTile.getY()-1).imgKey=="brick")
                        world.setTile(collidingTile.getX(), collidingTile.getY(), new ShadowTile(collidingTile.getX(), collidingTile.getY()));
                    for(Enemy enemy : world.enemies)enemy.shouldPathfind = true;
                }
            }
        }
        for(Enemy enemy : world.enemies){
            if(rect.intersects(enemy.rect)){
                dead = true;
                if(Game.player.equippedItem==null)enemy.takeHits(Game.player.attack);
                else enemy.takeHits(Game.player.equippedItem.attack);
            }
        }
        x+=Math.cos(angle)*speed;
        y+=Math.sin(angle)*speed;
        super.update(world);
    }

    public void render(Graphics g, Camera camera){
        if((int)x-camera.getX()>0-rect.width&&(int)x-camera.getX()<Game.WIDTH&&(int)y-camera.getY()>0-rect.height&&(int)y-camera.getY()<Game.HEIGHT){
            g.drawImage(Game.imgManager.getImage(imgKey), (int)x-camera.getX(), (int)y-camera.getY(), rect.width, rect.height,null);
        }else{
            dead = true;
        }
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}
