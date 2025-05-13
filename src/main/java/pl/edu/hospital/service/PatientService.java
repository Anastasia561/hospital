package pl.edu.hospital.service;

import org.springframework.stereotype.Service;
import pl.edu.hospital.dto.PatientForAdminDto;
import pl.edu.hospital.dto.PatientForScheduleDto;
import pl.edu.hospital.entity.Patient;
import pl.edu.hospital.mapper.PatientMapper;
import pl.edu.hospital.repository.PatientRepository;

import java.util.List;
import java.util.Optional;

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

    public String getPatientFullNameByUsername(String username) {
        Patient p = patientRepository.findByUsername(username);
        return p.getFirstName() + " " + p.getLastName();
    }

    public List<PatientForAdminDto> getAllForAdminByEmail(String email) {
        return patientRepository.getPatientsByEmail(email)
                .stream()
                .map(PatientMapper::toPatientForAdminDto)
                .toList();
    }

}
