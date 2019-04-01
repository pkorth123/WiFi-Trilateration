/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signal_parsing;

public class ThreadB extends Thread {

    ResourceLock lock;

    ThreadB(ResourceLock lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            synchronized (lock) {
                if((lock.flag==1) || (lock.flag==3)){
                    lock.wait();
                }else{
                    float dist58 = parseSignal.getDistance("192.168.1.58", 5458, 6);
                    System.out.println(dist58 + "   58");
                    Thread.sleep(1000);
                    lock.flag = 3;
                    lock.notifyAll();
                }

            }
        } catch (Exception e) {
            System.out.println("Exception 2 :" + e.getMessage());
        }

    }
}
