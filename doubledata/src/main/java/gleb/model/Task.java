package gleb.model;

import java.time.Instant;

public class Task {
    private DigestTaskStatus status;
    private final Instant receivedTime;
    private final String algo;
    private final String src;
    private final int id;

    private String payload;
//    String result;
//    String errText;


    public Task(DigestTaskStatus status, Instant receivedTime, String algo, String src, int id) {
        this.status = status;
        this.receivedTime = receivedTime;
        this.algo = algo;
        this.src = src;
        this.id = id;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
