package java0.conc0301;

public class DaemonThread {
    
    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> {
                try {
                    Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
                Thread t = Thread.currentThread();
                System.out.println("当前线程:" + t.getName());
        };
        Thread thread = new Thread(task);
        thread.setName("test-thread-1");
        //守护线程，当前除主线程外，没有其他活跃线程，自动结束
        thread.setDaemon(true);
        thread.start();
        System.out.println(Thread.activeCount());
        //Thread.sleep(2000);
    }
    
    
}
