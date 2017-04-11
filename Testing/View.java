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

class View extends JFrame implements ActionListener
{
   public static Model model;
   public static Controller controller;
   public static MyPanel panel;
   public static JSplitPane split;
   public static JSplitPane finalSplit;
   private final JFileChooser fc = new JFileChooser();
   
   
   View(Controller c, Model m) throws Exception
   {
      this.model = m;
      this.controller = c;
      
      //General information for the frame
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setSize(700, 600);
      this.setTitle("SVAS");
      
      //Generate the different panels for the frame
      //The frame is the main window, which contains 3 individual panels
      this.panel = new MyPanel(model); //Panel contains the actual frame from "avi"
      JPanel buttonsPanel = new JPanel(); //Panel contains extra buttons for features of GUI 
      JPanel stepPanel = new JPanel(); //Panel allows control to step through an "avi" video file
      buttonsPanel.setLayout(new GridLayout(2,2));
      
      //Generate the different buttons for the UI
      JButton browse = new JButton("Upload File");
      browse.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            //Handle open button action.
            if (e.getSource() == browse) 
            {
               int returnVal = fc.showOpenDialog(null);

               if (returnVal == JFileChooser.APPROVE_OPTION) 
               {
                  File file = fc.getSelectedFile();
                  try
                  {
                     View.panel.addImage(file.getAbsolutePath());
                     if(View.panel.frame != null)
                     {
                        View.split.setDividerLocation(View.panel.frame.getWidth());
                        View.finalSplit.setDividerLocation(View.panel.frame.getHeight());
                     }
                  }
                  catch(Exception er)
                  {
                    er.printStackTrace();
                  }
               } 
               else 
               {
                  System.out.println("Open command cancelled by user.");
               }
            }
         }
      });
      JButton clear = new JButton("Clear Vectors");
      clear.addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent act)
         {
            if(act.getSource() == clear)
            {
               //Clear the vectors drawn
               View.model.getVectors().clear();
            }
         }
      });
      JButton next = new JButton("Next");
      JButton prev = new JButton("Previous");
      
      
      //Add controller to main frame interface, add necessary buttons to button panel
      buttonsPanel.add(browse);
      buttonsPanel.add(clear);
      stepPanel.add(prev);
      stepPanel.add(next);
      this.panel.addMouseListener(controller);
      
      //Create a spilt panel for easier use and add to the frame
      this.split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel, buttonsPanel);
      this.finalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, split, stepPanel);
      
      //Set up where the screens split in the GUI 
      //setOneTouchExpandable allows the splits to be resized
      split.setOneTouchExpandable(true);
      finalSplit.setOneTouchExpandable(true);
      split.setDividerLocation(400);
      finalSplit.setDividerLocation(500);
      //Adds all the panels to the overall frame
      this.add(finalSplit);
    
      //Force frame to show in center of screen and make it visible to user
      this.setLocationRelativeTo(null);
      this.setVisible(true);
   }
   
   public void actionPerformed(ActionEvent evt)
   {   
      repaint(); //calls MyPanel.paintComponent
   }
}