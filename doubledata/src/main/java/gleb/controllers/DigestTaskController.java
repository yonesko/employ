package gleb.controllers;

import gleb.data.DigestTaskRepo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/digesttask")
public class DigestTaskController {

    private DigestTaskRepo taskRepo;
    private Map<String, SseEmitter> sses = new ConcurrentHashMap<>();

    @PutMapping("/add")
    public void addTask(@RequestParam("src") String src, @RequestParam("algo") String algo, @CookieValue("userid") String userId) {
//        taskRepo.save(new Task(algo, src));
        System.out.println(String.format("Adding '%s' '%s' task", src, algo));

        SseEmitter sseEmitter = sses.get(userId);
        if (sseEmitter != null)
            try {
                sseEmitter.send(SseEmitter.event().data("Privet from SSE"));
            } catch (IOException e) {
                System.err.println("sseEmitter.send err");
            }
    }

    @GetMapping("/getall")
    public SseEmitter getTasks(@CookieValue("userid") String userId) {
        SseEmitter sse = new SseEmitter(60 * 1000L);
        sse.onCompletion(() -> {
            System.out.println(String.format("Removing %s from SseEmitters", userId));
            sses.remove(userId);
        });
        System.out.println(String.format("Adding SSE for '%s' userid", userId));
        sses.putIfAbsent(userId, sse);
        return sse;
    }

    private static final char[] HEX_CHARS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static char[] encodeHex(byte[] bytes) {
        char chars[] = new char[32];
        for (int i = 0; i < chars.length; i = i + 2) {
            byte b = bytes[i / 2];
            chars[i] = HEX_CHARS[(b >>> 0x4) & 0xf];
            chars[i + 1] = HEX_CHARS[b & 0xf];
        }
        return chars;
    }
}
