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

class View extends JFrame implements ActionListener
{
   Model model;
   Controller controller;
   private MyPanel panel;
   
   View(Controller c, Model m) throws Exception
   {
      this.model = m;
      this.controller = c;
      
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setTitle("SVAS");
      this.panel = new MyPanel(model);
      this.setSize(MyPanel.frame.getWidth(null), MyPanel.frame.getHeight(null));
      this.panel.addMouseListener(controller);
      this.getContentPane().add(this.panel);
      this.setVisible(true);
   }
   
   public void actionPerformed(ActionEvent evt)
   {
      repaint(); //calls MyPanel.paintComponent
   }
}