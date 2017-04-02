import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.io.File;
import java.util.ArrayList;
import java.awt.Color;

class Model
{
   private Controller controller;
   private ArrayList<Vector> vectors;
  
   public Model(Controller c)
   {
      this.controller = c;
      this.vectors = new ArrayList<Vector>();
   }
   
   public ArrayList<Vector> getVectors() { return this.vectors; }
   
   public void addVector(int x1, int y1, int color)
   {
      //find x2, y2 (distance from center of color hue)
      System.out.println("Color selected is " + color);
      //X2 is X difference from center of color wheel to the selected color from the frame
      int x2 = VFI_Map.getDistances(color)[0];
      //Y2 is Y difference from center of color wheel to the selected color from the frame
      int y2 = VFI_Map.getDistances(color)[1];
      //Set the end point of the vector to relative distance the the color is on the color wheel
      x2 = x1 - x2;
      y2 = y1 - y2;

      vectors.add(new Vector(x1, y1, x2, y2, 5, 2));
   }
}