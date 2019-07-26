import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Game extends JFrame implements Runnable, KeyListener, MouseListener, MouseMotionListener {

    boolean running = false;
    public static BufferedImage buffer;
    public static ImageManager imgManager;
    public static Player player;
    public static Camera camera;
    public static World world;
    Canvas canvas = new Canvas();

    public static boolean[] keys = new boolean[50];
    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    public static final int A = 4;
    public static final int D = 5;
    public static final int S = 6;
    public static final int W = 7;
    public static final int ESC = 8;
    public static final int SPACE = 9;
    public static final int E = 10;
    public static final int U = 11;
    public static boolean u_released = false;

    public static final int WIDTH = 1600, HEIGHT = 900;
    public static int PLAYER_WIDTH = 20, PLAYER_HEIGHT = 20;

    public Game(){
        super("Side Scroller");
        setSize(WIDTH, HEIGHT);
        imgManager = new ImageManager();
        imgManager.loadImages("images.txt");
        buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        camera = new Camera();
        newLevel();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas.setIgnoreRepaint(true);
        canvas.setSize(WIDTH, HEIGHT);
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        canvas.addKeyListener(this);
        setLocationRelativeTo(null);
        setUndecorated(true);
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        add(canvas);
        setVisible(true);

        new Thread(this).start();
    }

    public void newLevel(){
        do{
            if(player!=null)player.reset(getWidth()/2-PLAYER_WIDTH/2, getHeight()/2-PLAYER_HEIGHT/2);
            else player = new Player(getWidth()/2-PLAYER_WIDTH/2, getHeight()/2-PLAYER_HEIGHT/2);
            camera.update(player);
            if(world!=null){
                //world.levelUp();
            }
            world = new World();
            boolean[][] copy = new boolean[world.map.length][world.map[0].length];
            for(int r = 0; r<copy.length; r++){
                for(int c = 0; c<copy[0].length; c++){
                    copy[r][c] = world.map[r][c];
                }
            }
            DungeonGenerator.n = 0;
            DungeonGenerator.floodFill((int)player.getX()/Tile.TILE_SIZE, (int)player.getY()/Tile.TILE_SIZE, copy);
            System.out.println(DungeonGenerator.n);
        }while(DungeonGenerator.n<800||player.isColliding(world));

    }

    double target = 1000.0/144;
    @Override
    public void run() {
        double repaintTarget = 1000.0/144;
        int repaintCount = 0;
        running = true;
        long startTime = System.nanoTime();
        long repaintStartTime = System.nanoTime();
        long updatesDone = 0;
        canvas.createBufferStrategy(2);
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        BufferedImage bi = gc.createCompatibleImage(WIDTH, HEIGHT);
        while(running){
            long updatesNeeded = (long) (((System.nanoTime()-startTime)/1000000)/target);
            long repaintsNeeded = (long) (((System.nanoTime()-repaintStartTime)/1000000)/repaintTarget);
            boolean shouldRepaint = false;
            for(;updatesDone<=updatesNeeded; updatesDone++){
                update();
                shouldRepaint = true;
            }
            if(shouldRepaint && repaintCount<=repaintsNeeded){
                render(bi, bufferStrategy);
                repaintCount++;

                if(repaintCount>10){
                    repaintStartTime = System.nanoTime();
                    repaintCount = 0;
                }
            }
        }
        System.exit(0);
    }
    public void update(){
        if(keys[ESC])System.exit(0);
        if(world.enemies.isEmpty()){
            newLevel();
        }
        if(!gameOver){
            world.update();
            player.update(world);
            camera.update(player);
        }else{
        }

    }
    public static boolean gameOver = false;

    BufferedImage ss;
    public void render(BufferedImage bi, BufferStrategy bs) {
        Graphics2D g2d = bi.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        world.render(g2d, camera);
        player.render(g2d, camera);
        if(gameOver){
            g2d.drawImage(imgManager.getImage("gameOver"), WIDTH/2-128, HEIGHT/2-125, 256, 256, null);
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Monospaced", Font.BOLD, 24));
            g2d.drawString("Spacebar to start over!", WIDTH/2-155,HEIGHT/2+200);
        }
        Graphics g = bs.getDrawGraphics();
        g.drawImage(bi, 0, 0, null);
        if(keys[U]){
            ss = ImageTools.copy(bi);
        }
        if(!bs.contentsLost())
            bs.show();
        if(g!=null)g.dispose();
        if(g2d!=null)g2d.dispose();
        u_released = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_UP)keys[UP]=true;
        if(e.getKeyCode()==KeyEvent.VK_DOWN)keys[DOWN]=true;
        if(e.getKeyCode()==KeyEvent.VK_LEFT)keys[LEFT]=true;
        if(e.getKeyCode()==KeyEvent.VK_RIGHT)keys[RIGHT]=true;
        if(e.getKeyCode()==KeyEvent.VK_A)keys[A]=true;
        if(e.getKeyCode()==KeyEvent.VK_S)keys[S]=true;
        if(e.getKeyCode()==KeyEvent.VK_D)keys[D]=true;
        if(e.getKeyCode()==KeyEvent.VK_W)keys[W]=true;
        if(e.getKeyCode()==KeyEvent.VK_ESCAPE)keys[ESC]=true;
        if(e.getKeyCode()==KeyEvent.VK_SPACE){
            keys[SPACE]=true;
        }
        if(e.getKeyCode()==KeyEvent.VK_E)keys[E]=true;
        if(e.getKeyCode()==KeyEvent.VK_U)keys[U]=true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) keys[UP] = false;
        if (e.getKeyCode() == KeyEvent.VK_DOWN) keys[DOWN] = false;
        if (e.getKeyCode() == KeyEvent.VK_LEFT) keys[LEFT] = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) keys[RIGHT] = false;
        if (e.getKeyCode() == KeyEvent.VK_A) keys[A] = false;
        if (e.getKeyCode() == KeyEvent.VK_S) keys[S] = false;
        if (e.getKeyCode() == KeyEvent.VK_D) keys[D] = false;
        if (e.getKeyCode() == KeyEvent.VK_W) keys[W] = false;
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) keys[ESC] = false;
        if (e.getKeyCode()==KeyEvent.VK_SPACE){
            keys[SPACE]=false;
            if(gameOver){
                player=null;
                newLevel();
                gameOver=false;
            }
        }
        if(e.getKeyCode()==KeyEvent.VK_E){
            keys[E]=false;
            Tile tile = world.getObject((mousePos.x+camera.getX())/Tile.TILE_SIZE, (mousePos.y+camera.getY())/Tile.TILE_SIZE);
            if(tile!=null&&tile.interactable&&player.hasLineOfSight(tile.getPixelX()+(int)tile.getRect().getWidth()/2, tile.getPixelY()+(int)tile.getRect().getHeight()/2, world)&&Enemy.calculateH(tile.getPixelX(), tile.getPixelY(), (int)player.getX(),(int) player.getY())<=75)tile.interact(world);
        }
        if(e.getKeyCode()==KeyEvent.VK_U){
            keys[U]=false;
            u_released = true;
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");//dd/MM/yyyy
            Date now = new Date();
            String strDate = sdfDate.format(now);
            File outputFile = new File(strDate+".png");
            try {
                if(outputFile.createNewFile())System.out.println("Image created");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                ImageIO.write(ss, "png", outputFile);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mousePos = new Point(e.getX(), e.getY());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton()==MouseEvent.BUTTON1)mouseLeft = true;
        if(e.getButton()==MouseEvent.BUTTON3)mouseRight = true;
        mousePos = new Point(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mousePos = new Point(e.getX(), e.getY());
        if(e.getButton()==MouseEvent.BUTTON1)mouseLeft = false;
        if(e.getButton()==MouseEvent.BUTTON3) {
            mouseRight = false;
//            world.setTile((mousePos.x+camera.getX())/Tile.TILE_SIZE, (mousePos.y+camera.getY())/Tile.TILE_SIZE, new BrickTile((mousePos.x+camera.getX())/Tile.TILE_SIZE, (mousePos.y+camera.getY())/Tile.TILE_SIZE));
//            world.setTile((mousePos.x+camera.getX())/Tile.TILE_SIZE, (mousePos.y+camera.getY())/Tile.TILE_SIZE+1, new ShadowTile((mousePos.x+camera.getX())/Tile.TILE_SIZE, (mousePos.y+camera.getY())/Tile.TILE_SIZE+1));

        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mousePos = new Point(e.getX(), e.getY());
    }

    public static boolean mouseLeft = false;
    public static boolean mouseRight = false;
    public static Point mousePos = new Point();
    @Override
    public void mouseMoved(MouseEvent e) {
        mousePos = new Point(e.getX(), e.getY());
    }

    public static float toScreenX(float x){
        return x-camera.getX();
    }

    public static float toScreenY(float y){
        return y - camera.getY();
    }

    public static float toWorldX(float x){
        return x+camera.getX();
    }

    public static float toWorldY(float y){
        return y +camera.getY();
    }

    public static boolean onScreen(float x, float y, int width, int height){
        x = toScreenX(x);
        y = toScreenY(y);
        return (x>=0-width&&x<=WIDTH&&y>=0-height&&y<=HEIGHT);
    }

}
