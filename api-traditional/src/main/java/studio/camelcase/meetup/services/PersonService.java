package studio.camelcase.meetup.services;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;
import studio.camelcase.meetup.repositories.models.Person;

public interface PersonService {

    @Async
    CompletableFuture<Person> createUserAsync(Person person);
    Person createUserSync(Person person);

    Person getUserByExternalId(UUID externalId) throws RuntimeException;
}
