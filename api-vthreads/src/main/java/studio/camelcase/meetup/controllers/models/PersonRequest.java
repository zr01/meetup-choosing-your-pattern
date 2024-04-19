package studio.camelcase.meetup.controllers.models;

import java.time.LocalDate;

public record PersonRequest(
    String firstName,
    String middleName,
    String lastName,
    char gender,
    LocalDate birthDate
) {

}

