package studio.camelcase.meetup.controllers.models;

import java.time.LocalDate;
import java.util.UUID;

public record PersonResponse(
    UUID id,
    String firstName,
    String middleName,
    String lastName,
    char gender,
    LocalDate birthDate
) {

}
