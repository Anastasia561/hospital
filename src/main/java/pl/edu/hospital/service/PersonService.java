package pl.edu.hospital.service;

import org.springframework.stereotype.Service;
import pl.edu.hospital.entity.Person;
import pl.edu.hospital.repository.PersonRepository;

import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Optional<Person> findByUsername(String username) {
        return personRepository.findByUsername(username);
    }
}
