import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class Entity {

    protected Rectangle rect;
    protected int animCount = 0;
    protected  int hits = 5;

    public boolean isDead() {
        return dead;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    protected int width = 20, height = 20;
    String imgKey = "tile";
    public boolean dead = false;

    public Entity(){
        x = 0;
        y = 0;
        rect = new Rectangle((int)x, (int)y, width, height);
    }

    public Entity(float x, float y){
        this.x = x;
        this.y = y;
        rect = new Rectangle((int)x, (int)y, width, height);
    }

    public void update(World world){
        updateRect();
        animCount++;
        if(animCount>=1000)animCount=0;
    }

    public void updateRect(){
        rect.x = (int)x;
        rect.y = (int)y;
    }

    Tile collidingTile = null;
    public boolean isColliding(World world){
        ArrayList<Tile> possible = new ArrayList<>();
        int xa = (int)x/Tile.TILE_SIZE-1;
        int ya = (int)y/Tile.TILE_SIZE-1;
        for(int y = 0; y<3; y++){
            for(int x = 0; x<3; x++){
                if(world.getTile(xa+x,ya+y)!=null)possible.add(world.getTile(xa+x, ya+y));
                if(world.getObject(xa+x, ya+y)!=null)possible.add(world.getObject(xa+x, ya+y));
            }
        }
        for(Tile tile : possible){
            if(tile.getRect().intersects(rect)&&tile.collides){
                collidingTile = tile;
                return true;
            }
        }

        collidingTile = null;
        return false;
    }

    Line2D line = null;
    public boolean hasLineOfSight(int tx, int ty, World world){
        line = new Line2D.Float(x+getWidth()/2, y+getHeight()/2, tx, ty);
        for(int y = 0; y<world.HEIGHT; y++){
            for(int x = 0; x<world.WIDTH; x++){
                Tile tile = world.getTile(x, y);
                if(Game.onScreen(tile.getPixelX(), tile.getPixelY(), Tile.TILE_SIZE, Tile.TILE_SIZE)&&tile.collides&&line.intersects(tile.rect)){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isColliding(World world, Rectangle r){
        ArrayList<Tile> possible = new ArrayList<>();
        int xa = rect.x/Tile.TILE_SIZE-1;
        int ya = rect.y/Tile.TILE_SIZE-1;
        for(int y = 0; y<3; y++){
            for(int x = 0; x<3; x++){
                if(world.getTile(xa+x,ya+y)!=null)possible.add(world.getTile(xa+x, ya+y));
            }
        }
        for(Tile tile : possible){
            if(tile.getRect().intersects(r)&&tile.collides){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Tile> getTiles(World world){
        ArrayList<Tile> possible = new ArrayList<>();
        ArrayList<Tile> ret = new ArrayList<>();
        int xa = (int)x/Tile.TILE_SIZE-1;
        int ya = (int)y/Tile.TILE_SIZE-1;
        for(int y = 0; y<3; y++){
            for(int x = 0; x<3; x++){
                if(world.getTile(xa+x,ya+y)!=null)possible.add(world.getTile(xa+x, ya+y));
            }
        }
        for(Tile tile : possible){
            if(tile.getRect().intersects(rect)){
                if(!ret.contains(tile))ret.add(tile);
            }
        }
        return ret;
    }

    public void render(Graphics g, Camera camera){
        g.drawImage(Game.imgManager.getImage(imgKey), (int)x-camera.getX(), (int)y-camera.getY(), width, height, null);
    }

    protected float x, y;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        rect.x = (int) x;
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        rect.y = (int) y;
        this.y = y;
    }

    public int getHits() {
        return hits;
    }

    public void takeHits(int damage){
        this.hits-=damage;
    }

    public void heal(int health){
        this.hits+=health;
    }
}
