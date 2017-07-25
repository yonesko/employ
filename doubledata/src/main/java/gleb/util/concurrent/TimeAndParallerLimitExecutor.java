package gleb.util.concurrent;

import org.apache.commons.lang3.concurrent.TimedSemaphore;

import java.util.concurrent.*;

public class TimeAndParallerLimitExecutor implements Executor {
    private Executor executor = Executors.newFixedThreadPool(1);
    private TimedSemaphore timedSemaphore = new TimedSemaphore(10, TimeUnit.SECONDS, 2);

    private BlockingQueue<Runnable> queue = new LinkedBlockingDeque<>();

    public TimeAndParallerLimitExecutor() {
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    timedSemaphore.acquire();
                    executor.execute(queue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    @Override
    public void execute(Runnable command) {
        queue.add(command);
    }
}
