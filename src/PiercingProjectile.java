public class PiercingProjectile extends Projectile {
    protected int blastRadius = 75;
    public PiercingProjectile(float x, float y, double angle) {
        super(x, y, angle);
        blockDamage = 3;
        imgKey = "piercingProjectile";
    }
    public void update(World world){
        super.update(world);
        if(dead){
            for(Enemy enemy: world.enemies){
                if(Enemy.calculateH((int)x, (int)y, (int)enemy.getX(), (int)enemy.getY())<blastRadius){
                    if(Game.player.equippedItem!=null)enemy.takeHits(Game.player.equippedItem.attack);
                }
            }
            if(Game.onScreen(x, y, width, height))world.entities.add(new Animation(x-blastRadius, y-blastRadius, "explosion", blastRadius*2, blastRadius*2, 12, 1, 10));
        }
    }

}
