package gleb.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;

@Entity
@Data
public class Task {
    private DigestTaskStatus status;
    private Instant received;
    private String algo;
    private String src;
    @Id
    @GeneratedValue
    private int id;

    private String payload;
//    String result;
//    String errText;


    public Task(String algo, String src) {
        this.algo = algo;
        this.src = src;
        status = DigestTaskStatus.WAIT;
        received = Instant.now();
    }
}
