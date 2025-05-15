package pl.edu.hospital.service;

import org.springframework.stereotype.Service;
import pl.edu.hospital.dto.PatientForAdminDto;
import pl.edu.hospital.dto.PatientForRecordDto;
import pl.edu.hospital.entity.Patient;
import pl.edu.hospital.exception.PatientNotFoundException;
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

    public String getPatientFullNameByUsername(String username) throws PatientNotFoundException {
        Patient p = patientRepository.findByUsername(username)
                .orElseThrow(() -> new PatientNotFoundException(username));
        return p.getFirstName() + " " + p.getLastName();
    }

    public List<PatientForAdminDto> getAllForAdminByEmail(String email) {
        return patientRepository.getPatientsByEmail(email)
                .stream()
                .map(PatientMapper::toPatientForAdminDto)
                .toList();
    }

    public PatientForRecordDto getPatientByAppointmentId(int appId) {
        Patient patient = patientRepository.findPatientByAppointmentId(appId)
                .orElseThrow(() -> new PatientNotFoundException(appId + ""));
        return PatientMapper.toPatientForRecordDto(patient);
    }
}
