package dbService.models;

public class User {
    private int id;
    private String cmd;
    private int cmdReceived;

    public User(int id, String cmd, int cmdReceived) {
        this.id = id;
        this.cmd = cmd;
        this.cmdReceived = cmdReceived;
    }

    public int getId() {
        return id;
    }

    public String getCmd() {
        return cmd;
    }

    public int getCmdReceived() {
        return cmdReceived;
    }
}
