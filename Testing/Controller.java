import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import java.awt.Graphics;
import java.io.IOException;
import javax.swing.Timer;
import java.util.LinkedList;
import java.io.IOException;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Rectangle;

class Controller implements MouseListener
{
   Model model;
   View view;
   
   Controller() throws Exception
   {
      this.model = new Model(this);
   }
   
   public static void startView() throws Exception
   {
      Controller c = new Controller();
      c.view = new View(c, c.model);
      new Timer(20, c.view).start();
   }
   
   public void mousePressed(MouseEvent e)
   {
      if(SwingUtilities.isLeftMouseButton(e))
      {
         if(MyPanel.frame != null)
            model.addVector(e.getX(), e.getY(), MyPanel.frame.getRGB(e.getX(), e.getY()));
      }
      else if(SwingUtilities.isRightMouseButton(e))
      {
         try
         {
            Rectangle rect = ScreenCaptureRectangle.getCapture();
            if(rect != null)
            {
               model.generateMap(rect);
            }
            else
               System.out.println("Did not read capture properly.");
         }
         catch(Exception ex)
         {
            ex.printStackTrace();
         }
      }
   }
   
   public void mouseReleased(MouseEvent e) {    }
   public void mouseEntered(MouseEvent e) {    }
   public void mouseExited(MouseEvent e) {    }
   public void mouseClicked(MouseEvent e) {    }
}