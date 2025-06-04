package pl.edu.hospital.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.edu.hospital.validation.annotation.ValidPhone;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {
    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }
        return phoneNumber.matches("^\\+?[0-9]{9,15}$");
    }
}