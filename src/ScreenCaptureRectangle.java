import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.io.File;
import javax.imageio.ImageIO;

public class ScreenCaptureRectangle 
{

    Rectangle captureRect;

    ScreenCaptureRectangle(final BufferedImage screen) 
    {
        //copy of the frame from the avi file
        final BufferedImage screenCopy = new BufferedImage(screen.getWidth(), screen.getHeight(),screen.getType());
        final JLabel screenLabel = new JLabel(new ImageIcon(screenCopy));
        //Allows you to scroll in the frame
        JScrollPane screenScroll = new JScrollPane(screenLabel);
        screenScroll.setPreferredSize(new Dimension((int)(screen.getWidth()), (int)(screen.getHeight())));

        JPanel panel = new JPanel(new BorderLayout());
        //Adds the frame and the scrolling feature to the frame
        panel.add(screenScroll, BorderLayout.CENTER);

        final JLabel selectionLabel = new JLabel("Drag a rectangle over the map to capture a screen shot!");
        //Add the label to the new frame to tell the user what to do
        panel.add(selectionLabel, BorderLayout.SOUTH);

        repaint(screen, screenCopy);
        screenLabel.repaint();

        //Logic that allows the pop-up windows to capture the rectangle that you highlight
        //Calls to "repaint" allow the pop-up to continue to show the user what they are highlighting 
            //when dragging the mouse
        screenLabel.addMouseMotionListener(new MouseMotionAdapter() 
        {

            Point start = new Point();

            @Override
            public void mouseMoved(MouseEvent me) {
                start = me.getPoint();
                repaint(screen, screenCopy);
                selectionLabel.setText("Start Point: " + start);
                screenLabel.repaint();
            }

            @Override
            public void mouseDragged(MouseEvent me) {
                Point end = me.getPoint();
                captureRect = new Rectangle(start,
                        new Dimension(end.x-start.x, end.y-start.y));
                repaint(screen, screenCopy);
                screenLabel.repaint();
                selectionLabel.setText("Rectangle: " + captureRect);
            }
        });

        JOptionPane.showMessageDialog(null, panel);
    }

    public void repaint(BufferedImage orig, BufferedImage copy) {
        Graphics2D g = copy.createGraphics();
        g.drawImage(orig,0,0, null);
        if (captureRect!=null) {
            g.setColor(Color.RED);
            g.draw(captureRect);
            g.setColor(new Color(255,255,255,150));
            g.fill(captureRect);
        }
        g.dispose();
    }

    public static Rectangle getCapture() throws Exception 
    {
        Robot robot = new Robot();
        ScreenCaptureRectangle capture = new ScreenCaptureRectangle(Controller.view.panel.getPanelFrame());
        if(capture.captureRect != null)
           return capture.captureRect;
        else
        {
           System.out.println("No defined rectangle");
           return null;
        }
    }
}
