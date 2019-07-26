import java.awt.*;

public class Tile {

    protected int x, y;//world coordinates
    public static final int WIDTH = 16, HEIGHT = 16;
    public static final int SCALE = 2;
    public static final int TILE_SIZE = WIDTH*SCALE;
    protected boolean collides = false;
    protected String imgKey = "tile";

    public boolean isInteractable() {
        return interactable;
    }

    public void setInteractable(boolean interactable) {
        this.interactable = interactable;
    }

    protected boolean interactable = false;

    public Rectangle getRect() {
        return rect;
    }

    protected Rectangle rect;
    protected boolean breaks = false;

    public int getHits() {
        return hits;
    }

    public void interact(World world){

    }

    public void hit(int hits) {
        this.hits -= hits;
    }

    protected int hits = 1;

    public Tile(int x, int y){
        this.x = x;
        this.y = y;
        rect = new Rectangle(getPixelX(), getPixelY(), WIDTH*SCALE, HEIGHT*SCALE);
    }

    public void render(Graphics g, Camera camera){
        g.drawImage(Game.imgManager.getImage(imgKey), getPixelX()-camera.getX(), getPixelY()-camera.getY(), WIDTH*SCALE, HEIGHT*SCALE, null);
    }

    public int getPixelX(){
        return x*WIDTH*SCALE;
    }

    public int getPixelY(){
        return y*WIDTH*SCALE;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isCollides() {
        return collides;
    }

    public void setCollides(boolean collides) {
        this.collides = collides;
    }

    public String getImgKey() {
        return imgKey;
    }

    public void setImgKey(String imgKey) {
        this.imgKey = imgKey;
    }
}
