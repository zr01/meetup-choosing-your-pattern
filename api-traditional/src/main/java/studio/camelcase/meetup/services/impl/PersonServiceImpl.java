package studio.camelcase.meetup.services.impl;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import studio.camelcase.meetup.exceptions.ResourceDoesNotExistException;
import studio.camelcase.meetup.metrics.ApplicationMetrics;
import studio.camelcase.meetup.repositories.models.Person;
import studio.camelcase.meetup.repositories.models.PersonRepository;
import studio.camelcase.meetup.services.PersonService;

@Service
public class PersonServiceImpl implements PersonService {

    static final Logger log = LoggerFactory.getLogger(PersonServiceImpl.class);

    PersonRepository personRepository;
    ApplicationMetrics appMetrics;

    public PersonServiceImpl(
        PersonRepository personRepository,
        ApplicationMetrics appMetrics
    ) {
        this.personRepository = personRepository;
        this.appMetrics = appMetrics;
    }

    @Override
    @WithSpan
    @Async("jobExecutor")
    public CompletableFuture<Person> createUserAsync(Person person) {
        log.debug("Async create person {}, externalId -> {}", person, person.getExternalId());
        // Save to DB here
        var saved = personRepository.save(person);
        log.info("Async created person with id {}", saved.getExternalId());
        appMetrics.asyncInc();
        appMetrics.createdInc();
        return CompletableFuture.completedFuture(saved);
    }

    @Override
    public Person createUserSync(Person person) {
        log.debug("Sync create person {}, externalId -> {}", person, person.getExternalId());
        // Save to DB here
        var saved = personRepository.save(person);
        log.info("Sync created person with id {}", saved.getExternalId());
        appMetrics.syncInc();
        appMetrics.createdInc();
        return saved;
    }

    @Override
    @WithSpan
    public Person getUserByExternalId(UUID externalId) {
        log.debug("Retrieving person by ID {}", externalId);
        var retrieved = personRepository.getByExternalId(externalId)
            .orElseThrow(() -> new ResourceDoesNotExistException(externalId + " does not exist", null));

        log.info("Successfully retrieved from DB {}", retrieved);
        return retrieved;
    }
}
