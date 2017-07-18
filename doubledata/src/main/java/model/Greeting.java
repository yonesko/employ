package model;

/**
 * Created by User on 18.07.2017.
 */
public class Greeting {
    private final String content;
    private final int id;

    public Greeting(int id, String content) {
        this.id = id;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
