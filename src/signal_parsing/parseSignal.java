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
        Thread.sleep(3000);
        b.start();
        Thread.sleep(3000);
        c.start();
        Thread.sleep(3000);

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
