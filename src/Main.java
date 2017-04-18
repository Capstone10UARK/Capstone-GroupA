import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;
import java.awt.Graphics;
import javax.swing.JOptionPane;

public class Main
{
   /**************************************************************
   //Method: main
   //Return: None (void)
   //Purpose: start the GUI and generate mapping with key
   **************************************************************/
   public static void main(String args[]) throws Exception
   {
      VFI_Map.Init();
      Controller.startView();
   }
   
   /************************************************************************
   //Method: alert
   //Return: None (void)
   //Purpose: Provide an error message if needed in program
   ************************************************************************/
   public static void alert(String infoMessage)
   {
      JOptionPane.showMessageDialog(null, infoMessage, "Message", JOptionPane.INFORMATION_MESSAGE);
   }
}