package studio.camelcase.meetup.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import studio.camelcase.meetup.repositories.models.Person;

@Repository
public interface PersonRxRepository extends ReactiveCrudRepository<Person, Long> {

}
