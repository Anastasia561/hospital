package pl.edu.hospital.mapper;

import org.springframework.stereotype.Component;
import pl.edu.hospital.dto.PatientForAdminDto;
import pl.edu.hospital.entity.Patient;

@Component
public class PatientMapper {
    public static PatientForAdminDto toPatientForAdminDto(Patient patient) {
        PatientForAdminDto dto = new PatientForAdminDto();
        dto.setFirstName(patient.getFirstName());
        dto.setLastName(patient.getLastName());
        dto.setPhone(patient.getPhoneNumber());
        dto.setAddress(patient.getAddress());
        dto.setEmail(patient.getEmail());
        dto.setBirthDate(patient.getBirthDate());
        return dto;
    }

}
