package pl.edu.hospital.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import pl.edu.hospital.entity.enums.Language;
import pl.edu.hospital.validation.OnCreate;
import pl.edu.hospital.validation.annotation.UniqueUsername;
import pl.edu.hospital.validation.annotation.ValidPassword;

public abstract class PersonForProfileDto {
    @NotEmpty(groups = OnCreate.class)
    @Size(min = 2, max = 50, groups = OnCreate.class)
    @UniqueUsername(groups = OnCreate.class)
    protected String username;
    @ValidPassword(groups = OnCreate.class)
    protected String password;
    @NotEmpty
    @Size(min = 3, max = 50)
    protected String firstName;
    @NotEmpty
    @Size(min = 3, max = 50)
    protected String lastName;
    @NotEmpty
    @Email(message = "{pl.edu.hospital.failure.invalidEmail}")
    protected String email;
    protected Language language;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }
}
