public class Deadlock {
    public static void main(String[] args) {
        Object resource1 = new Object();
        Object resource2 = new Object();

        Thread t1 = new Thread("Thread 1") {
            public void run() {
                try{
                    synchronized (resource1) {
                        System.out.println(Thread.currentThread().getName() + " Has Resource 1");
                        sleep(1000);
                        synchronized (resource2) { 
                            System.out.println(Thread.currentThread().getName() + " Has Resource 2");
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                    System.exit(0);
                }
            }       
        };     

        Thread t2 = new Thread("Thread 2") {
            public void run() {
                try {
                    synchronized (resource2) {
                        System.out.println(Thread.currentThread().getName() + " Has Resource 2");
                        sleep(1000);
                        synchronized (resource1) {
                            System.out.println(Thread.currentThread().getName() + " Has Resource 1");
                        }
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                    System.exit(0);
                }
            }       
        };
        // The two threads have hold and wait over exclusive resources. No preemption is employed so they never give up their respective resources. 
        t1.start();
        t2.start();
    }
}