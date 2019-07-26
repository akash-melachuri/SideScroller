public class RangedEnemy extends Enemy {
    public static int attack = 20;
    public RangedEnemy(float x, float y) {
        super(x, y);
        imgKey = "rangedEnemy";
        range = 300;
        melee = false;
        firerate=120;
        spawnsBabies = false;
        attack = maxAttack;
    }

    public void update(World world){
        if(calculateH((int)x, (int)y, (int)Game.player.getX(), (int)Game.player.getY())<=range&&hasLineOfSight((int)Game.player.getX()+Game.player.getWidth()/2, (int)Game.player.getY()+Game.player.getHeight()/2, world)){
            inRange = true;
        }else inRange = false;
        if(inRange&&animCount%firerate==0&&hasLineOfSight((int)Game.player.getX()+Game.player.getWidth()/2, (int)Game.player.getY()+Game.player.getHeight()/2, world)){
            world.projectiles.add(new EnemyProjectile(x+width/2, y+height/2, Math.atan2((Game.player.getY()+Game.player.getHeight()/2)-(y+height/2), (Game.player.getX()+Game.player.getWidth()/2)-(x+width/2))));
        }
        super.update(world);
    }

}
