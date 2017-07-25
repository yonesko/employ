package gleb.web;

import gleb.data.TaskRepo;
import gleb.data.model.Task;
import gleb.util.concurrent.DigestRunnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executor;

@RestController
@RequestMapping("/task")
public class DigestTaskController {

    public static final String ALGO_DEFAULT = "MD5";
    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private Executor executor;

    @PutMapping
    void add(@CookieValue("userid") String userid, @RequestParam("src") String src, @RequestParam("algo") String algo) {
        submit(userid, src, algo);
    }

    @PostMapping
    void handleFileUpload(@CookieValue("userid") String userid, MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            Scanner scanner = new Scanner(file.getInputStream());
            while (scanner.hasNext()) {
                submit(userid, scanner.next(), ALGO_DEFAULT);
            }
        }
    }

    @DeleteMapping
    void delete(@CookieValue("userid") String userid, @RequestParam("id") int id) {
        taskRepo.delete(userid, id);
    }

    @GetMapping
    List<Task> getAll(@CookieValue("userid") String userid) {
        return taskRepo.getAll(userid);
    }

    private void submit(String userid, String src, String algo) {
        Task task = new Task(src, algo);

        taskRepo.save(userid, task);

        executor.execute(new DigestRunnable(taskRepo, userid, task));
    }
}
