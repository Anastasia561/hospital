package pl.edu.hospital.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.hospital.dto.patient.PatientForAdminDto;
import pl.edu.hospital.dto.patient.PatientForProfileDto;
import pl.edu.hospital.dto.patient.PatientForRecordDto;
import pl.edu.hospital.entity.Patient;
import pl.edu.hospital.exception.PatientNotFoundException;
import pl.edu.hospital.mapper.PatientMapper;
import pl.edu.hospital.repository.PatientRepository;

import java.util.List;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public PatientService(PatientRepository patientRepository, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

    public List<PatientForAdminDto> getAllForAdmin() {
        return patientRepository.findAll()
                .stream()
                .map(patientMapper::toPatientForAdminDto)
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
                .map(patientMapper::toPatientForAdminDto)
                .toList();
    }

    public PatientForRecordDto getPatientByAppointmentId(int appId) {
        Patient patient = patientRepository.findPatientByAppointmentId(appId)
                .orElseThrow(() -> new PatientNotFoundException(appId + ""));
        return patientMapper.toPatientForRecordDto(patient);
    }

    public PatientForProfileDto getPatientByUsername(String username) {
        Patient patient = patientRepository.findByUsername(username)
                .orElseThrow(() -> new PatientNotFoundException(username));
        return patientMapper.toPatientForProfileDto(patient);
    }

    @Transactional
    public void updatePatient(PatientForProfileDto dto) {
        Patient patient = patientRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new PatientNotFoundException(dto.getUsername()));

        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setEmail(dto.getEmail());
        patient.setLanguage(dto.getLanguage());
        patient.setBirthDate(dto.getBirthDate());
        patient.setPhoneNumber(dto.getPhoneNumber());

        patient.getAddress().setNumber(dto.getNumber());
        patient.getAddress().setStreet(dto.getStreet());
        patient.getAddress().getCity().getCountry().setName(dto.getCountry());
        patient.getAddress().getCity().setName(dto.getCity());

        patientRepository.save(patient);
    }

    public void registerPatient(PatientForProfileDto dto) {
        Patient patient = patientMapper.toPatient(dto);
        patientRepository.save(patient);
    }
}
