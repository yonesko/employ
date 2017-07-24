package gleb;

import java.util.List;

public interface TaskRepo {
    void add(Task task);

    void delete(Task task);

    void delete(int id);

    List<Task> getAll();
}
