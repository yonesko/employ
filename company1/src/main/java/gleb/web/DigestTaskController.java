package gleb.web;

import gleb.data.TaskRepo;
import gleb.data.model.Task;
import gleb.util.concurrent.DigestRunnable;
import gleb.util.concurrent.DigestRunnableFactory;
import gleb.util.concurrent.TimeAndParallelLimitExecutor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

@RestController
@RequestMapping("/task")
public class DigestTaskController {
    private static final String ALGO_DEFAULT = "MD5";
    private final TaskRepo taskRepo;
    private final TimeAndParallelLimitExecutor executor;
    private final DigestRunnableFactory digestRunnableFactory;

    public DigestTaskController(TaskRepo taskRepo, TimeAndParallelLimitExecutor executor, DigestRunnableFactory digestRunnableFactory) {
        this.taskRepo = taskRepo;
        this.executor = executor;
        this.digestRunnableFactory = digestRunnableFactory;
    }

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

    @GetMapping("big")
    long[] big() {
        long r[] = new long[(int) 1e7];
        Random random = new Random();
        for (int i = 0; i < r.length; i++) {
            r[i] = i;
        }

        return r;
    }

    private void submit(String userId, String src, String algo) {
        Task task = new Task(src, algo);

        taskRepo.save(userId, task);

        executor.execute(digestRunnableFactory.create(task, userId));
    }
}
