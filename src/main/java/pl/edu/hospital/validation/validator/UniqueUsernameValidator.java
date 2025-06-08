package pl.edu.hospital.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.edu.hospital.repository.PersonRepository;
import pl.edu.hospital.validation.annotation.UniqueUsername;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {
    private final PersonRepository personRepository;

    public UniqueUsernameValidator(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return !personRepository.existsByUsername(username);
    }
}
