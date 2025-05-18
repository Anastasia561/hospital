package pl.edu.hospital.service;

import org.springframework.stereotype.Service;
import pl.edu.hospital.dto.DoctorForAdminDto;
import pl.edu.hospital.dto.DoctorForProfileDto;
import pl.edu.hospital.dto.DoctorRegistrationDto;
import pl.edu.hospital.entity.Doctor;
import pl.edu.hospital.entity.enums.Specialization;
import pl.edu.hospital.exception.DoctorNotFoundException;
import pl.edu.hospital.mapper.DoctorMapper;
import pl.edu.hospital.repository.DoctorRepository;

import java.util.List;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<DoctorForAdminDto> getAllForAdmin() {
        return doctorRepository.findAll()
                .stream()
                .map(DoctorMapper::toDoctorForAdminDto)
                .toList();
    }

    public String getDoctorFullNameByUsername(String username) throws DoctorNotFoundException {
        Doctor d = doctorRepository.findByUsername(username)
                .orElseThrow(() -> new DoctorNotFoundException(username));
        return d.getFirstName() + " " + d.getLastName();
    }

    public List<DoctorForAdminDto> getAllBySpecialization(Specialization specialization) {
        return doctorRepository.findAllBySpecialization(specialization)
                .stream()
                .map(DoctorMapper::toDoctorForAdminDto)
                .toList();
    }

    public void createDoctor(DoctorRegistrationDto dto) {
        Doctor doctor = DoctorMapper.toDoctor(dto);
        doctorRepository.save(doctor);
    }

    public DoctorForAdminDto findByUsernameForAdminDto(String username) {
        Doctor doctor = doctorRepository.findByUsername(username)
                .orElseThrow(() -> new DoctorNotFoundException(username));
        return DoctorMapper.toDoctorForAdminDto(doctor);
    }

    public DoctorForProfileDto findByUsernameForProfileDto(String username) {
        Doctor doctor = doctorRepository.findByUsername(username)
                .orElseThrow(() -> new DoctorNotFoundException(username));
        return DoctorMapper.toDoctorForProfileDto(doctor);
    }

    public void updateDoctor(DoctorForProfileDto dto) {
        Doctor doctor = doctorRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new DoctorNotFoundException(dto.getUsername()));

        doctor.setFirstName(dto.getFirstName());
        doctor.setLanguage(dto.getLanguage());
        doctor.setLastName(dto.getLastName());
        doctor.setEmail(dto.getEmail());

        doctorRepository.save(doctor);
    }
}
