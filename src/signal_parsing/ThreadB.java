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

public class ThreadB extends Thread {

    double dist58 = 0;
    float n = 9;
    int txPower = 24;
    String line;
    String host = "192.168.1.58";
    int port = 5458;
    ResourceLock lock;

    ThreadB(ResourceLock lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            Socket liveDump57 = new Socket(host, port);
            while (true) {
                BufferedReader in = new BufferedReader(new InputStreamReader(liveDump57.getInputStream()));
                line = in.readLine();
                if (!in.readLine().isEmpty()) {
                    float signal = Parser.getSignalStrength(line);
                    try {
                        synchronized (lock) {

                            if (lock.flag != 2) {
                                lock.wait();
                            } else {
                                dist58 = (float) Math.pow(10, ((txPower - signal) / (10 * n)));
                                if (dist58 == 0) {
                                    run();
                                } else {//if dist58 was not assigned a value, run again
                                    Thread.sleep(100);
                                    lock.flag = 3;
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

    public double getDist58() {
        return dist58;
    }

}
