package studio.camelcase.meetup.controllers;

import io.micrometer.core.annotation.Timed;
import java.time.LocalDate;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import studio.camelcase.meetup.controllers.models.PersonRequest;
import studio.camelcase.meetup.controllers.models.PersonResponse;
import studio.camelcase.meetup.repositories.models.Person;
import studio.camelcase.meetup.services.PersonService;

@RestController
@RequestMapping("/person")
@Timed
public class PersonController {

    static final Logger log = LoggerFactory.getLogger(PersonController.class);

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public PersonResponse postCreateUser(@RequestBody PersonRequest request) {
        log.debug("Received the request of creating a user for {}", request);
        var newPerson = personService.createUser(toDb(request));
        log.info("Created person {}", newPerson);
        return toResponse(newPerson);
    }

    @GetMapping("/random")
    @Timed(value = "random.person.generate", percentiles = {0.5, 0.9, 0.99}, extraTags = {"random.person.generate"})
    public PersonResponse getRandom() {
        var p = toResponse(personService.getUserById(UUID.randomUUID()));
        log.info("Random person generated {}", p);
        return p;
    }

    static Person toDb(PersonRequest from) {
        return new Person(
            UUID.randomUUID(),
            from.firstName(),
            from.middleName(),
            from.lastName(),
            from.gender(),
            from.birthDate()
        );
    }

    static PersonResponse toResponse(Person from) {
        return new PersonResponse(
            from.id(),
            from.firstName(),
            from.middleName(),
            from.lastName(),
            from.gender(),
            from.birthDate()
        );
    }
}
