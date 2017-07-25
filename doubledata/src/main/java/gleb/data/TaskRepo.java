package gleb.data;

import gleb.data.model.Task;

import java.util.List;

public interface TaskRepo {
    void save(Task task);

    boolean update(Task task);

    void delete(int id);

    List<Task> getAll();
}
