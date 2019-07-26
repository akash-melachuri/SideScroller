import java.util.ArrayList;

public class Animation extends Entity {
    protected int cols;
    protected int rows;
    protected int currentCol = 0;
    protected int currentRow = 0;
    protected int timer;
    protected String baseKey;
    protected int frames;
    public Animation(float x, float y, String imgKey, int width, int height, int col, int row, int timer){
        super(x, y);
        this.baseKey = imgKey;
        this.imgKey = baseKey+"r0c0";
        this.width = width;
        this.height = height;
        this.cols = col;
        this.rows = row;
        this.timer = timer;
        frames = rows*cols;
    }
    public void update(World world){
        super.update(world);
        if(animCount%timer==0){
            if(currentCol>=cols-1){
                currentCol=0;
                currentRow++;
            }
            if(currentRow>=rows-1){
                currentRow=0;
            }
            currentCol++;
            imgKey = baseKey+"r"+currentRow+"c"+currentCol;
            frames--;
        }
        if(frames<=0)dead = true;
    }

}
