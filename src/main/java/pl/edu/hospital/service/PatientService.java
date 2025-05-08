package pl.edu.hospital.service;

import org.springframework.stereotype.Service;
import pl.edu.hospital.dto.PatientForAdminDto;
import pl.edu.hospital.mapper.PatientMapper;
import pl.edu.hospital.repository.PatientRepository;

import java.util.List;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientForAdminDto> getAllForAdmin() {
        return patientRepository.findAll()
                .stream()
                .map(PatientMapper::toPatientForAdminDto)
                .toList();
    }
}
