package gleb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class TaskRepoSimpleList implements TaskRepo {
    List<Task> taskTable = Collections.synchronizedList(new ArrayList<>());

    public void add(Task task) {
        taskTable.add(task);
        System.out.println(String.format("Added task %s to BD", task));
    }

    public void delete(Task task) {
        taskTable.remove(task);
    }

    @Override
    public void delete(int id) {
        for (Iterator<Task> iterator = taskTable.iterator(); iterator.hasNext(); ) {
            Task task = iterator.next();
            if (task.getId() == id) {
                iterator.remove();
                System.out.println(String.format("Removed task %s to BD", task));
                return;
            }
        }
    }

    @Override
    public List<Task> getAll() {
        return new ArrayList<>(taskTable);
    }


}
