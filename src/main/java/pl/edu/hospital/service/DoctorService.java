package pl.edu.hospital.service;

import org.springframework.stereotype.Service;
import pl.edu.hospital.dto.DoctorForAdminDto;
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
}
