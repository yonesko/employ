package controller;

import model.Greeting;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class RestController {
    private int i;

    @RequestMapping(value = "/", method = GET)
    public String index() {
        return "/privet.html";
    }

    @RequestMapping("/greeting")
    @ResponseBody
    Greeting home(@RequestParam(value = "name", defaultValue = "World") String name) {

        return new Greeting(i++, String.format("Hello, %s", name));
    }
}
