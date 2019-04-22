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

public class ThreadB extends Thread {

    double dist58 = 0;
    double dist58Prev = 0;
    float n = 6;
    int txPower = 34;
    String line;
    String host = "192.168.1.58";
    int port = 5458;
    float signal;

    Lock lock;

    ThreadB(Lock lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            Socket liveStream58 = new Socket(host, port);//open connection
            while (true) {
                BufferedReader in = new BufferedReader(new InputStreamReader(liveStream58.getInputStream()));
                line = in.readLine(); //read a line
               // if (!in.readLine().isEmpty()) {
                    signal = Parser.getSignalStrength(line);
                    try {
                        synchronized (lock) {
                            if (lock.flag != 2) {
                                lock.wait();
                            } else if (signal != 0){
                                dist58 = Math.pow(10, ((txPower - signal) / (10 * n)));//do the distance calulation
                                dist58Prev = dist58;
                                //System.out.println(signal);
                                Thread.sleep(100);
                                lock.flag = 3; //change lock
                                lock.notifyAll(); //notify other threads of lock status       
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Exception 1 :" + e.getMessage());
                    }

             //   }
            }
        } catch (IOException ex) {
            Logger.getLogger(ThreadA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public double getDist58() {
        return dist58;
    }

}
