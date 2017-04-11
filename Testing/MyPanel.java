import javax.swing.JFrame;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.awt.Color;
import javax.imageio.ImageIO;
import java.awt.event.WindowEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JFileChooser;

class MyPanel extends JPanel
{
   Model model;
   public static BufferedImage frame;

   MyPanel(Model m) throws Exception 
   {
      this.model = m;
   }
   
   public void paintComponent(Graphics g) 
   {
      // Draw the view
      g.drawImage(this.frame, 0, 0, null);
      //Draw Vectors if any exist
      g.setColor(Color.white);
      for(int i = 0; i < model.getVectors().size(); i++)
      {
         //Draws a line from point (x1, y1) to (x2, y2)
         g.drawLine(model.getVectors().get(i).x1, model.getVectors().get(i).y1, 
            model.getVectors().get(i).x2, model.getVectors().get(i).y2);
         //Draw arrow at the end of the line
         g.fillPolygon(model.getVectors().get(i).xpoints, model.getVectors().get(i).ypoints, 3);
      }
   }
   
   public void addImage(String filename) throws Exception
   {
      frame = ImageIO.read(new File(filename));
   }
   
}