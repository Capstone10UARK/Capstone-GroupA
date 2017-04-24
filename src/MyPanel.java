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
   private static String frameName;
   private static EmbeddedMediaPlayer emp;
   //Location of the VLCJ shared library (same location as the download for VLC)
   private static final String NATIVE_LIBRARY_SEARCH_PATH = "./library/";

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
      File loadedImage = new File(filename);
      //Get name of frame without file extension
      String name = loadedImage.getName().substring(0, loadedImage.getName().indexOf("."));
      setFrameName(name);
      
      frame = ImageIO.read(loadedImage);
   }
   
   /************************************************************************
   //Method: setFrameName
   //Return: None (void)
   //Purpose: set the name of the frame so that if the user decides to 
   //   write data to a file, we can use the same name as the image
   ************************************************************************/
   public void setFrameName(String name) throws Exception
   {
      //Add "txt" extension for writing to a file
      name = name + ".txt";
      this.frameName = name;
   }

   /****************************************************************************
   //Method: getFrameName
   //Return: String (frame's name)
   //Purpose: return the name of the current frame to then save the text file
   //   (used in conjunciton with setFrameName)
   ****************************************************************************/
   public String getFrameName()
   {
      return this.frameName;
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
        
        //Set the time to jump for a single frame
        //1000 ms/s divided by frames/second = ms / frame
        float fps = emp.getFps();
        long singleFrame = (long)(1000 / fps);
           
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
               long currentTime = emp.getTime();
               //If the current time is over the length (video is over--break from loop)
               if(currentTime >= emp.getLength())
                  break;
                  
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
               long newTime = currentTime + singleFrame;
               //If the next step is over the length (video/capture is done)
               if(newTime >= emp.getLength())
                  break;
               
               //If we didn't break, set new time and continue to grab more frames
               emp.setTime(newTime);
               count++;
           }
       }
       else
          Main.alert("Did not get any frames (no directory selected)");
        
        if(count != 0)
           Main.alert("Capture frames was successful.");
        else
           Main.alert("No frames to capture (end of video)");
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
