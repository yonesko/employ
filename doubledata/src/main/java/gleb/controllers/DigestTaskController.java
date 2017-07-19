package gleb.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/digesttask")
public class DigestTaskController {
    private static final char[] HEX_CHARS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public String calcHash(@RequestParam("algo") String algo, @RequestParam("file") MultipartFile file) throws IOException, NoSuchAlgorithmException {
        byte[] digest = MessageDigest.getInstance(algo).digest(file.getBytes());
        return String.valueOf(encodeHex(digest));
    }

    @ResponseBody()
    @PutMapping("/add")
    public Map<String, String> addTask(@RequestParam("src") String src, @RequestParam("algo") String algo) {
        Map<String, String> r = new HashMap<>();
        r.put("statusR", "OK");
        System.out.println(src);
        return r;
    }

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
