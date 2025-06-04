package pl.edu.hospital.dto.doctor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import pl.edu.hospital.entity.enums.Language;
import pl.edu.hospital.entity.enums.Specialization;
import pl.edu.hospital.validation.annotation.UniqueUsername;
import pl.edu.hospital.validation.annotation.ValidPassword;

public class DoctorRegistrationDto {
    @NotEmpty
    @Size(min = 2, max = 50)
    @UniqueUsername
    private String username;
    @NotEmpty
    @Size(min = 3, max = 50)
    private String firstName;
    @NotEmpty
    @Size(min = 3, max = 50)
    private String lastName;
    @ValidPassword
    private String password;
    @Email(message = "Email must be a valid email address")
    private String email;
    private Language language;
    @Positive
    private int experience;
    private Specialization specialization;

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

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }
}
