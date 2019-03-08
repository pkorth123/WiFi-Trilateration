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
        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Pat\\Desktop\\hello\\02-chan1-back.txt"));
        String line;
        while ((line = br.readLine()) != null) {
            float signalStrength = 0;
            float seconds = getSecond(line);
            int i = 0;
            while(((line = br.readLine()) !=null) && (getSecond(line) < (seconds+1)) && (getSecond(line) >= seconds)){
                i++;
                signalStrength += getSignalStrength(line);
                line = br.readLine();
            }
            
            float distance = 0;
            float n = 4;
            System.out.print(seconds + "  ");
            System.out.println(signalStrength/i);
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
