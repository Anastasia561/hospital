package pl.edu.hospital.dto.patient;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import pl.edu.hospital.dto.PersonForProfileDto;
import pl.edu.hospital.entity.enums.Language;
import pl.edu.hospital.validation.annotation.Adult;
import pl.edu.hospital.validation.annotation.ValidPhone;

import java.time.LocalDate;

public class PatientForProfileDto extends PersonForProfileDto {
    @Past
    @Adult
    private LocalDate birthDate;
    @ValidPhone
    private String phoneNumber;
    @NotEmpty
    @Size(min = 3, max = 50)
    private String country;
    @NotEmpty
    @Size(min = 3, max = 50)
    private String city;
    @NotEmpty
    @Size(min = 3, max = 50)
    private String street;
    @Positive
    private int number;

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
