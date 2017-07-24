package gleb;

import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;

public class DigestRunnable implements Runnable {

    private final Task task;

    private TaskRepo taskRepo;

    public DigestRunnable(Task task, TaskRepo taskRepo) {
        this.task = task;
        this.taskRepo = taskRepo;
    }

    @Override
    public void run() {
        //imitate wait
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        task.setStatus(Task.Status.PROCCESS);
        if (!taskRepo.update(task)) {
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

        if (!taskRepo.update(task)) {
            System.out.println(String.format("Task %s has been deleted, stop processing", task));
        }
    }
}
