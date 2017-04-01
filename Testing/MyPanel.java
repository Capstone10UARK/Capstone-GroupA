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

class MyPanel extends JPanel
{
   Model model;
   public static BufferedImage frame;

   MyPanel(Model m) throws Exception 
   {
      this.model = m;
      frame = ImageIO.read(new File("stillFrameNoVec.png"));
      //frame = ImageIO.read(new File("stillFrame.png"));
   }
   
   public void paintComponent(Graphics g) 
   {
      // Draw the view
      g.drawImage(this.frame, 0, 0, null);
      //Draw Vectors if any exist
      //for each vector 
      g.setColor(Color.white);
      for(int i = 0; i < model.getVectors().size(); i++)
      {
         g.drawLine(model.getVectors().get(i).x1, model.getVectors().get(i).y1, 
            model.getVectors().get(i).x2, model.getVectors().get(i).y2);
         g.fillPolygon(model.getVectors().get(i).xpoints, model.getVectors().get(i).ypoints, 3);
      }
   }
}