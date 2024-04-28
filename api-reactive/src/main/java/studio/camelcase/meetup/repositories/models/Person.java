package studio.camelcase.meetup.repositories.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDate;
import java.util.UUID;

@Table("customers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    Long id = null;

    UUID externalId = UUID.randomUUID();
    String firstName = "";
    String middleName = "";
    String lastName = "";
    char gender = 'X';
    LocalDate birthDate = LocalDate.now();
}
