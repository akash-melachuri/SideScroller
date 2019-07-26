import java.util.Random;

public class DungeonGenerator {

    public static boolean[][] cellularAutomata(int width, int height, float chance, int deathLimit, int birthLimit, int iterations){
        boolean[][] array = new boolean[height][width];
        for(int r = 0; r<height; r++){
            for(int c = 0; c<width; c++){
                if(chance<Math.random())array[r][c] = true;
            }
        }

        for(int i = 0; i<iterations; i++) array = simulateLife(array, deathLimit, birthLimit);
        return array;
    }

    public static boolean[][] simulateLife(boolean[][] old, int deathLimit, int birthLimit){
        boolean[][] newArray = new boolean[old[0].length][old.length];
        for(int y = 0; y<old.length; y++){
            for(int x = 0; x<old[0].length; x++){
                int neighbors = aliveNeighbors(x, y, old);
                if(old[y][x]){
                    if(neighbors<deathLimit){
                        newArray[y][x] = false;
                    }else{
                        newArray[y][x] = true;
                    }
                }else{
                    if(neighbors>birthLimit){
                        newArray[y][x] = true;
                    }else{
                        newArray[y][x] = false;
                    }
                }
            }
        }
        return newArray;
    }

    public static int aliveNeighbors(int x, int y, boolean[][] array){
        x-=1;
        y-=1;
        int count = 0;
        for(int i = 0; i<3; i++){
            for(int j = 0; j<3; j++){
                int locX = x+j;
                int locY = y+i;
                if(i==0&&j==0){

                }else if(locX<0||locX>=array[0].length||locY<0||locY>=array.length){
                    count++;
                }else if(array[locY][locX]){
                    count++;
                }
            }
        }
        return count;
    }
    //false is empty
    static int n = 0;
    public static void floodFill(int x, int y, boolean[][] array){
        if(x<0||x>=array[0].length||y<0||y>=array.length||array[y][x])return;
        else{
            array[y][x] = true;
            floodFill(x+1, y, array);
            floodFill(x-1, y, array);
            floodFill(x, y+1, array);
            floodFill(x, y-1, array);
        }
        n++;
        return;
    }

}
