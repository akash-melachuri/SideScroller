public class Camera {

    private int x, y;

    public Camera(){
        x = 0;
        y = 0;
    }

    public void update(Player player){
        x = (int) player.getX()-(Game.WIDTH/2-player.getWidth()/2);
        y = (int) player.getY()-(Game.HEIGHT/2-player.getHeight()/2);

        if(y<0)y=0;
        if(x<0)x=0;
        if(y>World.HEIGHT*Tile.TILE_SIZE-Game.HEIGHT)y=World.HEIGHT*Tile.TILE_SIZE-Game.HEIGHT;
        if(x>World.WIDTH*Tile.TILE_SIZE-Game.WIDTH)x=World.WIDTH*Tile.TILE_SIZE-Game.WIDTH;
    }

    public Camera(int x, int y) {
        this.x = x;
        this.y = y;
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
}
