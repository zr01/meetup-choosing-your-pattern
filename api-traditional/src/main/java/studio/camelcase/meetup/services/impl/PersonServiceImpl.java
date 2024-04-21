package studio.camelcase.meetup.services.impl;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import studio.camelcase.meetup.exceptions.ResourceDoesNotExistException;
import studio.camelcase.meetup.repositories.models.Person;
import studio.camelcase.meetup.repositories.models.PersonRepository;
import studio.camelcase.meetup.services.PersonService;

@Service
public class PersonServiceImpl implements PersonService {

    static final Logger log = LoggerFactory.getLogger(PersonServiceImpl.class);

    PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    @WithSpan
    public Person createUser(Person person) {
        log.debug("Create person {}, externalId -> {}", person, person.getExternalId());
        // Save to DB here
        var saved = personRepository.save(person);
        log.info("Created person with id {}", saved.getExternalId());
        return person;
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
