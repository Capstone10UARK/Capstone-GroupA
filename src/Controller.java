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
   public static View view;

   /*************************************************************************
   //Method: Controller (constructor)
   //Purpose: Construct the controller for the GUI
   *************************************************************************/
   Controller() throws Exception
   {
      this.model = new Model(this);
   }

   /*************************************************************************
   //Method: startView
   //Return: None (void)
   //Purpose: Call the constructor for the controller and create the view
   //  add a timer to the view so that the view gets redrawn (helps with
   //  having to draw vectos)
   *************************************************************************/
   public static void startView() throws Exception
   {
      Controller c = new Controller();
      c.view = new View(c, c.model);
      new Timer(20, c.view).start();
   }

   /*************************************************************************
   //Method: mousePressed
   //Return: None (void)
   //Purpose: Listen for mouse being pressed in the GUI.  Add a vector to
   //  location the moused clicked
   *************************************************************************/
   public void mousePressed(MouseEvent e)
   {
      if(SwingUtilities.isLeftMouseButton(e))
      {
         if(view.panel.getPanelFrame() != null)
         {
            Color c = new Color(view.panel.getPanelFrame().getRGB(e.getX(), e.getY()));
            float[] hsv = new float[3];
            //Add vector only if the pixel is non grayscale color
            Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsv);
            //Condition to not draw on grayscale locations
            if((hsv[1] > 0.2)&&(hsv[2] > 0.2))
              model.addVector(e.getX(), e.getY(), view.panel.getPanelFrame().getRGB(e.getX(), e.getY()));
         }
         //WARNING: Drawing on video does not work
         /*if(View.panel.getComponents().length != 0)
         {
            View.panel.drawVideo(e.getX(), e.getY());
         }*/
      }
   }

   public void mouseReleased(MouseEvent e) {    }
   public void mouseEntered(MouseEvent e) {    }
   public void mouseExited(MouseEvent e) {    }
   public void mouseClicked(MouseEvent e) {    }
}
