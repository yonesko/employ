package gleb.data;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import gleb.data.model.Task;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class TaskRepoSimple implements TaskRepo {
    private static Logger logger = Logger.getLogger(TaskRepoSimple.class);
    private Table<String, Integer, Task> taskTable = HashBasedTable.create();

    public synchronized void save(String userId, Task task) {
        taskTable.put(userId, task.getId(), task);
        if (logger.isTraceEnabled()) logger.trace(String.format("Saved %s task for user %s", task, userId));
    }

    @Override
    public synchronized boolean update(String userId, Task task) {
        Task taskInTable = taskTable.get(userId, task.getId());
        if (taskInTable != null) {
            taskInTable.setStatusPayload(task.getStatusPayload());
            taskInTable.setStatus(task.getStatus());

            if (logger.isTraceEnabled()) logger.trace(String.format("Updated %s task for user %s", taskInTable, userId));
            return true;
        }

        if (logger.isTraceEnabled()) logger.trace(String.format("Failed to update %s task fo user %s: no such task in table", task, userId));
        return false;
    }

    @Override
    public synchronized void delete(String userId, int id) {
        taskTable.remove(userId, id);
        if (logger.isTraceEnabled()) logger.trace(String.format("Removed %d task for user %s", id, userId));
    }

    @Override
    public synchronized List<Task> getAll(String userid) {
        return new ArrayList<>(taskTable.row(userid).values());
    }

}
