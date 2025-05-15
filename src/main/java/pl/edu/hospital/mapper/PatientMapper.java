package pl.edu.hospital.mapper;

import org.springframework.stereotype.Component;
import pl.edu.hospital.dto.PatientForAdminDto;
import pl.edu.hospital.dto.PatientForRecordDto;
import pl.edu.hospital.entity.Patient;

@Component
public class PatientMapper {
    public static PatientForAdminDto toPatientForAdminDto(Patient patient) {
        PatientForAdminDto dto = new PatientForAdminDto();
        dto.setFullName(patient.getFirstName() + " " + patient.getLastName());
        dto.setPhone(patient.getPhoneNumber());
        dto.setAddress(patient.getAddress());
        dto.setEmail(patient.getEmail());
        dto.setUsername(patient.getUsername());
        dto.setBirthDate(patient.getBirthDate());
        return dto;
    }

    public static PatientForRecordDto toPatientForRecordDto(Patient patient) {
        PatientForRecordDto dto = new PatientForRecordDto();
        dto.setFullName(patient.getFirstName() + " " + patient.getLastName());
        dto.setPhoneNumber(patient.getPhoneNumber());
        dto.setEmail(patient.getEmail());
        return dto;
    }
}
