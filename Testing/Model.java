import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.io.File;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Robot;
import java.awt.Rectangle;
import java.awt.Toolkit;

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
      if(MyPanel.frame != null)
      {
        //find x2, y2 (distance from center of color hue)
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
   
   public void captureScreen(int x, int y) throws Exception
   {
      BufferedImage buff = ScreenImage.createImage(View.panel);
      ImageIO.write(buff, "png", new File("images/temp"));
      System.out.println("Screenshot has been taken");
   }
   
   public void generateMap(Rectangle rect) throws Exception
   {
      BufferedImage capture = ScreenImage.createImage(View.panel);
      BufferedImage key = capture.getSubimage(rect.x, rect.y, rect.width, rect.height);
      ImageIO.write(key, "png", new File("images/tempKey"));
      System.out.println("Key is generated");
   }
}