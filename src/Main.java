import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;
import java.awt.Graphics;

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
}