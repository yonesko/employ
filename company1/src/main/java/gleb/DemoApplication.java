package gleb;

import gleb.data.TaskRepo;
import gleb.data.TaskRepoSimple;
import gleb.util.concurrent.DigestRunnableFactory;
import gleb.util.concurrent.TimeAndParallelLimitExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@SpringBootApplication
@ComponentScan("gleb.web")
public class DemoApplication {
    @Autowired
    Environment env;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    TaskRepo taskRepo() {
        return new TaskRepoSimple();
    }

    @Bean
    TimeAndParallelLimitExecutor myExecutor() {
        return new TimeAndParallelLimitExecutor(Integer.valueOf(env.getProperty("timeAndParallelLimitExecutor.threadsInPool")),
                Integer.valueOf(env.getProperty("timeAndParallelLimitExecutor.timePeriodSeconds")),
                Integer.valueOf(env.getProperty("timeAndParallelLimitExecutor.limitOfTimePeriod")));
    }

    @Bean
    DigestRunnableFactory digestRunnableFactory(TaskRepo taskRepo) {
        return new DigestRunnableFactory(taskRepo);
    }
}