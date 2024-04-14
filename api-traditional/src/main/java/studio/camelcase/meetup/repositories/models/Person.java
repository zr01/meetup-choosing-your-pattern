package studio.camelcase.meetup.repositories.models;

import java.time.LocalDate;
import java.util.UUID;

public record Person(
    UUID id,
    String firstName,
    String middleName,
    String lastName,
    char gender,
    LocalDate birthDate
) {

}
