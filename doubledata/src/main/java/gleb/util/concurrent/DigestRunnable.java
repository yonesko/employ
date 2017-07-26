package gleb.util.concurrent;

import gleb.data.TaskRepo;
import gleb.data.model.Task;
import gleb.util.Util;

import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;

public class DigestRunnable implements Runnable {
    private TaskRepo taskRepo;
    private String userid;
    private Task task;

    public DigestRunnable(TaskRepo taskRepo, String userid, Task task) {
        this.taskRepo = taskRepo;
        this.userid = userid;
        this.task = task;
    }

    @Override
    public void run() {
        task.setStatus(Task.Status.PROCCESS);
        if (!taskRepo.update(userid, task)) {
            System.out.println(String.format("Task %s has been deleted, stop processing", task));
            return;
        }

        //imitate calc
        try {
            Thread.sleep(1000);

            MessageDigest digest = MessageDigest.getInstance(task.getAlgo());
            URL url = new URL(task.getSrc());

            InputStream inputStream = url.openStream();
            while (inputStream.available() > 0)
                digest.update((byte) inputStream.read());

            task.setStatus(Task.Status.OK);
            task.setStatusPayload(Util.bytesToHex(digest.digest()));
        } catch (Exception e) {
            task.setStatus(Task.Status.ERR);
            task.setStatusPayload(e.toString());
        }

        if (!taskRepo.update(userid, task)) {
            System.out.println(String.format("Task %s has been deleted, stop processing", task));
        }
    }
}
