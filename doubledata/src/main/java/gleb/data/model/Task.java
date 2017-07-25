package gleb.data.model;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

public class Task {
    private final static AtomicInteger count = new AtomicInteger();

    private final String src;
    private final String algo;
    private final int id;
    private final Instant received;

    private Status status;
    private String statusPayload;

    public Task(String src, String algo) {
        this.src = src;
        this.algo = algo;
        id = count.incrementAndGet();
        status = Status.WAIT;
        received = Instant.now();
    }

    public Instant getReceived() {
        return received;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setStatusPayload(String statusPayload) {
        this.statusPayload = statusPayload;
    }

    public Status getStatus() {
        return status;
    }

    public String getStatusPayload() {
        return statusPayload;
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
                ", received=" + received +
                ", status=" + status +
                ", statusPayload='" + statusPayload + '\'' +
                '}';
    }

    public enum Status {
        WAIT, PROCCESS, OK, ERR
    }
}
