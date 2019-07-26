import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class Player extends Entity{
    
    private float speed = 2.0f;

    private int firerate = 15;

    protected ArrayList<Projectile> projectiles = new ArrayList<>();

    protected String currentImgKey = "playerDown";
    protected int maxHits = 100;
    protected int regen = 30;
    protected boolean triattack = false;
    protected boolean piercingAttack = false;
    protected Item equippedItem = null;
    protected ArrayList<Item> items = new ArrayList<>();
    protected int currentItem = 1;

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    protected int attack = 20;

    public Player(float x, float y){
        super(x, y);
        width = 20;
        height = 20;
        rect = new Rectangle((int) x, (int) y, width, height);
        hits = maxHits;
    }

    public void reset(float x, float y){
        this.x = x;
        this.y = y;
        updateRect();
        hits = maxHits;
    }


    public void update(World world){
        if(animCount%regen==0&&hits<maxHits)hits++;
        if(Game.keys[Game.W]||Game.keys[Game.UP]){
            y-=speed;
        }
        updateRect();
        if(isColliding(world))y = (float)collidingTile.rect.getY()+collidingTile.rect.height;


        if(Game.keys[Game.S]||Game.keys[Game.DOWN]){
            y+=speed;
        }
        updateRect();
        if(isColliding(world))y = (float)collidingTile.rect.getY()-height;


        if(Game.keys[Game.A]||Game.keys[Game.LEFT]){
            x-=speed;
        }
        updateRect();
        if(isColliding(world))x = collidingTile.getPixelX()+Tile.TILE_SIZE;


        if(Game.keys[Game.D]||Game.keys[Game.RIGHT]){
            x+=speed;
        }
        updateRect();
        if(isColliding(world))x = collidingTile.getPixelX()-width;


        if(x>Tile.TILE_SIZE*World.WIDTH-width)x=Tile.TILE_SIZE*World.WIDTH-width;
        if(x<0)x=0;
        if(y>Tile.TILE_SIZE*World.HEIGHT-height)y = Tile.TILE_SIZE*World.HEIGHT-height;
        if(y<0)y=0;
        if(Game.mouseLeft&&animCount%firerate==0&&equippedItem==null){
            projectiles.add(new Projectile(getX()+width/2, getY()+height/2, Math.atan2(Game.toWorldY(Game.mousePos.y) - (y+height/2),Game.toWorldX(Game.mousePos.x)-(x+width/2))));
        }
        if(Game.mouseLeft&&equippedItem!=null&&animCount%equippedItem.firerate==0){
            equippedItem.attack();
            equippedItem.duration--;
            if(equippedItem.duration<=0)equippedItem=null;
        }

        for(Enemy enemy : world.enemies){
            if(enemy.calculateH((int)enemy.getX(),(int) enemy.getY(),(int) x,(int) y)<=enemy.range*Tile.TILE_SIZE&&enemy.animCount%enemy.firerate==0&&enemy.melee){
                takeHits(enemy.attack);
                //Play enemy attack animation
            }
        }

        for(Entity entity : world.entities){
            if(entity instanceof Item){
                if(rect.intersects(entity.rect)){
                    entity.dead = true;
                    items.add((Item)entity);
                    equippedItem = (Item) entity;
                }
            }
        }

        if(hits<=0){
            hits=0;
            Game.gameOver = true;
        }

        for(Projectile p : projectiles){
            p.update(world);
        }
        Iterator<Projectile> p = projectiles.iterator();
        while(p.hasNext()){
            Projectile pr = p.next();
            if(pr.isDead())p.remove();
        }
        super.update(world);
        Game.PLAYER_WIDTH = width;
        Game.PLAYER_HEIGHT = height;
    }

    protected int healthBarHeight = 10;
    protected int healthBarWidth = 40;
    public void render(Graphics g, Camera camera){
        for(Projectile p:projectiles){
            p.render(g, camera);
        }
        g.drawImage(Game.imgManager.getImage(currentImgKey),(int)x-camera.getX(), (int)y-camera.getY(), width, height,null);
        g.setColor(Color.GREEN);
        g.fillRect((int)(getX()+getWidth()/2)- healthBarWidth/2-camera.getX(), (int)getY()-15-camera.getY(),(int)(healthBarWidth*((float)hits/maxHits)), healthBarHeight);
        g.setColor(Color.RED);
        g.fillRect((int)(getX()+getWidth()/2)- healthBarWidth/2-camera.getX()+(int)(healthBarWidth*((float)hits/maxHits)), (int)getY()-15-camera.getY(),(int)(healthBarWidth*((float)(maxHits-hits)/maxHits)), healthBarHeight);
        g.setColor(Color.CYAN);
        g.drawRect(5,Game.HEIGHT-50, 45, 45);
        if(equippedItem!=null){
            g.drawImage(Game.imgManager.getImage(equippedItem.imgKey), 5, Game.HEIGHT-50, 45,45,null);
            g.setColor(Color.GREEN);
            g.fillRect(5, Game.HEIGHT-15,(int)(45*((float)equippedItem.duration/equippedItem.maxDuration)), 10);
            g.setColor(Color.RED);
            g.fillRect(5+(int)(45*((float)equippedItem.duration/equippedItem.maxDuration)), Game.HEIGHT-15,45-(int)(45*((float)equippedItem.duration/equippedItem.maxDuration)), 10);
        }

    }

    public boolean isInCenterOfScreen(){
        return x==Game.WIDTH/2-width&&y==Game.HEIGHT/2-height;
    }

    public float getSpeed() {
        return speed;
    }
}
