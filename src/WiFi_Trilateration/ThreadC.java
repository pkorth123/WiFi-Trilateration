/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WiFi_Trilateration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadC extends Thread {

    double dist59;
    double dist59Prev;
    float n = 6;
    int txPower = 20;
    String line;
    String host = "192.168.1.59";
    int port = 5459;
    float signal;

    Lock lock;

    ThreadC(Lock lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            Socket liveStream59 = new Socket(host, port);//open connection
            while (true) {
                //buffer has to be completely wiped every time of values returned are not realtime
                BufferedReader in = new BufferedReader(new InputStreamReader(liveStream59.getInputStream()));
                line = in.readLine(); //read a line
                if (!in.readLine().isEmpty()) {
                    signal = Parser.getSignalStrength(line);
                    try {
                        synchronized (lock) {
                            if (lock.flag != 3) {
                                lock.wait();
                            } else if (signal != 0) {
                                dist59 = Math.pow(10, ((txPower - signal) / (10 * n)));//do the distance calulation
                                dist59Prev = dist59;
                               // System.out.println(signal);
                                Thread.sleep(100);
                                lock.flag = 1; //change lock
                                lock.notifyAll(); //notify other threads of lock status       
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Exception 1 :" + e.getMessage());

                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ThreadA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public double getDist59() {
        return dist59;
    }

}
