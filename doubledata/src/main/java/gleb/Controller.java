package gleb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/task")
public class Controller {

    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private Executor executor = new MyExecutor();

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
