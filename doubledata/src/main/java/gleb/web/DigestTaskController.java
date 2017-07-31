package gleb.web;

import gleb.data.TaskRepo;
import gleb.data.model.Task;
import gleb.util.concurrent.DigestRunnable;
import gleb.util.concurrent.TimeAndParallelLimitExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

@RestController
@RequestMapping("/task")
public class DigestTaskController {
    public static final String ALGO_DEFAULT = "MD5";
    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private TimeAndParallelLimitExecutor executor;

    @PutMapping
    void add(@CookieValue("userid") String userId, @RequestParam("src") String src, @RequestParam("algo") String algo) {
        submit(userId, src, algo);
    }

    @PostMapping
    void handleFileUpload(@CookieValue("userid") String userId, MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            Scanner scanner = new Scanner(file.getInputStream());
            while (scanner.hasNext()) {
                submit(userId, scanner.next(), ALGO_DEFAULT);
            }
        }
    }

    @DeleteMapping
    void delete(@CookieValue("userid") String userId, @RequestParam("id") int id) {
        taskRepo.delete(userId, id);
    }

    @GetMapping
    List<Task> getAll(@CookieValue("userid") String userId) {
        return taskRepo.getAll(userId);
    }

    private void submit(String userId, String src, String algo) {
        Task task = new Task(src, algo);

        taskRepo.save(userId, task);

        executor.execute(new DigestRunnable(taskRepo, userId, task));
    }
}
