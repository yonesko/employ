package gleb.util.concurrent;

import org.apache.commons.lang3.concurrent.TimedSemaphore;

import java.util.concurrent.*;

public class TimeAndParallerLimitExecutor implements Executor {
    private Executor executor;
    private TimedSemaphore timedSemaphore;

    private BlockingQueue<Runnable> queue = new LinkedBlockingDeque<>();

    public TimeAndParallerLimitExecutor(int nThreads, int timePeriod, int limitOfTimePeriod) {
        executor = Executors.newFixedThreadPool(nThreads);
        timedSemaphore = new TimedSemaphore(timePeriod, TimeUnit.SECONDS, limitOfTimePeriod);

        Thread t = new Thread(() -> {
            while (true) {
                try {
                    timedSemaphore.acquire();
                    executor.execute(queue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "TimeAndParallerLimitExecutor task executor thread");
        t.setDaemon(true);
        t.start();
    }

    @Override
    public void execute(Runnable command) {
        queue.add(command);
    }
}
