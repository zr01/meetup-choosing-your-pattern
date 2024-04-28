package studio.camelcase.meetup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@EnableR2dbcRepositories(basePackages = "studio.camelcase.meetup.repositories")
public class ApiReactiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiReactiveApplication.class, args);
    }

}
