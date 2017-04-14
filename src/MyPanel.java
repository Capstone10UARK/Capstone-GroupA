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

//Needed to support VLCJ (".avi" video)
import java.awt.BorderLayout;
import java.awt.Canvas;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

class MyPanel extends JPanel
{
   Model model;
   public static BufferedImage frame;
   private EmbeddedMediaPlayer emp;
   //Location of the VLCJ shared library (same location as the download for VLC)
   private static final String NATIVE_LIBRARY_SEARCH_PATH = "/usr/lib/vlc";

   /*************************************************************
   //Method: MyPanel (constructor)
   //Purpose: Construct the personalized panel which will contain
   //  the still frame or video
   **************************************************************/
   MyPanel(Model m) throws Exception 
   {
      this.model = m;
      //Load in the library for VLCJ
      NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), NATIVE_LIBRARY_SEARCH_PATH);
      Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
   }
   
   /******************************************************************
   //Method: paintComponent
   //Return: None (void)
   //Purpose: redraws the image (frame) and the vectors (if any exist
   // in the list)
   ******************************************************************/   
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
   
   /************************************************************************
   //Method: addImage
   //Return: None (void)
   //Purpose: sets the frame of the panel to contain the file selected
   ************************************************************************/
   public void addImage(String filename) throws Exception
   {
      //clear panel for image
      this.removeAll();
      frame = ImageIO.read(new File(filename));
   }
   
   /****************************************************************************
   //Method: addVideo
   //Return: None (void)
   //Purpose: sets the panel to contain the selected video
   ****************************************************************************/
   public void addVideo(String filename) throws Exception
   {
      //Get rid of any image in panel
      if(frame != null)
         frame = null;
      
      //Canvas -> used to display video
      Canvas c = new Canvas();
      //background is black (add to the panel)
      c.setBackground(Color.black);
      this.setLayout(new BorderLayout());
      this.add(c);
      //Init media player
      MediaPlayerFactory mpf = new MediaPlayerFactory();
      //control all interactions with the user
      emp = mpf.newEmbeddedMediaPlayer();
      emp.setVideoSurface(mpf.newVideoSurface(c));
      //full screen
      emp.toggleFullScreen();
      emp.setEnableMouseInputHandling(true);
      //disable keyboard
      emp.setEnableKeyInputHandling(false);
      //prepare file to read
      emp.prepareMedia(filename);
   }
   
   /**************************************************************************
   //Method: playVideo
   //Return: None (void)
   //Purpose: Play the uploaded video
   **************************************************************************/
   public void playVideo()
   {
     emp.play();
   }
}