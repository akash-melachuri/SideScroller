import org.w3c.dom.css.Rect;
import sun.awt.image.ImageWatched;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Enemy extends Entity {
    protected int range = 2;
    protected boolean melee = true;
    protected int firerate = 30;
    protected boolean inRange = false;
    protected boolean spawnsBabies = true;
    protected int maxHits = 100;
    public static int maxAttack = 3;
    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    protected int attack = maxAttack;

    public Enemy(float x, float y){
        super(x, y);
        width = 30;
        height = 30;
        rect = new Rectangle((int)x,(int)y, width, height);
        imgKey = "enemy";
        hits = maxHits;
    }

    float speed = 1;
    int pathFindingFrequency = 25;
    boolean activated = false;
    List<Node> path;
    boolean shouldPathfind = true;
    protected int damageInWallFrequency = 30;

    public void update(World world){
        Rectangle init = rect;
        if(hits<=0){
            dead = true;
            return;
        }
        List<Node> pathI = null;
        if(shouldPathfind&&animCount%pathFindingFrequency==0&&!inRange){
            pathI = pathFind(world);
        }
        if(pathI!=null&&!pathI.isEmpty()&&!inRange){
            path = pathI;
            if(pathI==null)shouldPathfind=false;
        }
        if(path!=null&&!path.isEmpty()&&!inRange){
            Tile tile = path.get(0).tile;
            double angle = Math.atan2((tile.getPixelY()+Tile.TILE_SIZE/2)-(y+getHeight()/2), (tile.getPixelX()+Tile.TILE_SIZE/2)-(x+getWidth()/2));
            x+=Math.cos(angle)*speed;
            updateRect();
            if(isColliding(world)||collideWithEnemies(world)) {
                x-=Math.cos(angle)*speed;
            }
            y+=Math.sin(angle)*speed;
            updateRect();
            if(isColliding(world)||collideWithEnemies(world)){
                y-=Math.sin(angle)*speed;
            }
            updateRect();
        }

        if(init==rect&&collideWithEnemies(world)){
            dead = true;
        }
        if(init == rect&&isColliding(world)&&animCount%damageInWallFrequency==0)takeHits(1);
        super.update(world);
    }
    Enemy collidedEnemy=null;
    public boolean collideWithEnemies(World world){
        for(Enemy enemy : world.enemies){
            if(enemy.activated&&!enemy.equals(this)){
                if(rect.intersects(enemy.rect)){
                    collidedEnemy = enemy;
                    return true;
                }
            }
        }
        collidedEnemy = null;
        return false;
    }

    public boolean smoothPath(List<Node> pathI, World world){
        Tile checkPoint = world.getTile((int)(getX()+getWidth()/2)/Tile.TILE_SIZE, (int)(getY()+getHeight()/2)/Tile.TILE_SIZE);;
        Iterator<Node> it = pathI.iterator();
        Node current = null;
        if(it.hasNext()) current = it.next();
        while(it.hasNext()){
            Node end = it.next();
            if(walkable(checkPoint, end.tile,world)){
                current = end;
                it.remove();
            }else{
                checkPoint = current.tile;
                current = end;
            }
        }

        return true;
    }

    public boolean walkable(Tile start, Tile end, World world){
        double angle = Math.atan2(end.getPixelY()-start.getPixelY(), end.getPixelX()-start.getPixelX());
        Rectangle r = rect;
        while(!r.intersects(end.rect)){
            r.x+=Math.cos(angle)*speed*3;
            r.y+=Math.sin(angle)*speed*3;
            if(isColliding(world, r))return false;
        }
        return true;
    }
    protected int healthBarHeight = 10;
    protected int healthBarWidth = 40;
    public void render(Graphics g, Camera camera){
        g.drawImage(Game.imgManager.getImage(imgKey), (int)x-camera.getX(), (int)y-camera.getY(), width, height, null);

        if(hits<maxHits){
            g.setColor(Color.GREEN);
            g.fillRect((int)(getX()+getWidth()/2)- healthBarWidth/2-camera.getX(), (int)getY()-15-camera.getY(),(int)(healthBarWidth*((float)hits/maxHits)), healthBarHeight);
            g.setColor(Color.RED);
            g.fillRect((int)(getX()+getWidth()/2)- healthBarWidth/2-camera.getX()+(int)(healthBarWidth*((float)hits/maxHits)), (int)getY()-15-camera.getY(),(int)(healthBarWidth*((float)(maxHits-hits)/maxHits)), healthBarHeight);
        }

        //        if(path!=null&&!path.isEmpty()){
//            for(Node node : path){
//                g.setColor(Color.red);
//                g.fillRect(node.tile.getPixelX()-camera.getX(), node.tile.getPixelY()-camera.getY(), Tile.TILE_SIZE, Tile.TILE_SIZE);
//            }
//        }
    }

    public List<Node> constructPath(Node node){
        LinkedList<Node> path = new LinkedList<>();
        while(node.parent!=null){
            path.addFirst(node);
            node = node.parent;
        }
        return path;
    }
    Point[] points = {new Point(0, -1), new Point(1, 0), new Point(0, 1), new Point(-1, 0),
            new Point(-1, -1), new Point(1, -1), new Point(1, 1), new Point(-1, 1)};
    public List<Node> pathFind(World world){
        Tile tile = world.getTile((int)(getX()+getWidth()/2)/Tile.TILE_SIZE, (int)(getY()+getHeight()/2)/Tile.TILE_SIZE);
        Node start = new Node();
        start.tile = tile;
        start.f = 0;
        start.g = 0;
        start.h = 0;
        start.parent = null;
        Node goal = new Node();
        goal.tile = world.getTile((int)(Game.player.getX()+Game.player.getWidth()/2)/Tile.TILE_SIZE, (int)(Game.player.getY()+Game.player.getHeight()/2)/Tile.TILE_SIZE);
        goal.g = 0;
        if(start.tile==null||goal.tile==null)return null;
        goal.h = calculateH(start.tile.getX(), start.tile.getY(), goal.tile.getX(), goal.tile.getY())*10;
        goal.f = goal.g+goal.h;
        ArrayList<Node> closed = new ArrayList<>();
        ArrayList<Node> open = new ArrayList<>();
        open.add(start);
        while(!open.isEmpty()){
            Node node = open.get(0);
            for(Node q : open){
                if(q.f<node.f)node = q;
            }
            open.remove(node);
            if(node.equals(goal)){
                return constructPath(node);
            }else{
                closed.add(node);
                for(Point p : points){
                    int x = (int)p.getX();
                    int y = (int)p.getY();
                    Node i = new Node();
                    i.tile = world.getTile(node.tile.getX()+x, node.tile.getY()+y);
                    if(i.tile==null){

                    } else if(x!=0&&y!=0){
                        i.g = node.g+14;
                        i.h = calculateH(i.tile.getX(), i.tile.getY(), goal.tile.getX(), goal.tile.getY())*10;
                        i.f = i.g+i.h;
                        Tile bottom = world.getTile(node.tile.getX(), node.tile.getY()+y);
                        Tile side = world.getTile(node.tile.getX()+x, node.tile.getY());
                        Tile top = world.getTile(node.tile.getX(), node.tile.getY()-y);
                        Tile otherSide = world.getTile(node.tile.getX()-x, node.tile.getY());

                        if(!closed.contains(i)&&!open.contains(i)&&i.tile!=null&&!i.tile.collides&&bottom!=null&&side!=null&&!bottom.collides&&!side.collides&&top!=null&&!top.collides&&otherSide!=null&&!otherSide.collides){
                            i.parent = node;
                            open.add(i);
                        }else if(i.tile.collides){
                            node.g += 50;
                            node.f = node.g+node.h;
                        }
                    }
                    else if(x!=0||y!=0){
                        i.g = node.g+10;
                        i.h = calculateH(i.tile.getX(), i.tile.getY(), goal.tile.getX(), goal.tile.getY())*10;
                        i.f = i.g+i.h;
                        if(!closed.contains(i)&&!open.contains(i)&&i.tile!=null&&!i.tile.collides){
                            i.parent = node;
                            open.add(i);
                        }else if(i.tile.collides){
                            node.g += 50;
                            node.f = node.g+node.h;
                        }
                    }
                }

            }
        }
        return null;
    }

    /*
    Node start = new Node(null, world.getTile((int)x/Tile.TILE_SIZE, (int)y/Tile.TILE_SIZE), 0, calculateH((int)x/Tile.TILE_SIZE, (int)y/Tile.TILE_SIZE, (int)Game.player.getX(), (int)Game.player.getY()));
        Node goal = new Node(null, new Tile((int)Game.player.getX()/Tile.TILE_SIZE, (int)Game.player.getY()/Tile.TILE_SIZE), start.h, 0);
        ArrayList<Node> closed = new ArrayList<Node>();
        ArrayList<Node> open = new ArrayList<Node>();
        open.add(start);
        Node current = open.get(0);
        while(!open.isEmpty()){
            current = open.get(0);
            for(Node q : open){
                if(q.f<current.f)current = q;
            }
            open.remove(current);
            for(int y = -1; y<2; y++){
                for(int x = -1; x<2; x++){
                    int xi = current.tile.getX()+x;
                    int yi = current.tile.getY()+y;
                    Tile tile =  world.getTile(xi, yi);
                    Node n = null;

                    if(x==0||y==0){
                        n = new Node(current, tile, current.g+10, calculateH(xi, yi, goal.tile.getX(), goal.tile.getY()));
                    }
                    else if(x==0&&y==0);
                    else if(x!=0&&y!=0){
                        n = new Node(current, tile, current.g+14, calculateH(xi, yi, goal.tile.getX(), goal.tile.getY()));
                    }
                    if(n.equals(goal))return n;
                    if(tile!=null&&!tile.collides&&n!=null&&!closed.contains(n)&&!open.contains(n)){
                        open.add(n);
                    }else if(n!=null&&open.contains(n)){
                        for(int i = 0; i<open.size(); i++){
                            if(open.get(i).equals(n)&&open.get(i).g<n.g){
                                closed.add(n);
                            }
                        }
                    }else if(n!=null&&closed.contains(n)){
                        for(int i = 0; i<closed.size(); i++){
                            if(closed.get(i).equals(n)&&closed.get(i).f>n.f){
                                open.add(n);
                            }
                        }
                    }
                }
            }
            closed.add(current);
        }
        return null;
     */

    public static int calculateH(int xi, int yi, int xf, int yf){
        return Math.abs(xi-xf)+Math.abs(yi-yf);
    }

}
