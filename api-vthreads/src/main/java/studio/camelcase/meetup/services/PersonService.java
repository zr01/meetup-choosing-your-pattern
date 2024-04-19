package studio.camelcase.meetup.services;

import java.util.UUID;
import studio.camelcase.meetup.repositories.models.Person;

public interface PersonService {

    Person createUser(Person person);

    Person getUserById(UUID id);
}
