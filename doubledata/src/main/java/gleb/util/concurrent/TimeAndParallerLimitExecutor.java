package gleb.util.concurrent;

import org.apache.commons.lang3.concurrent.TimedSemaphore;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

import java.util.concurrent.*;

@ManagedResource
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

    @ManagedAttribute
    public int getQueueSize() {
        return queue.size();
    }

    @ManagedAttribute
    public int getLimit() {
        return timedSemaphore.getLimit();
    }

    @ManagedAttribute
    public int getLastAcquiresPerPeriod() {
        return timedSemaphore.getLastAcquiresPerPeriod();
    }

    @ManagedAttribute
    public int getAcquireCount() {
        return timedSemaphore.getAcquireCount();
    }

    @ManagedAttribute
    public int getAvailablePermits() {
        return timedSemaphore.getAvailablePermits();
    }

    @ManagedAttribute
    public double getAverageCallsPerPeriod() {
        return timedSemaphore.getAverageCallsPerPeriod();
    }

    @ManagedAttribute
    public long getPeriod() {
        return timedSemaphore.getPeriod();
    }

    @ManagedAttribute
    public TimeUnit getUnit() {
        return timedSemaphore.getUnit();
    }

}
