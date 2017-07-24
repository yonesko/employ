package gleb;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

public class MyExecutor implements Executor {
    private static final int PARALLEL_LIMIT = 2;
    private static final SpeedLimit SPEED_LIMIT = new SpeedLimit(10, 2);

    private Executor executor = Executors.newFixedThreadPool(PARALLEL_LIMIT);

    private static final Phaser PHASER = new Phaser();


    private volatile int amount = SPEED_LIMIT.amount;

    public MyExecutor() {
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                amount = (SPEED_LIMIT.amount);
                System.out.println(String.format("Set speed limit amount to %d", SPEED_LIMIT.amount));
            }
        }, 0, TimeUnit.SECONDS.toMillis(SPEED_LIMIT.intervalSeconds));
    }

    @Override
    public void execute(Runnable command) {
        while (true) {
            synchronized (this) {
                if (amount > 0) {
                    executor.execute(command);
                    amount--;
                    break;
                }
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
