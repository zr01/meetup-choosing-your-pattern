package studio.camelcase.meetup.controllers;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import studio.camelcase.meetup.controllers.models.PersonRequest;
import studio.camelcase.meetup.controllers.models.PersonResponse;
import studio.camelcase.meetup.metrics.ApplicationMetrics;
import studio.camelcase.meetup.repositories.PersonRxRepository;
import studio.camelcase.meetup.repositories.models.Person;

@RestController
@RequestMapping("/person")
@Slf4j
public class PersonController {

    final PersonRxRepository personRepository;
    final ApplicationMetrics appMetrics;

    public PersonController(PersonRxRepository personRepository, ApplicationMetrics appMetrics) {
        this.personRepository = personRepository;
        this.appMetrics = appMetrics;
    }

    @PostMapping("/sync")
    Mono<PersonResponse> postCreateUser(
        @RequestBody PersonRequest request
    ) {
        appMetrics.requestInc();
        log.debug("Received the request of rx creating a user for {}", request);
        var dbRecord = toDb(request);
        return personRepository
            .save(dbRecord)
            .map(p -> {
                appMetrics.rxInc();
                log.info("Created person {} rx", dbRecord);
                return toResponse(p);
            })
            ;
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
