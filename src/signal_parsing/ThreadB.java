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

    ResourceLock lock;

    ThreadB(ResourceLock lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            float dist58 = 0;
            int n = 6;
            Socket liveDump58 = new Socket("192.168.1.58", 5458);
            BufferedReader in = new BufferedReader(new InputStreamReader(liveDump58.getInputStream()));
            String line = in.readLine();
            while (true) {
                line =in.readLine();
                try {
                    synchronized (lock) {
                        
                        if (lock.flag != 2) {
                            lock.wait();
                        } else {
                            
                            dist58 = (float) Math.pow(10, ((10 - (parseSignal.getSignalStrength(line))) / (10 * n)));
                            System.out.println(dist58 + "   58");
                            Thread.sleep(1000);
                            lock.flag = 3;
                            lock.notifyAll();
                        }
                        
                    }
                } catch (Exception e) {
                    System.out.println("Exception 1 :" + e.getMessage());
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(ThreadA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
