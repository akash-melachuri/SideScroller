public class EnemyProjectile extends Projectile {
    public EnemyProjectile(float x, float y, double angle) {
        super(x, y, angle);
        destructable = false;
        imgKey = "enemyProjectile";
        speed = 1.5f;
        rect.width = 10;
        rect.height = 10;

    }

    public void update(World world){
        x+=Math.cos(angle)*speed;
        y+=Math.sin(angle)*speed;
        if(isColliding(world))dead = true;
        if(rect.intersects(Game.player.rect)){
            Game.player.takeHits(RangedEnemy.attack);
            dead = true;
        }
        updateRect();
        animCount++;
        if(animCount>=1000)animCount=0;
    }
}
