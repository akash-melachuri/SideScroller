import java.awt.*;
import java.util.List;

public class BabyEnemy extends Enemy {
    public BabyEnemy(float x, float y) {
        super(x, y);
        width = 10;
        height = 10;
        rect = new Rectangle((int)x,(int)y, width, height);
        maxHits = 60;
        hits = maxHits;
        spawnsBabies = false;
    }



}
