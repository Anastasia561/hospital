package pl.edu.hospital.service;

import org.springframework.stereotype.Service;
import pl.edu.hospital.dto.DoctorForAdminDto;
import pl.edu.hospital.entity.Doctor;
import pl.edu.hospital.entity.enums.Specialization;
import pl.edu.hospital.mapper.DoctorMapper;
import pl.edu.hospital.repository.DoctorRepository;

import java.util.List;
import java.util.Optional;

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

    public String getDoctorFullNameByUsername(String username) {
        Doctor d = doctorRepository.findByUsername(username);
        return d.getFirstName() + " " + d.getLastName();
    }

    public List<DoctorForAdminDto> getAllBySpecialization(Specialization specialization) {
        return doctorRepository.findAllBySpecialization(specialization)
                .stream()
                .map(DoctorMapper::toDoctorForAdminDto)
                .toList();
    }
}
