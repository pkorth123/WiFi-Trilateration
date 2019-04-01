/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signal_parsing;

public class ThreadC extends Thread {

    ResourceLock lock;

    ThreadC(ResourceLock lock) {
        this.lock = lock;
    }

    @Override
    public void run() {

        try {
            synchronized (lock) {
                if ((lock.flag == 1) || (lock.flag == 2)) {
                    lock.wait();
                } else {
                    float dist59 = parseSignal.getDistance("192.168.1.59", 5459, 6);
                    System.out.println(dist59 + "   59");
                    Thread.sleep(1000);
                    lock.flag = 1;
                    lock.notifyAll();
                }
            }
        } catch (Exception e) {
            System.out.println("Exception 3 :" + e.getMessage());
        }

    }
}
