package gleb.web;

import gleb.data.TaskRepo;
import gleb.data.model.Task;
import gleb.util.concurrent.DigestRunnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.Executor;

@RestController
@RequestMapping("/task")
public class DigestTaskController {

    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private Executor executor;

    @PutMapping
    void add(@CookieValue("userid") String userid, @RequestParam("src") String src, @RequestParam("algo") String algo) {
        Task task = new Task(src, algo);

        taskRepo.save(userid, task);

        executor.execute(new DigestRunnable(taskRepo, userid, task));
    }

    @DeleteMapping
    void delete(@CookieValue("userid") String userid, @RequestParam("id") int id) {
        taskRepo.delete(userid, id);
    }

    @GetMapping
    List<Task> getAll(@CookieValue("userid") String userid) {
        return taskRepo.getAll(userid);
    }


}
