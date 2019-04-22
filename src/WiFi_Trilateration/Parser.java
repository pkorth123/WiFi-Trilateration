package WiFi_Trilateration;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * @author Pat
 */
public class Parser {

    static int frameWidth = 1021;//graphical display width
    static int frameHeight = 975;//graphical display height
    static int envWidth = 24;//multiplier for scaling height
    static int envHeight = 25;//multiplier for scaling width
    static double scaleX = (frameWidth/envWidth);//multiplier for scaling output width to frame 
    static double scaleY = (frameHeight/envWidth);//multiplier for scaling output height to frame 
    

    public static void main(String[] args) throws IOException, InterruptedException {

        JFrame frame = new JFrame();//frame init
        File f = new File("src/Images/TestingMap1.jpg");
        BufferedImage img = ImageIO.read(f);
        JPanel canvas = new JPanel() {//override paintComponent to draw on JFrame/background
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img, 0, 0, this);
            }
        };

        BackgroundImage background = new BackgroundImage(img);//create background class from image read
        frame.setContentPane(background);//set created background object on JFrame
        frame.add(canvas); //add paintComponent override to canvas
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(frameWidth, frameHeight);//set frame size
        frame.setVisible(true);

        Lock lock = new Lock();//make new lock object
        ThreadA a = new ThreadA(lock);//thread for ap1
        ThreadB b = new ThreadB(lock);//thread for ap2
        ThreadC c = new ThreadC(lock);//thread for ap3
        a.start();
        b.start();
        c.start();
        Thread.sleep(5000);//give AP's time to pull an initial value
        double x;
        double y;
     
        while (true) {
            double dist57 = a.getDist57();
           // Thread.sleep(300);

            double dist58 = b.getDist58();
           // Thread.sleep(300);

            double dist59 = c.getDist59();
           // Thread.sleep(400);

            Thread.sleep(2000);
            x = ((Parser.getCoordinates(dist57, dist58, dist59).getX())*scaleX);
            y = ((Parser.getCoordinates(dist57, dist58, dist59).getY())*scaleY);  
                System.out.println("(" + (x) + ", " + (y) + ")"); //print coordinates
                Graphics g = img.getGraphics();
                g.setColor(Color.red);
                g.fillOval((int) x, (int) y, 50, 50);
                g.setColor(Color.black);
                g.drawString("(" + (int)x + ", " + (int)y + ")", (int)x, (int)y);
                g.dispose();
                canvas.repaint();

        }

    }

    public static float getSignalStrength(String line) {
        float signal = 0;
        Pattern p = Pattern.compile("[-][0-9][0-9]");
        Matcher m = p.matcher(line);
        if (m.find() == true) {
            signal = Float.parseFloat(m.group());
        }
        return signal;
    }
    
    //AP coordinates
    Point2D p1 = new Point2D.Float(0, 0);
    Point2D p2 = new Point2D.Float((float) 6.5, 0);
    Point2D p3 = new Point2D.Float((float) 3.85, (float) 5.8);
    //place holders for x and y values below
    static double x1 = 1, x2 = 10.5, x3 = 23, y1 = 1, y2 = 13.5, y3 = 1;
    //place holders for expression
    static double A = ((-2 * x1) + (2 * x2));
    static double B = ((-2 * y1) + (2 * y2));
    static double D = ((-2 * x2) + (2 * x3));
    static double E = ((-2 * y2) + (2 * y3));

    public static Point2D getCoordinates(double d1, double d2, double d3) {
        
        double C = ((Math.pow(d1, 2)) - (Math.pow(d2, 2)) - (Math.pow(x1, 2)) + (Math.pow(x2, 2)) - (Math.pow(y1, 2)) + (Math.pow(y2, 2)));//create more reference shortcuts for substitution
        double F = ((Math.pow(d2, 2)) - (Math.pow(d3, 2)) - (Math.pow(x2, 2)) + (Math.pow(x3, 2)) - (Math.pow(y2, 2)) + (Math.pow(y3, 2)));
        double x =(((C * E) - (F * B)) / ((E * A) - (B * D)));;
        double y = (((C * D) - (A * F)) / ((B * D) - (A * E)));
        Point2D coordinates = new Point2D.Float(0, 0);
       
        
        coordinates.setLocation(x, y);
        return coordinates;
    }

    String line;

    public String getLine() {
        return line;
    }
    //parse out seconds from time stamp for testing

    public static float getSecond(String line) {
        float second = 0;
        Pattern p = Pattern.compile("[0-9][0-9][.][0-9]*");
        Matcher m = p.matcher(line);
        m.find();
        second = Float.parseFloat(m.group());
        return second;
    }

}
