package gleb.data;

import gleb.data.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TaskRepoOnMap implements TaskRepo {
    private Map<Integer, Task> taskTable = new ConcurrentHashMap<>();

    public void save(Task task) {
        taskTable.put(task.getId(), task);
        System.out.println(String.format("Saved %s task", task));
    }

    @Override
    public synchronized boolean update(Task task) {
        Task taskInTable = taskTable.get(task.getId());
        if (taskInTable != null) {
            taskInTable.setStatusPayload(task.getStatusPayload());
            taskInTable.setStatus(task.getStatus());

            System.out.println(String.format("Updated %s task", taskInTable));
            return true;
        }

        System.out.println(String.format("Failed to update %s task: no such task in table", task));
        return false;
    }

    @Override
    public void delete(int id) {
        taskTable.remove(id);
        System.out.println(String.format("Removed %d task", id));
    }

    @Override
    public List<Task> getAll() {
        return new ArrayList<>(taskTable.values());
    }

}
