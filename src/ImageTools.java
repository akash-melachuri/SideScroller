import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class ImageTools {

    public static int HORIZONTAL_FLIP = 1, VERTICAL_FLIP = 2, DOUBLE_FLIP = 3;

    /**
     * Loads an image.
     *
     * @param fileName The name of a file with an image
     * @return Returns the loaded image. null is returned if the image cannot be loaded.
     */
    public static BufferedImage load(String fileName) {
        BufferedImage img=null;
        try {
            img = ImageIO.read(new File(fileName));
        } catch (IOException e) {
            System.out.println("Error loading image: "+fileName);
            return null;
        }


        return img;
    }

    /**
     * Copies and returns an image.
     *
     * @param img Receives a buffered image
     * @return Returns a copy of the received image. null is returned if the received image is null.
     */
    public static BufferedImage copy(BufferedImage img) {
        BufferedImage copy = new BufferedImage(img.getWidth(), img.getHeight(), img.getColorModel().getTransparency());
        Graphics2D g = (Graphics2D)copy.getGraphics();
        g.drawImage(img, 0, 0, null);
        return copy;
    }

    /**
     * Returns a new image with transparency enabled.
     *
     * @param img Receives a buffered image
     * @return returns a copy of the received image with a color mode that allows transparency.
     * null is returned if the received image is null.
     */
    public static BufferedImage copyWithTransparency(BufferedImage img) {
        if(img==null)return null;
        BufferedImage copy = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D)copy.getGraphics();
        g.drawImage(img, 0, 0, null);
        return copy;
    }

    /**
     * Checks if the provided image has transparency.
     *
     * @param img Receives a buffered image
     * @return returns true if the image has a transparent mode and false otherwise.
     */
    public static boolean hasTransparency(BufferedImage img) {
        if(img.getColorModel().getTransparency()==BufferedImage.TYPE_INT_ARGB)return true;
        return false;
    }

    /**
     * Scales an image.
     *
     * @param img Receives a buffered image and two positive double scale values (percentages)
     * @param horizontalScale Value to scale horizontal by.
     * @param verticalScale Value to scale vertical by.
     * @return creates and returns a scaled copy of the received image,
     * null is returned if the received image is null or if non-positive scales are provided
     */
    public static BufferedImage scale(BufferedImage img, double horizontalScale,
                                      double verticalScale) {
        if(img==null||horizontalScale<0||verticalScale<0)return null;
        BufferedImage copy = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D)copy.getGraphics();
        g.drawImage(img, 0, 0,(int)(horizontalScale*img.getWidth()), (int)(verticalScale*img.getHeight()), null);
        return copy;
    }

    /**
     * Scales an image.
     *
     * @param img Receives a buffered image
     * @param newWidth New width to scale to.
     * @param newHeight New height to scale to.
     * @return creates and returns a scaled copy of the received image,
     * null is returned if the received image is null or if non-positive dimensions are provided
     */
    public static BufferedImage scale(BufferedImage img, int newWidth,
                                      int newHeight) {
        if(img==null||newWidth<0||newHeight<0)return null;
        BufferedImage copy = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D)copy.getGraphics();
        g.drawImage(img, 0, 0,newWidth, newHeight, null);
        return copy;
    }

    /**
     * Rotates an image.
     *
     * @param img Receives a buffered image
     * @param angle The angle to rotate to.
     * @return The rotated image. null is returned if the received image is null.
     */
    public static BufferedImage rotate(BufferedImage img, double angle) {
        angle=angle%360;
        if(img==null)return null;
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.setToTranslation(0, 0);
        affineTransform.rotate(Math.toRadians(angle),img.getWidth()/2, img.getHeight()/2);
        int transparency = img.getColorModel().getTransparency();
        BufferedImage rotated = new BufferedImage(img.getWidth(),img.getHeight(), transparency);
        Graphics2D g = (Graphics2D)rotated.getGraphics();
        g.drawImage(img, affineTransform, null);
        return rotated;
    }

    /**
     * Flips an image.
     *
     * @param img Receives a buffered image
     * @param type Type of flip (int)
     * @return Creates and returns a flipped copy of the received image.
     * null is returned if the received image is null or if an invalid flipping value is provided
     */
    public static BufferedImage flip(BufferedImage img, int type) {
        if(img==null)return null;
        if(type== ImageTools.HORIZONTAL_FLIP){
            BufferedImage copy = new BufferedImage(img.getWidth(), img.getHeight(), img.getColorModel().getTransparency());
            Graphics2D g = (Graphics2D)copy.getGraphics();
            g.drawImage(img, img.getWidth(), 0, 0, img.getHeight(), 0, 0, img.getWidth(), img.getHeight(), null);
            return copy;
        }else if(type== ImageTools.VERTICAL_FLIP){
            BufferedImage copy = new BufferedImage(img.getWidth(), img.getHeight(), img.getColorModel().getTransparency());
            Graphics2D g = (Graphics2D)copy.getGraphics();
            g.drawImage(img, 0, img.getHeight(), img.getWidth(), 0, 0, 0, img.getWidth(), img.getHeight(), null);
            return copy;
        }else if(type== ImageTools.DOUBLE_FLIP){
            BufferedImage copy = new BufferedImage(img.getWidth(), img.getHeight(), img.getColorModel().getTransparency());
            Graphics2D g = (Graphics2D)copy.getGraphics();
            g.drawImage(img, img.getWidth(), img.getHeight(), 0, 0, 0, 0, img.getWidth(), img.getHeight(), null);
            return copy;
        }
        return null;
    }

    /**
     * Blurs an image.
     *
     * @param img Receives a buffered image
     * @return creates and returns a blurred copy of the received image,
     * the blurring is created by blending each cell with its 8 neighbors.
     * Null is returned if the received image is null.
     */
    public static BufferedImage blur(BufferedImage img) {
        if(img==null)return null;
        BufferedImage copy = copyWithTransparency(img);
        for(int y = 0; y<img.getHeight(); y++){
            for(int x=0; x<img.getWidth(); x++){
                int num = 0;
                int col = 0;
                int ox = x-1;
                int oy = y-1;
                int r = 0;
                int g = 0;
                int b = 0;
                int a = 0;
                for(int j = 0; j<3; j++){
                    for(int i = 0; i<3; i++){

                        if(ox+i>=0&&ox+i<img.getWidth()&&oy+j>=0&&oy+j<img.getHeight()&&(new Color(img.getRGB(ox+i, oy+j),true)).getAlpha()!=0){
                            Color currentColor = new Color(img.getRGB(x, y), true);
                            r =new Color(img.getRGB(ox+i, oy+j), true).getRed()+r;
                            g = new Color(img.getRGB(ox+i, oy+j), true).getGreen()+g;
                            b = new Color(img.getRGB(ox+i, oy+j), true).getBlue()+b;
                            a = new Color(img.getRGB(ox + i, oy+j), true).getAlpha()+a;
                            num++;
                            a=currentColor.getAlpha();
                        }
                    }
                }

                if(num!=0){
                    r/=num;
                    g/=num;
                    b/=num;
                   // a/=num;
                }else{
                    a = 0;
                }
                Color color = new Color(r, g, b, a);
                copy.setRGB(x, y, color.getRGB());
            }
        }
        return copy;
    }

    /**
     * Inverts an image's colors.
     *
     * @param img Receives a buffered image
     * @return Image with inverted colors. null is returned if the received image is null.
     */
    public static BufferedImage invertColor(BufferedImage img)
    {
        BufferedImage invert = copyWithTransparency(img);
        for(int r = 0; r<img.getHeight(); r++){
            for(int c = 0; c<img.getWidth(); c++){
                int colorNum = img.getRGB(c, r);
                Color color = new Color(colorNum, true);
                int red = 255-color.getRed();
                int blue = 255-color.getBlue();
                int green = 255-color.getGreen();
                int alpha = color.getAlpha();
                Color newColor = new Color(red, green, blue, alpha);
                invert.setRGB(c, r, newColor.getRGB());
            }
        }
        return invert;
    }

    /**
     * Removes a certain percentage of an image's pixels.
     *
     * @param img Receives a buffered image
     * @param percentToRemove Percent to remove of the image.
     * @return creates and returns a copy of the received image with the given
     * percentage in decimal form of the images remaining non-fully transparent
     * pixels changed to be completely transparent. null is returned if the
     * received image is null or if non-positive percentage is provided.
     */
    public static BufferedImage removePixels(BufferedImage img, double percentToRemove) {
        if(img==null||percentToRemove<0)return null;
        int num = 0;
        for(int y = 0; y<img.getHeight(); y++){
            for(int x = 0; x<img.getWidth(); x++){
                if(new Color(img.getRGB(x, y), true).getAlpha()!=0)num++;
            }
        }
        int pixelsToRemove = (int)(percentToRemove*num);
        BufferedImage copy = copyWithTransparency(img);

        return removePixels(copy, pixelsToRemove);
    }

    /**
     * Removes a certain amount of pixels from an image.
     *
     * @param img Receives a buffered image
     * @param numToRemove number of pixels to remove
     * @return creates and returns a copy of the received image with the given
     * number of the images remaining pixels removed.
     * Non-fully transparent pixels are changed to be completely transparent.
     * null is returned if the received image is null or if non-positive number
     * is provided. Note: is there are not enough pixels in the image it will
     * remove as many as it can.
     */
    public static BufferedImage removePixels(BufferedImage img, int numToRemove) {
        if(img==null||numToRemove<0)return null;
        BufferedImage copy =copyWithTransparency(img);
        int x = new Random().nextInt(img.getWidth());
        int y = new Random().nextInt(img.getHeight());
        ArrayList<Point> points = new ArrayList<>();
        do{
            Point point = new Point(x, y);
            boolean already = false;
            for(Point p : points){
                if(p.equals(point))already=true;
            }
            Color color = new Color(copy.getRGB(x, y), true);
            if(color.getAlpha()!=0&&!already){
                copy.setRGB(x, y, new Color(color.getRed(), color.getGreen(), color.getBlue(), 0).getRGB());
                numToRemove--;
                points.add(new Point(x, y));
            }
            x = new Random().nextInt(img.getWidth());
            y = new Random().nextInt(img.getHeight());
        }while(numToRemove>0);
        return copy;
    }

    /**
     * Fades an image.
     *
     * @param img Receives a buffered image
     * @param fade Double percentage to fade
     * @return Creates and returns a copy of the received image that has been
     * faded the given percentage. Fading is done by multiply the alpha value by (1-fade)
     * Null is returned if the received image is null or if non-positive fade value is provided
     * Note: any fade greater than 1 will be reduced to 1
     */
    public static BufferedImage fade(BufferedImage img, double fade) {
        if(img==null||fade<0)return null;
        if(fade>1)fade = 1;
        BufferedImage copy = copyWithTransparency(img);
        for(int y = 0; y<copy.getHeight(); y++){
            for(int x = 0; x<copy.getWidth(); x++){
                Color o = new Color(img.getRGB(x, y), true);
                int alpha = (int)(o.getAlpha()*(1-fade));
                Color col = new Color(o.getRed(),o.getGreen(),o.getBlue(), alpha);
                copy.setRGB(x, y, col.getRGB());
            }
        }
        return copy;
    }

    /**
     * Lightens an image.
     *
     * @param img Receives a buffered image
     * @param lightenFactor double percentage to lighten
     * @return creates and returns a copy of the received image that has been
     * lightened by the given percentage + 1.
     * Null is returned if the received image is null or if non-positive lighten.
     * Factor value is provided.
     * Note: any colors that end up being larger than 255 will be changed to 255.
     */
    public static BufferedImage lighten(BufferedImage img, double lightenFactor) {
        if(img==null||lightenFactor<0)return null;
        BufferedImage copy = copyWithTransparency(img);
        lightenFactor+=1.0f;
        for(int y = 0; y<img.getHeight(); y++){
            for(int x = 0; x<img.getWidth(); x++){
                int colorNum = img.getRGB(x, y);
                Color color = new Color(colorNum, true);
                int red = (int)(color.getRed()*lightenFactor);
                if(red>255)red=255;
                int blue = (int)(color.getBlue()*lightenFactor);
                if(blue>255)blue=255;
                int green = (int)(color.getGreen()*lightenFactor);
                if(green>255)green =255;
                int alpha = color.getAlpha();
                Color newColor = new Color(red, green, blue, alpha);
                copy.setRGB(x, y, newColor.getRGB());
            }
        }
        return copy;
    }

    /**
     * Darkens an image.
     *
     * @param img Receives a buffered image
     * @param darkenFactor double percentage to darken
     * @return creates and returns a copy of the received image that has been
     * darkened by 1 minus the given percentage.
     * null is returned if the received image is null or if non-positive.
     * Note: any colors that end up being larger than 255 will be
     * changed to 255.
     */
    public static BufferedImage darken(BufferedImage img, double darkenFactor) {
        if(img==null||darkenFactor<0)return null;
        BufferedImage copy = copyWithTransparency(img);
        darkenFactor=1.0f-darkenFactor;
        for(int y = 0; y<img.getHeight(); y++){
            for(int x = 0; x<img.getWidth(); x++){
                int colorNum = img.getRGB(x, y);
                Color color = new Color(colorNum, true);
                int red = (int)(color.getRed()*darkenFactor);
                if(red>255)red=255;
                int blue = (int)(color.getBlue()*darkenFactor);
                if(blue>255)blue=255;
                int green = (int)(color.getGreen()*darkenFactor);
                if(green>255)green =255;
                int alpha = color.getAlpha();
                Color newColor = new Color(red, green, blue, alpha);
                copy.setRGB(x, y, newColor.getRGB());
            }
        }
        return copy;
    }
}