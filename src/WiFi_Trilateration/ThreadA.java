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

public class ThreadA extends Thread {

    double dist57;
    double dist57Prev;
    double n = 6;
    int txPower = 20;//measured one meter reference signal strength of macbook air (dBm @ 1 meter)
    String line;
    String host = "192.168.1.57";
    int port = 5457;
    float signal;

    Lock lock;

    ThreadA(Lock lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            Socket liveStream57 = new Socket(host, port);//open connection
            while (true) {
                BufferedReader in = new BufferedReader(new InputStreamReader(liveStream57.getInputStream()));
                line = in.readLine(); //read a line
              //  if (!in.readLine().isEmpty()) {
                    signal = Parser.getSignalStrength(line);
                    try {
                        synchronized (lock) {
                            if (lock.flag != 1) {
                                lock.wait();
                            } else if (signal !=0) {
                                dist57 = Math.pow(10, ((txPower - signal) / (10 * n)));//do the distance calulation
                                dist57Prev = dist57;
                                Thread.sleep(100);
                                lock.flag = 2; //change lock
                                lock.notifyAll(); //notify other threads of lock status       
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Exception 1 :" + e.getMessage());
                    }

              //  }
            }
        } catch (IOException ex) {
            Logger.getLogger(ThreadA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public double getDist57() {
        return dist57;
    }

}
