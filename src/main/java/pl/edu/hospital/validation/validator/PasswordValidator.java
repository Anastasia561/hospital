package pl.edu.hospital.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.edu.hospital.validation.annotation.ValidPassword;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return password.length() >= 5 &&
                password.chars().filter(Character::isLowerCase).count() >= 1 &&
                password.chars().filter(Character::isUpperCase).count() >= 1 &&
                password.chars().filter(Character::isDigit).count() >= 3;
    }
}
