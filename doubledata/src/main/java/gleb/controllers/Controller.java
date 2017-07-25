package gleb.controllers;

import gleb.util.concurrent.DigestRunnable;
import gleb.data.model.Task;
import gleb.data.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.Executor;

@RestController
@RequestMapping("/task")
public class Controller {

    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private Executor executor;

    @PutMapping
    void add(@RequestParam("src") String src, @RequestParam("algo") String algo) {
        Task task = new Task(src, algo);

        taskRepo.save(task);

        executor.execute(new DigestRunnable(task, taskRepo));
    }

    @DeleteMapping
    void delete(@RequestParam("id") int id) {
        taskRepo.delete(id);
    }

    @GetMapping
    List<Task> getAll() {
        return taskRepo.getAll();
    }


}
