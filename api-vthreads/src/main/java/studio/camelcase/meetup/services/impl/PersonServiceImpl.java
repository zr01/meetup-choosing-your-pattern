package studio.camelcase.meetup.services.impl;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import studio.camelcase.meetup.repositories.models.Person;
import studio.camelcase.meetup.services.PersonService;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class PersonServiceImpl implements PersonService {

    static final Logger log = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Override
    @WithSpan
    public Person createUser(Person person) {
        log.debug("Create person {}, id -> {}", person, person.id());
        // Save to DB here
        return person;
    }

    @Override
    @WithSpan
    public Person getUserById(UUID id) {
        log.debug("Retrieving person by ID {}", id);
        return new Person(
            id,
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            'X',
            LocalDate.now().minusDays(365)
        );
    }
}
