package gleb;

import java.util.concurrent.atomic.AtomicInteger;

public class Task {
    private final static AtomicInteger count = new AtomicInteger();

    private String src;
    private String algo;
    private int id;

    public Task(String src, String algo) {
        this.src = src;
        this.algo = algo;
        id = count.incrementAndGet();
    }


    public String getSrc() {
        return src;
    }

    public String getAlgo() {
        return algo;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "src='" + src + '\'' +
                ", algo='" + algo + '\'' +
                ", id=" + id +
                '}';
    }
}
