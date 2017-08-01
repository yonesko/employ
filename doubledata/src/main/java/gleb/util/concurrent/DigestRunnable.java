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
        task.setStatus(Task.Status.PROCESS);
        if (!taskRepo.update(userid, task)) {
            System.out.println(String.format("Task %s has been deleted, stop processing", task));
            return;
        }

        //imitate calc
        try {
            Thread.sleep(1000);

            MessageDigest digest = MessageDigest.getInstance(task.getAlgo());
            URL url = new URL(task.getSrc());

            try (InputStream inputStream = url.openStream()) {
                long bytes = 0;
                byte buf[] = new byte[1024];

                for (int n = inputStream.read(buf); n > 0; bytes += n, n = inputStream.read(buf))
                    digest.update(buf, 0, n);

                System.out.println(String.format("bytes=%d", bytes));
            }

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
