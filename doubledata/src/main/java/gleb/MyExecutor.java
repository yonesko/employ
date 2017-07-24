package gleb;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class MyExecutor implements Executor {
    private static final int PARALLEL_LIMIT = 1;
    private static final SpeedLimit SPEED_LIMIT = new SpeedLimit(1, 3);

    private Executor executor = Executors.newFixedThreadPool(PARALLEL_LIMIT);

    private AtomicInteger amount = new AtomicInteger(SPEED_LIMIT.amount);

    public MyExecutor() {
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                amount.set(SPEED_LIMIT.amount);
            }
        }, 0, SPEED_LIMIT.intervalSeconds);
    }

    @Override
    public void execute(Runnable command) {
        while (true) {
            if (amount.get() > 0) {
                executor.execute(command);
                amount.decrementAndGet();
            }
        }
    }

    static class SpeedLimit {
        final int intervalSeconds;
        final int amount;

        SpeedLimit(int intervalSeconds, int amount) {
            this.intervalSeconds = intervalSeconds;
            this.amount = amount;
        }
    }

}
