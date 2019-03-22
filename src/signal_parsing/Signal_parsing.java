/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signal_parsing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
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
    public static void main(String[] args) throws FileNotFoundException, IOException {
        int portNumber = 5432;
        String host = "192.168.1.58";
        getDistance(host, portNumber);
    }
    // parse signal strength out

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

    public static float getDistance(String host, int portNumber) throws IOException {

        Socket liveDump = new Socket(host, portNumber);
        BufferedReader in = new BufferedReader(new InputStreamReader(liveDump.getInputStream()));
        String line = null;
        float distance = 0;
        float n = (float) 6;
        while ((line = in.readLine()) != null) {
            line = in.readLine();

            distance = (float) Math.pow(10, ((10 + (-(getSignalStrength(line)))) / (10 * n)));
            System.out.println(distance);
        }
        return distance;
    }

}
