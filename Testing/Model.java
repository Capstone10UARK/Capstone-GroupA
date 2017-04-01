import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.io.File;
import java.util.ArrayList;

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
      //System.out.println("Color is " + color);
      //if(VFI_Map.getDistances(color) == null)
      //{
         //System.out.println("Not found");
      //}
      //find x2, y2
      //int x2 = VFI_Map.getDistances(color)[0];
      //int y2 = VFI_Map.getDistances(color)[1];
      vectors.add(new Vector(0, 0, 20, 20, 10, 5));
   }
}