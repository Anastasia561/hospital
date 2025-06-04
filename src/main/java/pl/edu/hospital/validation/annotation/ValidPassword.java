package pl.edu.hospital.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import pl.edu.hospital.validation.validator.PasswordValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "Invalid password (must contain at least 1 uppercase, 1 lowercase, 3 digits)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
