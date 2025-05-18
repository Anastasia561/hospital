package pl.edu.hospital.service;

import org.springframework.stereotype.Service;
import pl.edu.hospital.dto.record.RecordCreationRequestDto;
import pl.edu.hospital.dto.record.RecordForDoctorDto;
import pl.edu.hospital.dto.record.RecordForPatientDto;
import pl.edu.hospital.entity.Appointment;
import pl.edu.hospital.entity.Doctor;
import pl.edu.hospital.entity.Patient;
import pl.edu.hospital.entity.Prescription;
import pl.edu.hospital.entity.Record;
import pl.edu.hospital.exception.AppointmentNotFoundException;
import pl.edu.hospital.exception.DoctorNotFoundException;
import pl.edu.hospital.exception.PatientNotFoundException;
import pl.edu.hospital.exception.RecordNotFoundException;
import pl.edu.hospital.mapper.PrescriptionMapper;
import pl.edu.hospital.mapper.RecordMapper;
import pl.edu.hospital.repository.AppointmentRepository;
import pl.edu.hospital.repository.DoctorRepository;
import pl.edu.hospital.repository.PatientRepository;
import pl.edu.hospital.repository.RecordRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecordService {
    private final RecordRepository recordRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;

    public RecordService(RecordRepository recordRepository, PatientRepository patientRepository,
                         AppointmentRepository appointmentRepository, DoctorRepository doctorRepository) {
        this.recordRepository = recordRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
    }

    public RecordForDoctorDto getRecordForDoctorByAppointmentId(int id) {
        Record record = recordRepository.findByAppointmentId(id)
                .orElseThrow(() -> new RecordNotFoundException(id));
        Patient patient = patientRepository.findPatientByAppointmentId(id)
                .orElseThrow(() -> new PatientNotFoundException(id + ""));

        return RecordMapper.toRecordForDoctorDto(record, patient);
    }

    public RecordForPatientDto getRecordForPatientByAppointmentId(int id) {
        Record record = recordRepository.findByAppointmentId(id)
                .orElseThrow(() -> new RecordNotFoundException(id));
        Doctor doctor = doctorRepository.findDoctorByAppointmentId(id)
                .orElseThrow(() -> new DoctorNotFoundException(id + ""));

        return RecordMapper.toRecordForPatientDto(record, doctor);
    }

    public void saveRecord(RecordCreationRequestDto dto) {
        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new AppointmentNotFoundException(dto.getAppointmentId()));

        Record record = RecordMapper.toRecord(dto, appointment);

        List<Prescription> prescriptions = dto.getPrescriptions().stream()
                .map(PrescriptionMapper::toPrescription)
                .peek(p -> p.setRecord(record))
                .collect(Collectors.toCollection(ArrayList::new));

        record.setPrescription(prescriptions);
        recordRepository.save(record);
    }
}
