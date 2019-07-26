import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.util.TreeMap;
import java.util.Set;

public class ImageManager
{
    TreeMap<String, BufferedImage> images = null;

    public ImageManager()
    {
        images =new TreeMap<String,BufferedImage>();
    }

    /* Pre: Receives a name of file
     * Post: Loads all this images in the file to map of images using the provided load type
     */
    public boolean loadImages(String fileName)
    {
        Scanner sc;
        try {
            sc = new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        while(sc.hasNextLine()){
            String line = sc.nextLine();
            String type = "";
            for(int i = 0; i<line.length(); i++){
                if (line.charAt(i) == ',') {
                    type = line.substring(0, i);
                    line = line.substring(i+1);
                    i=line.length();
                }
            }
            if(type.equals("single")){
                String key = "";
                String file = "";
                for(int i = 0; i<line.length(); i++){
                    if(line.charAt(i)==','){
                        key = line.substring(0, i);
                        file = line.substring(i+1);
                        loadImage(key, file);

                    }
                }
            }else if(type.equals("SLNbL")){
                String numCol_S = "";
                int numCol = 0;
                String key = "";

                for(int i = 0; i<line.length(); i++){
                    if(line.charAt(i)==','){
                        numCol_S = line.substring(0, i);
                        numCol = Integer.parseInt(numCol_S);
                        line = line.substring(i+1);
                        i = line.length();
                    }
                }

                for(int i = 0; i<line.length(); i++){
                    if(line.charAt(i)==','){
                        key = line.substring(0, i);
                        line = line.substring(i+1);
                        i=line.length();
                    }
                }

                BufferedImage img = null;
                try {
                    img = ImageIO.read(new File(line));
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                for(int j = 0; j<numCol; j++){
                    String key1 = key + j;
                    BufferedImage frame = img.getSubimage((img.getWidth()/numCol)*j, 0, img.getWidth()/numCol, img.getHeight());
                    images.put(key1, frame);
                }
            }else if(type.equals("GLNbL")){
                String numCol_s = "";
                int numCol = 0;
                String numRow_s = "";
                int numRow = 0;
                String key = "";
                for(int i = 0; i<line.length(); i++){
                    if(line.charAt(i)==','){
                        numCol_s=line.substring(0, i);
                        numCol = Integer.parseInt(numCol_s);
                        line = line.substring(i+1);
                        i=line.length();
                    }
                }
                for(int i = 0; i<line.length(); i++){
                    if(line.charAt(i)==','){
                        numRow_s=line.substring(0, i);
                        numRow = Integer.parseInt(numRow_s);
                        line = line.substring(i+1);
                        i=line.length();
                    }
                }
                for(int i = 0; i<line.length(); i++){
                    if(line.charAt(i)==','){
                        key = line.substring(0, i);
                        line = line.substring(i+1);
                        i=line.length();
                    }
                }
                String file = line;
                try {
                    BufferedImage img = ImageIO.read(new File(file));
                    int width = img.getWidth()/numCol;
                    int height = img.getHeight()/numRow;
                    for(int r = 0; r<numRow; r++){
                        for(int c = 0; c<numCol; c++){
                            String cKey = key+"r"+r+"c"+c;
                            BufferedImage frame = img.getSubimage(c*width, r*height, width, height);
                            images.put(cKey, frame);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }else if(type.equals("SLSN")){
                String numCol_s = "";
                int numCol = 0;
                ArrayList<String> keys = new ArrayList<>();
                for(int i = 0; i<line.length(); i++){
                    if(line.charAt(i)==',') {
                        numCol_s=line.substring(0, i);
                        numCol = Integer.parseInt(numCol_s);
                        line = line.substring(i + 1);
                        i = line.length();
                    }
                }

                for(int i = 0; i<numCol; i++){
                    for(int j = 0; j<line.length(); j++){
                        if(line.charAt(j)==','){
                            keys.add(line.substring(0, j));
                            line = line.substring(j+1);
                            j=line.length();
                        }
                    }
                }
                String file = line;
                BufferedImage img;
                try {
                    img = ImageIO.read(new File(file));
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                for(int i = 0; i<numCol; i++){
                    int width = img.getWidth()/numCol;
                    images.put(keys.get(i), img.getSubimage(i*width, 0, width, img.getHeight()));
                }
            }else if(type.equals("GLSN")){
                String numCol_s="";
                String numRow_r="";
                int numCol = 0;
                int numRow = 0;
                ArrayList<String> keys = new ArrayList<>();
                System.out.println(line);
                for(int i = 0; i<line.length(); i++){
                    if(line.charAt(i)==','){
                        numCol_s = line.substring(0, i);
                        numCol = Integer.parseInt(numCol_s);
                        System.out.println(numCol);
                        line = line.substring(i + 1);
                        i = line.length();
                    }
                }
                for(int i = 0; i<line.length(); i++){
                    if(line.charAt(i)==','){
                        numRow_r = line.substring(0, i);
                        numRow = Integer.parseInt(numRow_r);
                        System.out.println(numRow);
                        line = line.substring(i + 1);
                        i = line.length();
                    }
                }
                for(int i = 0; i<numCol*numRow; i++){
                    for(int j = 0; j<line.length();j++){
                        if(line.charAt(j)==','){
                            keys.add(line.substring(0, j));
                            line = line.substring(j+1);
                            j=line.length();
                        }
                    }
                }
                System.out.println(keys);
                System.out.println(line);
                try {
                    BufferedImage img = ImageIO.read(new File(line));
                    int width = img.getWidth()/numCol;
                    int height = img.getHeight()/numRow;
                    for(int y = 0; y<numRow; y++){
                        for(int x = 0; x<numCol; x++){
                            String k = keys.remove(0);
                            images.put(k, img.getSubimage(x*width, y*height, width, height));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }

        return true;
    }

    /* Pre: Receives a key
     * Post: returns the image that corrisponds to the given key, null if the key is not found
     */
    public BufferedImage getImage(String key)
    {
        return images.get(key);
    }

    /* Pre: Receives a key and a file name
     * Post: loads the image in the given file to the map with the provided key
     * Note: null is returned if the file can not be opened
     */
    public BufferedImage loadImage(String key, String file)
    {
        BufferedImage img=null;
        try {
            img = ImageIO.read(new File(file));
        } catch (IOException e) {
            System.out.println("Error loading image: "+file);
            return null;
        }

        images.put(key, img);
        return img;
    }

    /* Pre: Receives a key a
     * Post: removes the key and its image from the map, the removed image is returned.
     * null is returned if the map does not cantain the given key
     */
    public BufferedImage removeImage(String key)
    {
        if(!images.containsKey(key))return null;
        return images.remove(key);
    }

    /* Pre:
     * Post: empties the map
     */
    public void clear()
    {
        images.clear();
    }

    /* Pre:
     * Post: returns a set of all the keys in the map
     */
    public Set<String> getKeys()
    {
        return images.keySet();
    }
}