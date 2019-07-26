import org.w3c.dom.ranges.Range;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class World {
    public static final int WIDTH = 50, HEIGHT = 50;
    public ArrayList<ArrayList<Tile>> tiles = new ArrayList<>();
    public boolean[][] map;
    public ArrayList<Enemy> enemies = new ArrayList<>();
    public ArrayList<Projectile> projectiles = new ArrayList<>();
    public ArrayList<ArrayList<Tile>> objects = new ArrayList<>();
    public ArrayList<Entity> entities = new ArrayList<>();
    protected int baseAttack = 5;

    public World(){
        map = DungeonGenerator.cellularAutomata(WIDTH, HEIGHT, .4f, 5, 4, 10);
        for(int y = 0; y<HEIGHT ;y++){
            tiles.add(new ArrayList<>());
            objects.add(new ArrayList<>());
            for(int x = 0; x<WIDTH; x++){
                int treasureChestLimit = 3;
                if(!map[y][x]&&DungeonGenerator.aliveNeighbors(x, y, map)>treasureChestLimit&&Math.random()>0.75f&&y-1>=0&&map[y-1][x]){
                    objects.get(y).add(new TreasureTile(x, y));
                }else objects.get(y).add(null);
                if(map[y][x]){
                    tiles.get(y).add(new BrickTile(x, y));
                }
                else {
                    if(y>0&&map[y-1][x])tiles.get(y).add(new ShadowTile(x, y));
                    else tiles.get(y).add(new Tile(x, y));
                }
            }
        }
        while(enemies.size()<10){
            int x = (int)(Math.random()*Tile.TILE_SIZE*WIDTH);
            int y = (int)(Math.random()*Tile.TILE_SIZE*HEIGHT);
            Enemy enemy = new Enemy(x, y);
            while(enemy.isColliding(this)||enemy.collideWithEnemies(this)||Game.onScreen(enemy.x, enemy.y, enemy.getWidth(), enemy.getHeight())){
                x = (int)(Math.random()*Tile.TILE_SIZE*WIDTH);
                y = (int)(Math.random()*Tile.TILE_SIZE*HEIGHT);
                enemy.setX(x);
                enemy.setY(y);
                enemy.updateRect();
            }
            enemies.add(enemy);
        }
        while(enemies.size()<20){
            int x = (int)(Math.random()*Tile.TILE_SIZE*WIDTH);
            int y = (int)(Math.random()*Tile.TILE_SIZE*HEIGHT);
            RangedEnemy enemy = new RangedEnemy(x, y);
            while(enemy.isColliding(this)||enemy.collideWithEnemies(this)||Game.onScreen(enemy.x, enemy.y, enemy.getWidth(), enemy.getHeight())){
                x = (int)(Math.random()*Tile.TILE_SIZE*WIDTH);
                y = (int)(Math.random()*Tile.TILE_SIZE*HEIGHT);
                enemy.setX(x);
                enemy.setY(y);
                enemy.updateRect();
            }
            enemies.add(enemy);
        }
    }

    public void levelUp(){
        Enemy.maxAttack+=2;
    }

    public void reset(){
        Enemy.maxAttack=baseAttack;
        RangedEnemy.maxAttack=baseAttack*20;
    }

    public void update(){
        for(Projectile p : projectiles){
            p.update(this);
        }
        Iterator<Projectile> po = projectiles.iterator();
        while(po.hasNext()){
            Projectile pr = po.next();
            if(pr.isDead())po.remove();
        }

        for(Enemy enemy : enemies){
            if(Game.onScreen(enemy.x, enemy.y, enemy.getWidth(), enemy.getHeight()))enemy.activated = true;
            if(enemy.activated)enemy.update(this);
        }
        for(Entity entity:entities){
            if(Game.onScreen(entity.x,entity.y, entity.width,entity.height))entity.update(this);
        }

        Iterator<Enemy> p = enemies.iterator();
        ArrayList<Enemy> babies = new ArrayList<>();
        while(p.hasNext()){
            Enemy pr = p.next();

            if(pr.isDead()){
                if(pr.spawnsBabies){
                    for(int i= -1; i<1; i++){
                        for(int j = -1; j<1; j++){
                            Enemy b=new BabyEnemy(pr.getX()+20*i, pr.getY()+20*j);
                            if(!b.isColliding(this))babies.add(b);
                        }
                    }
                }
                for(int i = 0; i<30; i++){
                    entities.add(new Particle(pr.getX()+pr.getWidth()/2, pr.getY()+pr.getHeight()/2));
                }
                p.remove();
            }
        }

        Iterator<Entity> e = entities.iterator();
        while(e.hasNext()){
            Entity er = e.next();
            if(er.isDead())e.remove();
        }
        for(Enemy enemy : babies)enemies.add(enemy);
    }

    public void render(Graphics g, Camera camera){
        for(int y = 0; y<HEIGHT ;y++){
            for(int x = 0; x<WIDTH; x++){
                Tile tile = tiles.get(y).get(x);
                if(!(tile.getPixelX()-camera.getX()+(Tile.TILE_SIZE)<0||tile.getPixelX()-camera.getX()>Game.WIDTH||tile.getPixelY()-camera.getY()+(Tile.TILE_SIZE)<0||tile.getPixelY()-camera.getY()>Game.HEIGHT)) tiles.get(y).get(x).render(g, camera);
            }
        }
        for(int y = 0; y<HEIGHT; y++){
            for(int x = 0; x<WIDTH; x++){
                Tile tile = objects.get(y).get(x);
                if(tile!=null&&Game.onScreen(tile.getPixelX(), tile.getPixelY(), Tile.TILE_SIZE, Tile.TILE_SIZE))tile.render(g, camera);
            }
        }

        for(Enemy enemy : enemies){
            if(Game.onScreen(enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight()))enemy.render(g, camera);
        }
        for(Projectile p:projectiles){
            p.render(g, camera);
        }
        for(Entity entity : entities){
            if(Game.onScreen(entity.x,entity.y, entity.width,entity.height)){
                entity.render(g, camera);
            }
        }
    }

    public Tile getTile(int x, int y){
        if(x<0||y<0||x>=WIDTH||y>=HEIGHT)return null;
        return tiles.get(y).get(x);
    }

    public Tile getObject(int x, int y){
        if(x<0||y<0||x>=WIDTH||y>=HEIGHT)return null;
        return objects.get(y).get(x);
    }

    public void setTile(int x, int y, Tile tile){
        if(x<0||y<0||x>=WIDTH||y>=HEIGHT)return;
        tiles.get(y).set(x, tile);
    }

}
