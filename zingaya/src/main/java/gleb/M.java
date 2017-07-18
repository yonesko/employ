package gleb;

public class M {
    public static void main(String[] args) throws InterruptedException {
        Object m = new Object();
        Thread t = new Thread();


        synchronized (m) {
            m.wait();
        }
    }
}
