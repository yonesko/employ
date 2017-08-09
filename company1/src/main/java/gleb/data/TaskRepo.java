package gleb.data;

import gleb.data.model.Task;

import java.util.List;

public interface TaskRepo {
    void save(String userId, Task task);

    boolean update(String userId, Task task);

    void delete(String userId, int id);

    List<Task> getAll(String userId);
}
