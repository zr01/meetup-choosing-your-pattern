package studio.camelcase.meetup.configs;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AsyncConfig {

    @Bean("jobExecutor")
    public Executor jobExecutor() {
        return Executors.newFixedThreadPool(500);
    }
}
