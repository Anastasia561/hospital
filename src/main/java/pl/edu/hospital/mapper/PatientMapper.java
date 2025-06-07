package pl.edu.hospital.mapper;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Component;
import pl.edu.hospital.dto.patient.PatientForAdminDto;
import pl.edu.hospital.dto.patient.PatientForProfileDto;
import pl.edu.hospital.dto.patient.PatientForRecordDto;
import pl.edu.hospital.entity.Address;
import pl.edu.hospital.entity.City;
import pl.edu.hospital.entity.Country;
import pl.edu.hospital.entity.Patient;
import pl.edu.hospital.entity.enums.Role;

@Component
public class PatientMapper {
    public PatientForAdminDto toPatientForAdminDto(Patient patient) {
        PatientForAdminDto dto = new PatientForAdminDto();
        dto.setFullName(patient.getFirstName() + " " + patient.getLastName());
        dto.setPhone(patient.getPhoneNumber());
        dto.setAddress(patient.getAddress());
        dto.setEmail(patient.getEmail());
        dto.setUsername(patient.getUsername());
        dto.setBirthDate(patient.getBirthDate());
        return dto;
    }

    public PatientForRecordDto toPatientForRecordDto(Patient patient) {
        PatientForRecordDto dto = new PatientForRecordDto();
        dto.setFullName(patient.getFirstName() + " " + patient.getLastName());
        dto.setPhoneNumber(patient.getPhoneNumber());
        dto.setEmail(patient.getEmail());
        return dto;
    }

    public PatientForProfileDto toPatientForProfileDto(Patient patient) {
        PatientForProfileDto dto = new PatientForProfileDto();
        dto.setUsername(patient.getUsername());
        dto.setFirstName(patient.getFirstName());
        dto.setLastName(patient.getLastName());
        dto.setEmail(patient.getEmail());
        dto.setCity(patient.getAddress().getCity().getName());
        dto.setLanguage(patient.getLanguage());
        dto.setBirthDate(patient.getBirthDate());
        dto.setCountry(patient.getAddress().getCity().getCountry().getName());
        dto.setNumber(patient.getAddress().getNumber());
        dto.setStreet(patient.getAddress().getStreet());
        dto.setPhoneNumber(patient.getPhoneNumber());
        return dto;
    }

    public Patient toPatient(PatientForProfileDto dto) {
        Patient patient = new Patient();
        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setLanguage(dto.getLanguage());
        patient.setEmail(dto.getEmail());

        Country country = new Country();
        country.setName(dto.getCountry());

        City city = new City();
        city.setName(dto.getCity());
        city.setCountry(country);

        Address address = new Address();
        address.setNumber(dto.getNumber());
        address.setCity(city);
        address.setStreet(dto.getStreet());

        patient.setAddress(address);
        patient.setPhoneNumber(dto.getPhoneNumber());
        patient.setBirthDate(dto.getBirthDate());
        patient.setUsername(dto.getUsername());
        patient.setRole(Role.PATIENT);
        patient.setPassword(PasswordEncoderFactories
                .createDelegatingPasswordEncoder().encode(dto.getPassword()));
        return patient;
    }
}
