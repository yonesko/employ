package gleb.data;

import gleb.data.model.Task;

import java.util.List;

public interface TaskRepo {
    void save(String userid, Task task);

    boolean update(String userid,Task task);

    void delete(String userid,int id);

    List<Task> getAll(String userid);
}
