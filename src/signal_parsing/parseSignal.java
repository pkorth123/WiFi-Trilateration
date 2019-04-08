/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signal_parsing;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.net.*;

/**
 *
 * @author Pat
 */
public class parseSignal {
//removed main method, should only have to call getDistance() to start printing values

    public static void main(String[] args) throws IOException, InterruptedException {
        ResourceLock lock = new ResourceLock();

        ThreadA a = new ThreadA(lock);
        ThreadB b = new ThreadB(lock);
        ThreadC c = new ThreadC(lock);

        a.start();
        a.sleep(100);
        b.start();
        b.sleep(100);
        c.start();
        c.sleep(100);
        while (true) {
            float dist57 = a.getDist57();

            a.sleep(100);
            // System.out.println(dist57);

            float dist58 = b.getDist58();

            b.sleep(100);
            // System.out.println(dist58);

            float dist59 = c.getDist59();

            c.sleep(100);
            //System.out.println(dist59);
            Thread.sleep(700);

            System.out.println(parseSignal.getCoordinates(dist57, dist58, dist59));
        }
    }

    public static float getSignalStrength(String line) {
        float signal = 0;
        //    Pattern wep = Pattern.compile("WEP");
        //   Matcher match = wep.matcher(line);
        //  if (match.find() != true) {
        Pattern p = Pattern.compile("[-][0-9][0-9]");
        Matcher m = p.matcher(line);
        if (m.find() == true) {
            signal = Float.parseFloat(m.group());
            //    }
        }
        return signal;
    }

    String line;

    public String getLine() {
        return line;
    }

    float distance;

    Point2D p1 = new Point2D.Float(0, 0);
    Point2D p2 = new Point2D.Float((float) 6.5, 0);
    Point2D p3 = new Point2D.Float((float) 3.85, (float) 5.8);
    static double x1 = 0, x2 = 6.5, x3 = 3.85, y1 = 0, y2 = 0, y3 = 5.8;
    static double A = ((-2 * x1) + (2 * x2));
    static double B = ((-2 * y1) + (2 * y2));
    static double D = ((-2 * x2) + (2 * x3));
    static double E = ((-2 * y2) + (2 * y3));
    public static Point2D getCoordinates(float d1, float d2, float d3) {
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
}

//parse out seconds from time stamp
/*
    public static float getSecond(String line) {
        float second = 0;
        Pattern p = Pattern.compile("[0-9][0-9][.][0-9]*");
        Matcher m = p.matcher(line);
        m.find();
        second = Float.parseFloat(m.group());
        return second;
    }
 */
