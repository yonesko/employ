package gleb.main;

import gleb.data.TaskRepo;
import gleb.data.TaskRepoOnMap;
import gleb.util.concurrent.TimeAndParallerLimitExecutor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executor;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    TaskRepo taskRepo() {
        return new TaskRepoOnMap();
    }

    @Bean
    Executor myExecutor() {
        return new TimeAndParallerLimitExecutor();
    }
}
