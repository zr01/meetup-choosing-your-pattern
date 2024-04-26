package studio.camelcase.meetup.controllers;

import io.micrometer.core.annotation.Timed;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import studio.camelcase.meetup.controllers.models.PersonRequest;
import studio.camelcase.meetup.controllers.models.PersonResponse;
import studio.camelcase.meetup.metrics.ApplicationMetrics;
import studio.camelcase.meetup.repositories.models.Person;
import studio.camelcase.meetup.services.PersonService;

@RestController
@RequestMapping("/person")
@Timed
public class PersonController {

    static final Logger log = LoggerFactory.getLogger(PersonController.class);

    private final PersonService personService;
    private final ApplicationMetrics appMetrics;

    public PersonController(
        PersonService personService,
        ApplicationMetrics appMetrics
    ) {
        this.personService = personService;
        this.appMetrics = appMetrics;
    }

    @PostMapping("/async")
    public PersonResponse postCreateUser(@RequestBody PersonRequest request) {
        log.debug("Received the request of async creating a user for {}", request);
        var dbRecord = toDb(request);
        personService.createUserAsync(dbRecord);
        log.info("Created person {} asynchronously", dbRecord);
        appMetrics.requestInc();
        return toResponse(dbRecord);
    }

    @PostMapping("/sync")
    public PersonResponse postCreateUserSync(@RequestBody PersonRequest request) {
        log.debug("Received the request of sync creating a user for {}", request);
        var dbRecord = toDb(request);
        personService.createUserSync(dbRecord);
        log.info("Created person {} synchronously", dbRecord);
        appMetrics.requestInc();
        return toResponse(dbRecord);
    }

    @GetMapping("/{externalId}")
    public PersonResponse getUserByExternalId(
        @PathVariable UUID externalId
    ) {
        log.debug("Getting user by external ID [{}]", externalId);
        var p = toResponse(
            personService.getUserByExternalId(externalId)
        );
        log.info("Successfully Retrieved user by eternal ID [{}]", p.id());
        return p;
    }

    @GetMapping("/random")
    @Timed(value = "random.person.generate", percentiles = {0.5, 0.9, 0.99}, extraTags = {"random.person.generate"})
    public PersonResponse getRandom() {
        var p = toResponse(personService.getUserByExternalId(UUID.randomUUID()));
        log.info("Random person generated {}", p);
        return p;
    }

    static Person toDb(PersonRequest from) {
        return Person.builder()
            .id(null)
            .externalId(UUID.randomUUID())
            .firstName(from.firstName())
            .middleName(from.middleName())
            .lastName(from.lastName())
            .gender(from.gender())
            .birthDate(from.birthDate())
            .build();
    }

    static PersonResponse toResponse(Person from) {
        return new PersonResponse(
            from.getExternalId(),
            from.getFirstName(),
            from.getMiddleName(),
            from.getLastName(),
            from.getGender(),
            from.getBirthDate()
        );
    }
}
