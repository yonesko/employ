package gleb.util.concurrent;

import gleb.data.TaskRepo;
import gleb.data.model.Task;

public class DigestRunnableFactory {
    private final TaskRepo taskRepo;

    public DigestRunnableFactory(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    public DigestRunnable create(Task task, String userId) {
        return new DigestRunnable(taskRepo, userId, task);
    }
}
