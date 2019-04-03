/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signal_parsing;

public class ThreadA extends Thread {

    ResourceLock lock;
   
    ThreadA(ResourceLock lock) {
        this.lock = lock;
    }

    @Override
    public void run() {

        try {
            synchronized (lock) {
                
                if((lock.flag != 1)){
                    lock.wait();
                }else{
                    float dist57 = parseSignal.getDistance("192.168.1.57", 5457, 6);
                    System.out.println(dist57 + "   57");
                    Thread.sleep(1000);
                    lock.flag = 2;
                    lock.notifyAll();
                }

            }
        } catch (Exception e) {
            System.out.println("Exception 1 :" + e.getMessage());
        }

    }

}
