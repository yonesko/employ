package gleb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/task")
public class Controller {

    @Autowired
    TaskRepo taskRepo;

    static List<SseEmitter> list = Collections.synchronizedList(new ArrayList<>());

    @PutMapping
    void add(@RequestParam("src") String src, @RequestParam("algo") String algo) {
        Task task = new Task(src, algo);
        taskRepo.add(task);
        notifyUser(TaskEvent.ADD, task);
    }

    @DeleteMapping
    void delete(@RequestParam("id") int id) {
        taskRepo.delete(id);
        notifyUser(TaskEvent.DELETE, id);
    }

    @GetMapping
    List<Task> getAll() {
        return taskRepo.getAll();
    }

    @GetMapping("/event")
    SseEmitter getSSE() {
        return SseEmiterFactory.create();
    }

    void notifyUser(TaskEvent taskEvent, Object data) {
        list.forEach(sse -> {
            try {
                sse.send(SseEmitter.event().name(taskEvent.name()).data(data));
            } catch (IOException e) {
                System.err.println(String.format("Problem notifyUser %s", data));
            }
        });
    }

    enum TaskEvent {
        ADD,
        CHANGE,
        DELETE
    }

    static class SseEmiterFactory {
        static SseEmitter create() {
            SseEmitter sseEmitter = new SseEmitter();

            System.out.println(String.format("Created SSE %s", sseEmitter));

            sseEmitter.onCompletion(() -> {
                list.remove(sseEmitter);
                System.out.println(String.format("Removed SSE %s", sseEmitter));
            });
            sseEmitter.onTimeout(() -> {
                list.remove(sseEmitter);
                System.out.println(String.format("Removed SSE %s", sseEmitter));
            });

            list.add(sseEmitter);

            return sseEmitter;
        }
    }

}
