package pl.edu.hospital.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.hospital.entity.Person;
import pl.edu.hospital.repository.PersonRepository;
import pl.edu.hospital.service.PersonService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {
    @Mock
    private PersonRepository personRepository;
    @InjectMocks
    private PersonService personService;

    @Test
    void findByUsernameTest_shouldReturnEmptyOptionalWhenNotFound() {
        String username = "unknown";

        when(personRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<Person> result = personService.findByUsername(username);

        assertTrue(result.isEmpty());
        verify(personRepository).findByUsername(username);
    }
}
