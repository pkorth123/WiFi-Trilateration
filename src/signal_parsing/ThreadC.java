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

public class ThreadC extends Thread {

    ResourceLock lock;

    ThreadC(ResourceLock lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            float dist59 = 0;
            int n = 6;
            Socket liveDump57 = new Socket("192.168.1.59", 5459);
            BufferedReader in = new BufferedReader(new InputStreamReader(liveDump57.getInputStream()));
            String line = in.readLine();
            while (true) {
                line =in.readLine();
                try {
                    synchronized (lock) {
                        
                        if (lock.flag != 3) {
                            lock.wait();
                        } else {
                            
                            dist59 = (float) Math.pow(10, ((10 - parseSignal.getSignalStrength(line)) / (10 * n)));
                            System.out.println(dist59 + "   59");
                            Thread.sleep(1000);
                            lock.flag = 1;
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
