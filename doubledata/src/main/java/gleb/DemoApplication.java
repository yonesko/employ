package gleb;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }


    @Bean
    TaskRepo taskRepo() {
        return new TaskRepoSimpleList();
    }

    @Bean
    CommandLineRunner initData(TaskRepo taskRepo) {
        return args -> {
            taskRepo.add(new Task("Yandex", "MD5"));
            taskRepo.add(new Task("Rambler", "SHA256"));
        };
    }
}
