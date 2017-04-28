import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.io.File;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Robot;
import java.awt.Rectangle;
import java.io.PrintWriter;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import javax.swing.JFileChooser;
import java.text.DecimalFormat;

class Model
{
   private Controller controller;
   private ArrayList<Vector> vectors;

   /******************************************************************************
   //Method: Model (constructor)
   //Purpose: Create the model (handles the logic behind creating vectors) and the
   //   list of vectors for the GUI
   ********************************************************************************/
   public Model(Controller c)
   {
      this.controller = c;
      this.vectors = new ArrayList<Vector>();
   }

   /********************************************************************************
   //Method: Truncate
   //Return: double (after rounding)
   //Purpose: Round all velocity (and average speed) to two decimal points
   ********************************************************************************/
   public double truncate(double value)
   {
      DecimalFormat df = new DecimalFormat("#.##");
      String trunc = df.format(value);

      return Double.parseDouble(trunc);
   }   
   
   /********************************************************************************
   //Method: getVectors
   //Return: list of vectors
   //Purpose: Allow other classes in program to get the list of vectors
   ********************************************************************************/
   public ArrayList<Vector> getVectors() { return this.vectors; }

   /*********************************************************************************
   //Method: addVector
   //Return: None (void)
   //Purpose:  Take in a (x,y) and RGB value where the user clicked
   //          Add a vector to the list that is the proper size according
   //          to the map provided in VFI_Map.java
   *********************************************************************************/
   public void addVector(int x1, int y1, int color)
   {
      //find x2, y2 (distance from center of color hue)
      //X2 is X difference from center of color wheel to the selected color from the frame
      int x2 = VFI_Map.getDistances(color)[0];
      //Y2 is Y difference from center of color wheel to the selected color from the frame
      int y2 = VFI_Map.getDistances(color)[1];
      //Set the end point of the vector to relative distance the the color is on the color wheel
      x2 = (x1 - x2);
      y2 = (y1 - y2);
      //Get the velocity magnitude
      double vel = VFI_Map.getVelocity(color);
      double Vx = VFI_Map.getVx(color);
      double Vy = VFI_Map.getVy(color);

      vectors.add(new Vector(x1, y1, x2, y2, 5, 2, Vx, Vy, vel));
   }

   /**************************************************************************************
   //NOTE: Code below is used to create an image in the working directory of the program.
   //  During testing, this code was placed after creating the "screenShot" variable in 
   //  the "captureScreen" method.  The resulting image will show the removal of grayscale
   //  colors in a still frame from an avi file.
   **************************************************************************************/
   /*try 
      {
         int WIDTH = screenShot.getWidth();
         int HEIGHT = screenShot.getHeight();
         BufferedImage image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_ARGB);
         
         for(int i = 0; i < HEIGHT; i++)
         {
            for(int j = 0; j < WIDTH; j++)
            {
               Color c = new Color(screenShot.getRGB(j, i));
               float[] hsv = new float[3];
               
               Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsv);
               if((hsv[1] < 0.2)||(hsv[2] < 0.2))
               {
                  image.setRGB(j, i, 0x00ffffff);
               }
               else
               {
                  image.setRGB(j, i, screenShot.getRGB(j, i));
               }
            }
         }
         
         ImageIO.write(image, "PNG", new File("temp.png"));
         
       } 
       catch (Exception e) 
       {
          e.printStackTrace();
       }    */  
   
   /***************************************************************************************
   //Method: captureScreen
   //Return: None (void)
   //Purpose: Allow user to take a screen shot of a selected rectangle in the frame and 
   //   gather the average velocity of that region
   ***************************************************************************************/
   public void captureScreen(Rectangle rect) throws Exception
   {
      BufferedImage capture = ScreenImage.createImage(View.panel);//Grab only the frame from the GUI
      BufferedImage screenShot = capture.getSubimage(rect.x, rect.y, rect.width, rect.height);
       
      int count = 0;
      double sum = 0.0;
      
      for(int i = 0; i < screenShot.getHeight(); i++)
      {
         for(int j = 0; j < screenShot.getWidth(); j++)
         {
            Color c = new Color(screenShot.getRGB(j, i));
            int color = screenShot.getRGB(j, i);
            float[] hsv = new float[3];
      
            Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsv);
            //If the color is not gray scale (aka "is color")
            if((hsv[1] > 0.2)&&(hsv[2] > 0.2))
            {
               sum += VFI_Map.getVelocity(color);
               count++;
            }
          }
      }
      
      double tempAverage = sum / count;
      //Save average only to two decimal places
      double average = truncate(tempAverage);
      Main.alert("Average speed in region is " + average);
   }

   /**************************************************************************************
   //Method: writeFullFile
   //Return: None (void)
   //Purpose: Write out x, y, Vx, Vy, and Velocity (magnitude) for all color pixels in 
   //  frame's image
   **************************************************************************************/
   public void writeFullFile(Rectangle rect)
   {
      BufferedImage capture = ScreenImage.createImage(View.panel);
      BufferedImage screenShot = capture.getSubimage(rect.x, rect.y, rect.width, rect.height);
      int lineCount = 1;
      
      Main.alert("Choose location to save file");
      
      String directory = "";
      JFileChooser fc = new JFileChooser();
      fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      int returnVal = fc.showOpenDialog(null);

      if (returnVal == JFileChooser.APPROVE_OPTION)
      {
         File name = fc.getSelectedFile();
         //Grab full path of directory
         directory = name.getAbsolutePath();
      }
      else
         Main.alert("Must be a directory");
      
      //If a directory is chosen
      if(!directory.equals(""))
      {
         String fullpath = directory + "/" + View.panel.getFrameName() + ".csv";
      
         try
         {
            File file = new File(fullpath);
            PrintWriter printWriter = new PrintWriter(file);
            //Build the header of the spreadsheet file
            StringBuilder sb = new StringBuilder();
            sb.append("Line");
            sb.append(',');
            sb.append("X");
            sb.append(",");
            sb.append("Y");
            sb.append(",");
            sb.append("Vx");
            sb.append(",");
            sb.append("Vy");
            sb.append(",");
            sb.append("Speed");
            sb.append('\n');
            
            printWriter.write(sb.toString());
            
            for(int i = 0; i < screenShot.getHeight(); i++)
            {
               for(int j = 0; j < screenShot.getWidth(); j++)
               {
                  Color c = new Color(screenShot.getRGB(j, i));
                  int color = screenShot.getRGB(j, i);
                  float[] hsv = new float[3];
      
                  Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsv);
                  //If the color is not gray scale (aka "is color")
                  if((hsv[1] > 0.2)&&(hsv[2] > 0.2))
                  {
                     double Vx = VFI_Map.getVx(color);
                     double Vy = VFI_Map.getVy(color);
                     double velocity = VFI_Map.getVelocity(color);
                     printWriter.write(lineCount + ", " + j + ", " + i + ", " + truncate(Vx) + ", " + truncate(Vy) + ", " + truncate(velocity) + "\n");
                     lineCount++;
                  }
               }
            }
            printWriter.close();
         }
         catch(FileNotFoundException e)
         {
            Main.alert("Error when creating file");
         }
         
         Main.alert("Finished writing file");
      }
      else
      {
         Main.alert("No directory chosen (file was not written)");
      }
   }
   
   /**************************************************************************************
   //Method: writeVecFile
   //Return: None (void)
   //Purpose: Write out drawn vectors of a frame to a file
   **************************************************************************************/
   public void writeVecFile()
   {
      Main.alert("Choose location to save file");
      
      String directory = "";
      JFileChooser fc = new JFileChooser();
      fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      int returnVal = fc.showOpenDialog(null);

      if (returnVal == JFileChooser.APPROVE_OPTION)
      {
         File name = fc.getSelectedFile();
         //Grab full path of directory
         directory = name.getAbsolutePath();
      }
      else
         Main.alert("Must be a directory");
   
   
      if(!directory.equals(""))
      {
         String fullpath = directory + "/" + View.panel.getFrameName() + ".csv";
       
         if(vectors.size() > 0)
         {
            try
            {
               File file = new File(fullpath);
               PrintWriter printWriter = new PrintWriter(file);
               //Build the header of the spreadsheet file
               StringBuilder sb = new StringBuilder();
               sb.append("Vector Number");
               sb.append(',');
               sb.append("X");
               sb.append(",");
               sb.append("Y");
               sb.append(",");
               sb.append("Vx");
               sb.append(",");
               sb.append("Vy");
               sb.append(",");
               sb.append("Speed");
               sb.append('\n');
            
               printWriter.write(sb.toString());
               
               for(int i = 0; i < vectors.size(); i++)
               {
                  int vectorNum = (i+1); //add one because of index at 0
                  printWriter.write(vectorNum + ", " + vectors.get(i).x1 + ", " + vectors.get(i).y1 + ", " + truncate(vectors.get(i).vel_X) + ", " + truncate(vectors.get(i).vel_Y) + ", " + truncate(vectors.get(i).velocity) + "\n");
               }
               printWriter.close();
            }
            catch(FileNotFoundException e)
            {
               Main.alert("Error when creating file");
            }
         }
         else
         {
            Main.alert("No vectors are drawn");
         }
      
         Main.alert("Drawn vector file is created");
      }
      else
      {
         Main.alert("No file was written (directory not chosen)");
      }
   }
}