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
            if (m.find() == true){
            signal = Float.parseFloat(m.group());
    //    }
            }
        return signal;
    }

    String line;

    public String getStream(String host, int portNumber) throws IOException {

        Socket liveDump = new Socket(host, portNumber);
        BufferedReader in = new BufferedReader(new InputStreamReader(liveDump.getInputStream()));
        line = in.readLine();

        while ((line = in.readLine()) != null) {
            return line;
        }
        return line;

    }

    public String getLine() {
        return line;
    }

    float distance;

    public float getDistance(int n) {
        float signal = getSignalStrength(line);
        distance = (float) Math.pow(10, ((10 + (-(signal))) / (10 * n)));
        return distance;
    }

    public static Point2D getCoordinates(float dist57, float dist58, float dist59) {
        Point2D coordinates = new Point2D.Float(0, 0);
        Point2D p1 = new Point2D.Float(0, 0);
        Point2D p2 = new Point2D.Float((float) 6.5, 0);
        Point2D p3 = new Point2D.Float((float) 3.85, (float) 5.8);

        double x = 0;
        double y = 0;

        x = (((Math.pow(dist57, 2)) - (Math.pow(dist58, 2)) + (Math.pow(p2.getX(), 2))) / (2 * (p2.getX())));

        y = ((Math.pow(dist57, 2)) - (Math.pow(dist59, 2) + (Math.pow((p3.getX()), 2)) + (Math.pow(p3.getY(), 2)) - (2 * (p3.getX()) * x))) / (2 * (p3.getY()));

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
