package pl.edu.hospital.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.hospital.dto.doctor.DoctorForAdminDto;
import pl.edu.hospital.dto.doctor.DoctorForProfileDto;
import pl.edu.hospital.dto.doctor.DoctorRegistrationDto;
import pl.edu.hospital.entity.Doctor;
import pl.edu.hospital.entity.enums.Specialization;
import pl.edu.hospital.exception.DoctorNotFoundException;
import pl.edu.hospital.mapper.DoctorMapper;
import pl.edu.hospital.repository.DoctorRepository;

import java.util.List;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    public DoctorService(DoctorRepository doctorRepository, DoctorMapper doctorMapper) {
        this.doctorRepository = doctorRepository;
        this.doctorMapper = doctorMapper;
    }

    public List<DoctorForAdminDto> getAllForAdmin() {
        return doctorRepository.findAll()
                .stream()
                .map(doctorMapper::toDoctorForAdminDto)
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
                .map(doctorMapper::toDoctorForAdminDto)
                .toList();
    }

    public void createDoctor(DoctorRegistrationDto dto) {
        Doctor doctor = doctorMapper.toDoctor(dto);
        doctorRepository.save(doctor);
    }

    public DoctorForAdminDto findByUsernameForAdminDto(String username) {
        Doctor doctor = doctorRepository.findByUsername(username)
                .orElseThrow(() -> new DoctorNotFoundException(username));
        return doctorMapper.toDoctorForAdminDto(doctor);
    }

    public DoctorForProfileDto findByUsernameForProfileDto(String username) {
        Doctor doctor = doctorRepository.findByUsername(username)
                .orElseThrow(() -> new DoctorNotFoundException(username));
        return doctorMapper.toDoctorForProfileDto(doctor);
    }

    @Transactional
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
