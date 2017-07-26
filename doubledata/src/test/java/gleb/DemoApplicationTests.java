package gleb;

import gleb.data.model.Task;
import gleb.web.UserIdFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
@AutoConfigureMockMvc
public class DemoApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    private Cookie users[] = {new Cookie(UserIdFilter.USERID, "userid1"), new Cookie(UserIdFilter.USERID, "userid2")};

    @Test
    public void taskAdd() throws Exception {
        Task task = new Task("http://google.com", "MD5");

        final int tasknum = 7;
        for (Cookie user : users) {
            for (int i = 0; i < tasknum; i++) {
                mockMvc.perform(put("/task")
                        .param("src", task.getSrc())
                        .param("algo", task.getAlgo())
                        .cookie(user))
                        .andExpect(status().isOk());
            }
        }

        for (Cookie user : users) {
            mockMvc.perform(get("/task").cookie(user)).andExpect(jsonPath("$.length()", "").value(tasknum));

            for (int i = 0; i < tasknum; i++) {
                mockMvc.perform(get("/task").cookie(user)).andExpect(jsonPath("$[%d].src", i).value(task.getSrc()));
                mockMvc.perform(get("/task").cookie(user)).andExpect(jsonPath("$[%d].algo", i).value(task.getAlgo()));
                mockMvc.perform(get("/task").cookie(user)).andExpect(jsonPath("$[%d].status", i).value(task.getStatus().name()));
                mockMvc.perform(get("/task").cookie(user)).andExpect(jsonPath("$[%d].statusPayload", i).isEmpty());
            }
        }
    }
}
