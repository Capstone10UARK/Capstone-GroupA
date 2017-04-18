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
      x2 = x1 - x2;
      y2 = y1 - y2;
      //Get the velocity magnitude
      double vel = VFI_Map.getVelocity(color);

      vectors.add(new Vector(x1, y1, x2, y2, 5, 2, vel));
   }
   
   /***************************************************************************************
   //Method: captureScreen
   //Return: None (void)
   //Purpose: Allow user to take a screen shot of a selected rectangle in the frame
   ***************************************************************************************/
   public void captureScreen(Rectangle rect) throws Exception
   {
      BufferedImage capture = ScreenImage.createImage(View.panel);//Grab only the frame from the GUI
      BufferedImage screenShot = capture.getSubimage(rect.x, rect.y, rect.width, rect.height);
   }
   
   /**************************************************************************************
   //Method: writeVecFile
   //Return: None (void)
   //Purpose: Write out vectors of a frame to a file
   **************************************************************************************/
   public void writeVecFile()
   {
      /**********************************************************************************
      //NEEDS MORE WORK
      //Save the file to the same name as the frame being analyzed
      //Possibly look at re-saving the frame image to save the vectors that are drawn
      **********************************************************************************/
      if(vectors.size() > 0)
      {
         try
         {
            File file = new File("testing.txt");
            PrintWriter printWriter = new PrintWriter(file);
            for(int i = 0; i < vectors.size(); i++)
            {
               int vectorNum = (i+1); //add one because of index at 0
               printWriter.write("Vector number: " + vectorNum + " base (X): " + vectors.get(i).x1 + " base (Y): " + vectors.get(i).y1 + " Velocity: " + vectors.get(i).velocity + "\n");
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
   }
}