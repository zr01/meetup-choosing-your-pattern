package studio.camelcase.meetup.repositories.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customers_id_gen")
    @SequenceGenerator(name = "customers_id_gen", sequenceName = "customers_id_seq", allocationSize = 1)
    Long id = null;

    UUID externalId = UUID.randomUUID();
    String firstName = "";
    String middleName = "";
    String lastName = "";
    char gender = 'X';
    LocalDate birthDate = LocalDate.now();
}
