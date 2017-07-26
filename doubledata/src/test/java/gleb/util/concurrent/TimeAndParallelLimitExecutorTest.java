package gleb.util.concurrent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class TimeAndParallelLimitExecutorTest {
    public static final int N_THREADS = 4;
    public static final int TIME_PERIOD = 1;
    public static final int LIMIT_OF_TIME_PERIOD = 7;
    private Executor executor;

    @Before
    public void init() {
        executor = new TimeAndParallelLimitExecutor(N_THREADS, TIME_PERIOD, LIMIT_OF_TIME_PERIOD);
    }

    @Test
    public void checkParallel() throws Exception {
        AtomicInteger started = new AtomicInteger(0);

        for (int i = 0; i < 10; i++)
            executor.execute(() -> {
                started.incrementAndGet();
                LockSupport.park();
            });

        Thread.sleep(1000);

        Assert.assertEquals(N_THREADS, started.get());
    }

    @Test
    public void checkTimed() throws Exception {
        int bgiEnough = 100, periods = 3;
        AtomicInteger started = new AtomicInteger(0);

        for (int i = 0; i < bgiEnough; i++) executor.execute(started::incrementAndGet);

        Thread.sleep(TimeUnit.SECONDS.toMillis(periods * TIME_PERIOD));
        Assert.assertEquals(LIMIT_OF_TIME_PERIOD * periods, started.get());
    }
}