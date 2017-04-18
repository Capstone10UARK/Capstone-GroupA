import javax.swing.JFrame;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.awt.Image;
import java.util.ArrayList;
import java.awt.Color;
import javax.imageio.ImageIO;
import java.awt.event.WindowEvent;
import java.awt.event.MouseListener;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JSplitPane;
import java.awt.GridLayout;
import java.awt.Rectangle;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import java.text.Format;
import java.text.NumberFormat;
import javax.swing.JLabel;
import java.awt.Toolkit;

class View extends JFrame implements ActionListener
{
   public static Model model;
   public static Controller controller;
   public static MyPanel panel;
   private static JSplitPane split;
   private static JSplitPane finalSplit;
   private final JFileChooser fc = new JFileChooser();
   
   //Buttons
   private static JButton browse;
   private static JButton clear;
   private static JButton capture;
   private static JButton maxVel;
   private static JButton createFile;
   private static JButton next;
   private static JButton play;
   private static JButton pause;
   private static JButton grabFrames;
   
   /***********************************************************************************************
   //Method: View (constructor)
   //Return: None (void)
   //Purpose: Create the view for the user.  View is a frame that contains panels.  Panels can 
   //   contain butons, a frame, or a video
   ***********************************************************************************************/
   View(Controller c, Model m) throws Exception
   {
      this.model = m;
      this.controller = c;
      
      //General information for the frame
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
      this.setTitle("SVAS");
      
      //Generate the different panels for the frame
      //The frame is the main window, which contains 3 individual panels
      this.panel = new MyPanel(model); //Panel contains the actual frame from "avi"
      JPanel buttonsPanel = new JPanel(); //Panel contains extra buttons for features of GUI 
      JPanel stepPanel = new JPanel(new GridLayout(2,2)); //Panel allows control to step through an "avi" video file
      buttonsPanel.setLayout(new GridLayout(3,2));
      
      //Generate the different buttons for the UI
      browse = new JButton("Upload File");
      browse.addActionListener(this);
      //Button to allow user to clear the vectors drawn on the screen 
      clear = new JButton("Clear Vectors");
      clear.addActionListener(this);
      //Button to allow user to capture a portion of the screen
      capture = new JButton("Get Average Velocity in Area");
      capture.addActionListener(this);
      //Button to allow user to set the maximum velocity
      maxVel = new JButton("Set Velocity");
      maxVel.addActionListener(this);
      //Create a file of the vectors drawn 
      createFile = new JButton("Create Vector File");
      createFile.addActionListener(this);
      
      //Buttons to control the video
      next = new JButton("Next");
      next.addActionListener(this);
      play = new JButton("Play");
      play.addActionListener(this);
      pause = new JButton("Pause");
      pause.addActionListener(this);
      grabFrames = new JButton("Grab Frames");
      grabFrames.addActionListener(this);
      
      //Add controller to main frame interface, add necessary buttons to button panel
      buttonsPanel.add(browse);
      buttonsPanel.add(capture);
      buttonsPanel.add(maxVel);
      buttonsPanel.add(clear);
      buttonsPanel.add(createFile);
      stepPanel.add(pause);
      stepPanel.add(play);
      stepPanel.add(next);
      stepPanel.add(grabFrames);
      this.panel.addMouseListener(controller);
      
      //Create a spilt panel for easier use and add to the frame
      this.split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel, buttonsPanel);
      this.finalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, split, stepPanel);
      
      //Set up where the screens split in the GUI 
      //setOneTouchExpandable allows the splits to be resized
      split.setOneTouchExpandable(true);
      finalSplit.setOneTouchExpandable(true);
      //Set splits 3/4ths across the x direction for the vertical bar and 4/5ths down for the horizontal bar
      split.setDividerLocation(3*(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 4);
      finalSplit.setDividerLocation(4*(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 5);
      //Adds all the panels to the overall frame
      this.add(finalSplit);
    
      //Force frame to show in center of screen and make it visible to user
      this.setLocationRelativeTo(null);
      this.setVisible(true);
   }
   
   /*********************************************************************************
   //Method: getUserInput (stand-alone window)
   //Return: None (void)
   //Purpose: Create a popup window to get user input and set the maximum Velocity
   *********************************************************************************/
   public void getUserInput()
   {
      JFrame getMax = new JFrame();
      JPanel instructions = new JPanel();
      JPanel userInput = new JPanel();
      JPanel submission = new JPanel();
      
      JLabel instruction = new JLabel("Enter the maximum velocity");
      instructions.add(instruction);
      
      JTextField inputField = new JTextField(20);
      userInput.add(inputField);
      
      JButton submit = new JButton("Submit");
      submit.addActionListener(new ActionListener()
      {
          @Override 
          public void actionPerformed(ActionEvent act)
          {
             if(act.getSource() == submit)
             {
                String text = inputField.getText();
                VFI_Map.setMaxVelocity(Double.parseDouble(text));
                getMax.dispose();
             }
          }
      });
      submission.add(submit);
      
      getMax.add(instructions, BorderLayout.NORTH);
      getMax.add(inputField, BorderLayout.CENTER);
      getMax.add(submission, BorderLayout.SOUTH);
      
      getMax.setLocationRelativeTo(null);
      getMax.setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/5, (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/20);
      getMax.setVisible(true);
   }
   
   /*********************************************************************************
   //Method: actionPerformed
   //Return: None (void)
   //Purpose: Watch for actions in the view and repaint the panel that contains the 
   //  frame so that vectors can be seen
   *********************************************************************************/
   public void actionPerformed(ActionEvent act)
   {  
       if(act.getSource() == clear)
       {
          //Clear the vectors drawn
          View.model.getVectors().clear();
       }
       else if (act.getSource() == browse) 
       {
          int returnVal = fc.showOpenDialog(null);

          if (returnVal == JFileChooser.APPROVE_OPTION) 
          {
             File file = fc.getSelectedFile();
             //Grab file's name and extension
             String fileName = file.getName();
             String extension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
             try
             {
                if(extension.equals("jpeg")||extension.equals("png"))
                   View.panel.addImage(file.getAbsolutePath());
                else if(extension.equals("avi")||extension.equals("mp4"))
                   View.panel.addVideo(file.getAbsolutePath());
                else
                   Main.alert("File type selected is not supported (accepts: '.jpeg' '.png' '.avi' and '.mp4')");
             }
             catch(Exception er)
             {
                er.printStackTrace();
             }
          }
          else 
          {
             Main.alert("Open command cancelled by user");
          }
       }
       else if(act.getSource() == capture)
       {
          if(View.panel.getPanelFrame() != null)
          {
             try
             {
                //Grab Rectangle highlighted in the popup generated by ScreenCaptureRectangle.java
                Rectangle rect = ScreenCaptureRectangle.getCapture();
                if(rect != null)
                {
                   //Use the model to handle getting the average velocity from the region selected
                   View.model.captureScreen(rect);
                }
                else
                   System.out.println("Did not read capture properly.");
             }
             catch(Exception ex)
             {
                ex.printStackTrace();
             }
          }
          else
             Main.alert("No still image shown in frame");
       }
       else if(act.getSource() == maxVel)
       {
          getUserInput();
       }
       else if(act.getSource() == play)
       {
          //Check if there is a video in the panel
          if(View.panel.getComponents().length != 0)
             View.panel.playVideo();
          else
             Main.alert("A video is not uploaded");
       }
       else if(act.getSource() == next)
       {
          if(View.panel.getComponents().length != 0)
             View.panel.nextFrame();
          else
             Main.alert("A video is not uploaded");
       }
       else if(act.getSource() == pause)
       {
          if(View.panel.getComponents().length != 0)
             View.panel.pauseVideo();
          else
             Main.alert("A video is not uploaded");
       }
       else if(act.getSource() == grabFrames)
       {
          if(View.panel.getComponents().length != 0)
             View.panel.getFrames();
          else
             Main.alert("A video is not uploaded");
       }
       else if(act.getSource() == createFile)
       {
          if(View.panel.getPanelFrame() != null)
             View.model.writeVecFile();
          else
             Main.alert("No still image shown in frame");
       }
       
       repaint(); //calls MyPanel.paintComponent
   }
}