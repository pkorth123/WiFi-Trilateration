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

    static int width = 1021;
    static int height = 975;
    Point2D p1 = new Point2D.Float(0, 0);
    Point2D p2 = new Point2D.Float((float) 6.5, 0);
    Point2D p3 = new Point2D.Float((float) 3.85, (float) 5.8);
    static double x1 = 1, x2 = 23, x3 = 23, y1 = 12.5, y2 = 24, y3 = 1;
    static double A = ((-2 * x1) + (2 * x2));
    static double B = ((-2 * y1) + (2 * y2));
    static double D = ((-2 * x2) + (2 * x3));
    static double E = ((-2 * y2) + (2 * y3));

    public static void main(String[] args) throws IOException, InterruptedException {

        JFrame frame = new JFrame();//Frame init
        File f = new File("src/Images/TestingMap.jpg");
        BufferedImage img = ImageIO.read(f);
        JPanel canvas = new JPanel() {//override
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img, 0, 0, this);
            }
        };

        BackgroundImage background = new BackgroundImage(img);
        frame.setContentPane(background);
        frame.add(canvas);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(width, height);
        frame.setVisible(true);

        Lock lock = new Lock();
        ThreadA a = new ThreadA(lock);
        ThreadB b = new ThreadB(lock);
        ThreadC c = new ThreadC(lock);
        a.start();
        a.sleep(100);
        b.start();
        b.sleep(100);
        c.start();
        c.sleep(100);
        double x;
        double y;
        int mult = 100;//scale output points to fit frame dimensions
        while (true) {
            double dist57 = a.getDist57();
            a.sleep(100);

            double dist58 = b.getDist58();
            b.sleep(100);

            double dist59 = c.getDist59();
            c.sleep(100);

            Thread.sleep(400);
            x = ((Parser.getCoordinates(dist57, dist58, dist59).getX()) * mult);
            y = ((Parser.getCoordinates(dist57, dist58, dist59).getY()) * mult);
            if ((dist57 != 0) && (dist58 != 0) && (dist59 != 0)) {//don't print if any of the AP's did not receive a value
                System.out.println("(" + (x) + ", " + (y) + ")");
                Graphics g = img.getGraphics();
                g.setColor(Color.red);
                g.fillOval((int) x, (int) y, 50, 50);
                g.dispose();
                canvas.repaint();
            }

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

    public static Point2D getCoordinates(double d1, double d2, double d3) {
        double C = ((Math.pow(d1, 2)) - (Math.pow(d2, 2)) - (Math.pow(x1, 2)) + (Math.pow(x2, 2)) - (Math.pow(y1, 2)) + (Math.pow(y2, 2)));
        double F = ((Math.pow(d2, 2)) - (Math.pow(d3, 2)) - (Math.pow(x2, 2)) + (Math.pow(x3, 2)) - (Math.pow(y2, 2)) + (Math.pow(y3, 2)));
        double x = 0;
        double y = 0;
        Point2D coordinates = new Point2D.Float(0, 0);
        x = (((C * E) - (F * B)) / ((E * A) - (B * D)));
        y = (((C * D) - (A * F)) / ((B * D) - (A * E)));
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
