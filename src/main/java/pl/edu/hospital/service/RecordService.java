package pl.edu.hospital.service;

import org.springframework.stereotype.Service;
import pl.edu.hospital.dto.RecordForDoctorDto;
import pl.edu.hospital.entity.Patient;
import pl.edu.hospital.entity.Record;
import pl.edu.hospital.exception.PatientNotFoundException;
import pl.edu.hospital.exception.RecordNotFoundException;
import pl.edu.hospital.mapper.RecordMapper;
import pl.edu.hospital.repository.PatientRepository;
import pl.edu.hospital.repository.RecordRepository;

@Service
public class RecordService {
    private final RecordRepository recordRepository;
    private final PatientRepository patientRepository;

    public RecordService(RecordRepository recordRepository, PatientRepository patientRepository) {
        this.recordRepository = recordRepository;
        this.patientRepository = patientRepository;
    }

    public RecordForDoctorDto getRecordForDoctorByAppointmentId(int id) {
        Record record = recordRepository.findByAppointmentId(id)
                .orElseThrow(() -> new RecordNotFoundException(id));
        Patient patient = patientRepository.findPatientByAppointmentId(id)
                .orElseThrow(() -> new PatientNotFoundException(id + ""));

        return RecordMapper.toRecordDto(record, patient);
    }
}
