package pl.edu.hospital.mapper;

import org.springframework.stereotype.Component;
import pl.edu.hospital.dto.doctor.DoctorForAdminDto;
import pl.edu.hospital.dto.doctor.DoctorForProfileDto;
import pl.edu.hospital.dto.doctor.DoctorRegistrationDto;
import pl.edu.hospital.entity.Doctor;
import pl.edu.hospital.entity.enums.Role;

import java.time.LocalDate;

@Component
public class DoctorMapper {
    public DoctorForAdminDto toDoctorForAdminDto(Doctor doctor) {
        DoctorForAdminDto dto = new DoctorForAdminDto();
        dto.setUsername(doctor.getUsername());
        dto.setFullName(doctor.getFirstName() + " " + doctor.getLastName());
        dto.setEmail(doctor.getEmail());
        dto.setExperience(doctor.getExperience());
        dto.setEmploymentDate(doctor.getEmploymentDate());
        dto.setSpecialization(doctor.getSpecialization());
        return dto;
    }

    public Doctor toDoctor(DoctorRegistrationDto dto) {
        Doctor doctor = new Doctor();
        doctor.setFirstName(dto.getFirstName());
        doctor.setLastName(dto.getLastName());
        doctor.setEmail(dto.getEmail());
        doctor.setUsername(dto.getUsername());
        doctor.setLanguage(dto.getLanguage());
        doctor.setExperience(dto.getExperience());
        doctor.setEmploymentDate(LocalDate.now());
        doctor.setSpecialization(dto.getSpecialization());
        doctor.setPassword(dto.getPassword());
        doctor.setRole(Role.DOCTOR);
        return doctor;
    }

    public DoctorForProfileDto toDoctorForProfileDto(Doctor doctor) {
        DoctorForProfileDto dto = new DoctorForProfileDto();
        dto.setUsername(doctor.getUsername());
        dto.setEmail(doctor.getEmail());
        dto.setLanguage(doctor.getLanguage());
        dto.setSpecialization(doctor.getSpecialization());
        dto.setFirstName(doctor.getFirstName());
        dto.setLastName(doctor.getLastName());
        dto.setEmploymentDate(doctor.getEmploymentDate());
        return dto;
    }
}
