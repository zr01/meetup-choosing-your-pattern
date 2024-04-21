package studio.camelcase.meetup.services;

import java.util.UUID;
import studio.camelcase.meetup.repositories.models.Person;

public interface PersonService {

    Person createUser(Person person);

    Person getUserByExternalId(UUID externalId) throws RuntimeException;
}
