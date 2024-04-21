package studio.camelcase.meetup.repositories.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> getByExternalId(UUID externalId);
}
