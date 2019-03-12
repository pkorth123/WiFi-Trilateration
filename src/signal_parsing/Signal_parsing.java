/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signal_parsing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.*;
import java.net.*;

/**
 *
 * @author Pat
 */
public class Signal_parsing {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
        
        int portNumber = 5432;

        Socket liveDump = new Socket("192.168.1.59", portNumber);
        PrintWriter out = new PrintWriter(liveDump.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(liveDump.getInputStream()));
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

//        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Pat\\Desktop\\hello\\02-chan1-back.txt"));   
        String line;
        boolean infinite = true;
        while (infinite == true) {
                    System.out.println("here1");
            while ((line = in.readLine()) != null) {
                System.out.println("here2");
          //      Thread.sleep(100);
                float signalStrength = 0;
                float seconds = getSecond(line);
                int i = 0;
                while (((line = in.readLine()) != null) && (getSecond(line) < (seconds + 1)) && (getSecond(line) >= seconds)) {
                    i++;
                    signalStrength += getSignalStrength(line);
                    line = in.readLine();
                }

                double distance = 0;
                double n = 4;
                distance = Math.pow(10, ((10 + (-signalStrength / i)) / (10 * n)));
                System.out.print("at " + seconds + " seconds" + " the signal strength was ");
                System.out.print(signalStrength / i + "dBm, ");
                System.out.println("This is associated with a distance of " + distance + " meters given an n-value of " + n + ".");
                System.out.println();
            }
        }
    }

    // parse signal strength out
    public static float getSignalStrength(String line) {
        float signal = 0;
        Pattern p = Pattern.compile("[-][0-9]*");
        Matcher m = p.matcher(line);
        m.find();
        signal = Float.parseFloat(m.group());

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

}
