import javax.swing.JFrame;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.awt.Color;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.event.WindowEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JFileChooser;
import java.util.concurrent.TimeUnit;

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
   private static BufferedImage frame;
   private static EmbeddedMediaPlayer emp;
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
   //Method: getPanelFrame
   //Return: BufferedImage frame
   //Purpose: Method for getting the image in the frame of this panel
   ************************************************************************/
   public BufferedImage getPanelFrame()
   {
      return this.frame;
   }
   
   /************************************************************************
   //Method: setPanelFrame
   //Return: None (void)
   //Purpose: Method for setting the frame with a BufferedImage
   ************************************************************************/
   public void setPanelFrame(BufferedImage panFrame)
   {
      this.frame = panFrame;
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
      
      String os = System.getProperty("os.name").toLowerCase();
      if(os.indexOf("mac") >= 0)
      {
         Main.alert("GUI does not support Mac playing a video");
      }
      else
      {
         //Canvas -> used to display video
         Canvas c = new Canvas();
         //background is black (add to the panel)
         c.setBackground(Color.black);
         c.addMouseListener(View.controller);
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
         //Setup video on first frame and then pause (let user control from there)
         emp.startMedia(emp.mrl());
         emp.pause();
      }
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
   
   /***************************************************************************
   //Method: nextFrame
   //Return: None (void)
   //Purpose: Move the video to the next frame
   ***************************************************************************/
   public void nextFrame()
   {
      emp.nextFrame();
   }
   
   /***************************************************************************
   //Method: getFrames
   //Return: None (void)
   //Purpose: Step through video and generate images of frames
   ***************************************************************************/
   public void getFrames()
   {
        //Generate additional tag on frames captured
        int count = 0;
        //Grab the directory chosen by the user
        String directory = "";
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fc.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) 
        {
           File file = fc.getSelectedFile();
           //Grab full path of directory
           directory = file.getAbsolutePath();
        }
        else
           Main.alert("Must be a directory");
      
        //If a directory location was selected for the screen shots then generate screen shots
        if(!(directory.equals("")))
        {    
           //Allow dialog box for selecting directory to disappear
           try
           {
              TimeUnit.MILLISECONDS.sleep(500);
           }
           catch(Exception ex)
           { 
              ex.printStackTrace();
           }
           while(true)
           {
               //emp.saveSnapshot(new File("frame" + Integer.toString(count)));
               BufferedImage image = emp.getVideoSurfaceContents();
               if(image != null)
               {
                  try
                  {
                     ImageIO.write(image, "PNG", new File(directory + "/Frame" + Integer.toString(count) + ".png"));
                  }
                  catch(IOException ex)
                  {
                     ex.printStackTrace();
                  }
               }
               
               count++;
               float current = emp.getPosition();
               float interval = 0.005f;
               if((emp.getPosition() > 1.0f) || (current + interval > 1.0f))
                  break;
               emp.setPosition((current + interval));
           }
         }
         else
            Main.alert("Did not get any frames");
            
        Main.alert("Capture frames was successful.");    
   }
   
   /***************************************************************************
   //Method: pauseVideo
   //Return: None (void)
   //Purpose: Pause the video
   ***************************************************************************/
   public void pauseVideo()
   {
      emp.setPause(true);
   }
}