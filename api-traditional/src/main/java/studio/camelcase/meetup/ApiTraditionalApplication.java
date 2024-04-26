package studio.camelcase.meetup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ApiTraditionalApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiTraditionalApplication.class, args);
    }

}
