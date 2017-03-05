import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class Main
{
   private static int mHeight;
   private static int mWidth;
   private static BufferedImage image;
   private static Matrix colorKey;

   public static void rgbToInt(int color)
   {
      int[] rgb[] = new int[3];
      rgb[0] = (color)&0xFF;
      rgb[1] = (color>>8)&0xFF;
      rgb[2] = (color>>16)&0xFF;
      //return rgb;
      System.out.println("Value of integer is " + color);
      for(int i = 0; i < rgb.length; i++)
         System.out.println("Value at " + i + " is " + rgb[i]);
      throw new IllegalArgumentException("Stop");
   }

   public static void findColor(int x, int y)
   {
      for(int i = 0; i < mHeight; i++)
      {
         System.out.println();
         System.out.println("Row " + i);
         for(int j = 0; j < mWidth; j++)
            rgbToInt(image.getRGB(j, i));
            //System.out.print(image.getRGB(j, i) + " ");
      }
      System.out.println();
   }

   public static void main(String args[]) throws IOException
   {
      File file = new File("colorKey.png");
      image = ImageIO.read(file);
      mHeight = image.getHeight();
      mWidth = image.getWidth();
      findColor(1, 1);
      colorKey = new Matrix(mHeight, mWidth*3);
   }
}
