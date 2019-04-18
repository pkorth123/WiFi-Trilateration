/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signal_parsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadA extends Thread {

    double dist57 = 0;
    double dist57Prev = 0;
    float n = 9;
    int txPower = 24;
    String line;
    String host = "192.168.1.57";
    int port = 5457;

    ResourceLock lock;

    ThreadA(ResourceLock lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            Socket liveDump57 = new Socket(host, port);
            while (true) {
                dist57 = 0;
                BufferedReader in = new BufferedReader(new InputStreamReader(liveDump57.getInputStream()));
                line = in.readLine();
                if (!in.readLine().isEmpty()) {
                    float signal = Parser.getSignalStrength(line);
                    try {
                        synchronized (lock) {

                            if (lock.flag != 1) {
                                lock.wait();
                            } else {
                                dist57 = Math.pow(10, ((txPower - signal) / (10 * n)));
 
                                if (dist57 == 0)// || (dist57 >= (dist57Prev + 10)) || (dist57 <= (dist57Prev - 10))) 
                                {
                                    run();
                                } else {//if dist57 was not assigned a value, run again
                                    dist57Prev = dist57;
                                    Thread.sleep(100);
                                    lock.flag = 2;
                                    lock.notifyAll();
                                }
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

    public double getDist57() {
        return dist57;
    }

}
