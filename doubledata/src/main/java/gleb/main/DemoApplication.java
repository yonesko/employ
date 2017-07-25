package gleb.main;

import gleb.data.TaskRepo;
import gleb.data.TaskRepoSimple;
import gleb.util.concurrent.TimeAndParallerLimitExecutor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.concurrent.Executor;

@SpringBootApplication
@ComponentScan("gleb.web")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    TaskRepo taskRepo() {
        return new TaskRepoSimple();
    }

    @Bean
    Executor myExecutor() {
        return new TimeAndParallerLimitExecutor();
    }
}
