package gleb.model;

import java.time.Instant;

public class Task {
    DigestTaskStatus status;
    Instant receivedTime;
    String result;
    String algo;
    String src;
    String errText;
}
