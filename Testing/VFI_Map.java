import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.awt.Color;

class VFI_Map
{
   private static int mHeight; //height of color key image
   private static int mWidth; //width of color key image
   private static double Vmax = 236.5;
   private static double Dmax; //maximum distance from center of color hue to edge
   private static BufferedImage image; //The actual image of the color key
   private static HashMap<Integer,Double> RGB_VelMap = new HashMap<>(); //mapping from a color to a velocity 
   private static HashMap<Integer,int[]> RGB_DisMap = new HashMap<>(); //mapping from a color to a distance from the center of the key

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

   public static int searchMap(Map mp, int color) 
   {
       Iterator it = mp.entrySet().iterator();
       float minDifference = 10000000;
       int value = 0;
       
       float[] selectedHSL = new float[3];
       Color sel = new Color(color);
       Color.RGBtoHSB(sel.getRed(), sel.getGreen(), sel.getBlue(), selectedHSL);
       
       while (it.hasNext()) 
       {
           Map.Entry pair = (Map.Entry)it.next();
           
           Color map = new Color((int)pair.getKey());
           float[] hsl = new float[3];
           Color.RGBtoHSB(map.getRed(), map.getGreen(), map.getBlue(), hsl);
           float difference = Math.abs(selectedHSL[0] - hsl[0]);
           
           if(difference < minDifference && difference < 0.01)
           {
              minDifference = difference;
              value = (int)pair.getKey();
           }
       }
       return value;
   }
   
   public static int[] getDistances(int RGB){  
     int close = searchMap(RGB_DisMap, RGB);
     return RGB_DisMap.get(close);
   }
   
   public static double getVelocity(int RGB){
     int close = searchMap(RGB_VelMap, RGB);
     return RGB_VelMap.get(close);
   }

   public static void Init() throws IOException
   {
      //Preloaded key for color mapping
      File file = new File("images/colorKey.png");
      image = ImageIO.read(file);
      mHeight = image.getHeight();
      mWidth = image.getWidth();

      //Find the maximum distance to the center black pixel using pythagorean theorem
      Dmax = Math.sqrt(Math.pow(mHeight/2,2) + Math.pow(mWidth/2,2));

      //Find Velocity for given pixel based on RGB value and the distance to the origin
      findVelDis();
   }
}
