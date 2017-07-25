package gleb.data;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import gleb.data.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskRepoSimple implements TaskRepo {
    private Table<String, Integer, Task> taskTable = HashBasedTable.create();

    public synchronized void save(String userid, Task task) {
        taskTable.put(userid, task.getId(), task);
        System.out.println(String.format("Saved %s task for user %s", task, userid));
    }

    @Override
    public synchronized boolean update(String userid, Task task) {
        Task taskInTable = taskTable.get(userid, task.getId());
        if (taskInTable != null) {
            taskInTable.setStatusPayload(task.getStatusPayload());
            taskInTable.setStatus(task.getStatus());

            System.out.println(String.format("Updated %s task for user %s", taskInTable, userid));
            return true;
        }

        System.out.println(String.format("Failed to update %s task fo user %s: no such task in table", task, userid));
        return false;
    }

    @Override
    public synchronized void delete(String userid, int id) {
        taskTable.remove(userid, id);
        System.out.println(String.format("Removed %d task for user %s", id, userid));
    }

    @Override
    public List<Task> getAll(String userid) {
        return new ArrayList<>(taskTable.row(userid).values());
    }

}
