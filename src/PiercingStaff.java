public class PiercingStaff extends Item {
    protected int duration = 30;
    public PiercingStaff(float x, float y) {
        super(x, y);
        imgKey = "redStaff";
        attack = 40;
        firerate = 40;
    }
    public void attack(){
        Game.player.projectiles.add(new PiercingProjectile(Game.player.getX()+Game.player.width/2, Game.player.getY()+Game.player.height/2, Math.atan2(Game.toWorldY(Game.mousePos.y) - (Game.player.y+Game.player.height/2),Game.toWorldX(Game.mousePos.x)-(Game.player.x+Game.player.width/2))));
    }
}
