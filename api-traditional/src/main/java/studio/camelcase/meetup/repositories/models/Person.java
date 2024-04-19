package studio.camelcase.meetup.repositories.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "customers")
public record Person(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id,

    UUID externalId,
    String firstName,
    String middleName,
    String lastName,
    char gender,
    LocalDate birthDate
) {

}
