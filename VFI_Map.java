import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.HashMap;

class VFI_Map
{
   private static int mHeight;
   private static int mWidth;
   private static double Vmax = 236.5;
   private static double Dmax;
   private static BufferedImage image;
   private static HashMap<Integer,Double> RGB_VelMap = new HashMap<>();
   private static HashMap<Integer,int[]> RGB_DisMap = new HashMap<>();

   public static void findVelDis()
   {
      for(int i = 0; i < mHeight; i++)
      {
         for(int j = 0; j < mWidth; j++){
            int Dx = mWidth/2-j;
            int Dy = mHeight/2-i;
            int[] dis = {Dx, Dy};
            double d = Math.sqrt(Math.pow(Dx,2) + Math.pow(Dy,2));
            double v = (d*Vmax)/Dmax;
            if(RGB_VelMap.containsKey(image.getRGB(j,i))){
              //System.out.println("Duplicate of " + image.getRGB(j,i) + "with velocity: " + v);
            }
            else{
              if(image.getRGB(j,i) == -16777216)
                RGB_VelMap.put(image.getRGB(j,i), 0.0);
              else
                RGB_VelMap.put(image.getRGB(j,i), v);
                RGB_DisMap.put(image.getRGB(j,i), dis);
            }
          }
      }
   }

   public static double getVelocity(int RGB){
     return RGB_VelMap.get(RGB);
   }

   public static int[] getDistances(int RGB){
     return RGB_DisMap.get(RGB);
   }

   public static void main(String args[]) throws IOException
   {
      File file = new File("images/colorKey.png");
      image = ImageIO.read(file);
      mHeight = image.getHeight();
      mWidth = image.getWidth();

      //Find the maximum distance to the center black pixel using pythagorean theorem
      Dmax = Math.sqrt(Math.pow(mHeight/2,2) + Math.pow(mWidth/2,2));

      //Find Velocity for given pixel based on RGB value and the distance to the origin
      findVelDis();

      //Test for velocity and distance at given pixel
      System.out.println(RGB_VelMap.get(image.getRGB(0,0)) + ", " + RGB_DisMap.get(image.getRGB(0,0))[0]);
      System.out.println(getDistances(image.getRGB(0,0))[0] + ", " + getDistances(image.getRGB(0,0))[1]);
   }
}
