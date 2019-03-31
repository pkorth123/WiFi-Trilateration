/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signal_parsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.net.*;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pat
 */
public class Signal_parsing {
//removed main method, should only have to call getDistance() to start printing values

    public static void main(String[] args) throws IOException {
        Signal_parsing parse = new Signal_parsing();
        parse.getThreads();
    }

    public void getThreads() {
        Semaphore one = new Semaphore(1);
        Semaphore two = new Semaphore(0);
        Semaphore three = new Semaphore(0);

        Thread router57 = new Thread(new Runnable() {

            public void run() {
                try {
                    try {
                        one.acquire();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Signal_parsing.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    float dist57 = getDistance("192.168.1.57", 5457, 6);
                    System.out.println(dist57 + "   57");
                    two.release();
                } catch (IOException ex) {
                    Logger.getLogger(Signal_parsing.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
        Thread router58 = new Thread(new Runnable() {
            //add if statements so that when all three value are not void it calcs a point
            public void run() {
                try {
                    try {
                        two.acquire();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Signal_parsing.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    float dist58 = getDistance("192.168.1.58", 5458, 6);
                    System.out.println(dist58 + "   58");
                    three.release();
                } catch (IOException ex) {
                    Logger.getLogger(Signal_parsing.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
        Thread router59 = new Thread(new Runnable() {

            public void run() {
                try {
                    try {
                        three.acquire();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Signal_parsing.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    float dist59 = getDistance("192.168.1.59", 5459, 6);
                    System.out.println(dist59 + "   59");
                    one.release();
                } catch (IOException ex) {
                    Logger.getLogger(Signal_parsing.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
        router57.start();
        router58.start();
        router59.start();

    }

    public static float getSignalStrength(String line) {
        float signal = 0;
        if (line != null) {
            Pattern p = Pattern.compile("[-][0-9][0-9]");
            Matcher m = p.matcher(line);
            m.find();
            signal = Float.parseFloat(m.group());
        }
        return signal;
    }
//parse out seconds from time stamp

    public static float getSecond(String line) {
        float second = 0;
        Pattern p = Pattern.compile("[0-9][0-9][.][0-9]*");
        Matcher m = p.matcher(line);
        m.find();
        second = Float.parseFloat(m.group());
        return second;
    }

    public static float getDistance(String host, int portNumber, float n) throws IOException {

        Socket liveDump = new Socket(host, portNumber);
        BufferedReader in = new BufferedReader(new InputStreamReader(liveDump.getInputStream()));
        String line = null;
        float distance = 0;

        while ((line = in.readLine()) != null) {
            float signal = getSignalStrength(line);
            float startTime = System.currentTimeMillis();
            int i = 0;
            while ((line = in.readLine()) != null && (startTime < (startTime + 1000))) {
                i++;
                signal += getSignalStrength(line);
                signal = (signal / i);

            }
            distance = (float) Math.pow(10, ((10 + (-(signal))) / (10 * n)));
            System.out.println(distance + "  " + host);
        }
        return distance;

    }

}
