package pl.edu.hospital.mapper;

import org.springframework.stereotype.Component;
import pl.edu.hospital.dto.patient.PatientForAdminDto;
import pl.edu.hospital.dto.patient.PatientForProfileDto;
import pl.edu.hospital.dto.patient.PatientForRecordDto;
import pl.edu.hospital.entity.Patient;

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

    public static PatientForProfileDto toPatientForProfileDto(Patient patient) {
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
}
