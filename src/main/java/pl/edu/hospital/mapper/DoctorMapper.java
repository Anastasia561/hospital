package pl.edu.hospital.mapper;

import org.springframework.stereotype.Component;
import pl.edu.hospital.dto.DoctorForAdminDto;
import pl.edu.hospital.entity.Doctor;

@Component
public class DoctorMapper {
    public static DoctorForAdminDto toDoctorForAdminDto(Doctor doctor) {
        DoctorForAdminDto dto = new DoctorForAdminDto();
        dto.setFirstName(doctor.getFirstName());
        dto.setLastName(doctor.getLastName());
        dto.setEmail(doctor.getEmail());
        dto.setExperience(doctor.getExperience());
        dto.setEmploymentDate(doctor.getEmploymentDate());
        dto.setSpecialization(doctor.getSpecialization());
        return dto;
    }
}
