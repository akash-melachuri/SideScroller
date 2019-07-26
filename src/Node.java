
public class Node {
    Tile tile;
    Node parent;
    int f;
    int g;
    int h;

    public boolean equals(Object o){
        Node n = (Node)o;
        if(n.tile==null||tile==null)return false;
        return n.tile.getX()==tile.getX()&&n.tile.getY()==tile.getY();
    }
}