package pl.edu.hospital.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.hospital.entity.Person;

import java.util.Optional;

@Repository
public interface PersonRepository extends CrudRepository<Person, Integer> {
    Optional<Person> findByUsername(String username);

    boolean existsByUsername(String username);
}
