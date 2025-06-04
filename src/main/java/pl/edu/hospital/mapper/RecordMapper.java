package pl.edu.hospital.mapper;

import org.springframework.stereotype.Component;
import pl.edu.hospital.dto.record.RecordCreationRequestDto;
import pl.edu.hospital.dto.record.RecordForDoctorDto;
import pl.edu.hospital.dto.record.RecordForPatientDto;
import pl.edu.hospital.entity.Appointment;
import pl.edu.hospital.entity.Doctor;
import pl.edu.hospital.entity.Patient;
import pl.edu.hospital.entity.Record;
import pl.edu.hospital.exception.AppointmentNotFoundException;
import pl.edu.hospital.exception.DoctorNotFoundException;
import pl.edu.hospital.exception.PatientNotFoundException;
import pl.edu.hospital.repository.AppointmentRepository;
import pl.edu.hospital.repository.DoctorRepository;
import pl.edu.hospital.repository.PatientRepository;

@Component
public class RecordMapper {
    private final PrescriptionMapper prescriptionMapper;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    public RecordMapper(PrescriptionMapper prescriptionMapper, PatientRepository patientRepository,
                        DoctorRepository doctorRepository, AppointmentRepository appointmentRepository) {
        this.prescriptionMapper = prescriptionMapper;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public RecordForDoctorDto toRecordForDoctorDto(Record record) {
        RecordForDoctorDto dto = new RecordForDoctorDto();

        Integer appId = record.getAppointment().getId();
        Patient patient = patientRepository.findPatientByAppointmentId(appId)
                .orElseThrow(() -> new PatientNotFoundException(appId + ""));
        dto.setPatientFullName(patient.getFirstName() + " " + patient.getLastName());
        dto.setPatientPhoneNumber(patient.getPhoneNumber());
        dto.setPatientEmail(patient.getEmail());
        dto.setPrescriptions(record.getPrescription()
                .stream()
                .map(prescriptionMapper::toPrescriptionDto)
                .toList());
        dto.setAppointmentDate(record.getAppointment().getDate());
        dto.setSummary(record.getSummary());
        dto.setDiagnosis(record.getDiagnosis());
        return dto;
    }

    public RecordForPatientDto toRecordForPatientDto(Record record) {
        RecordForPatientDto dto = new RecordForPatientDto();

        Integer appId = record.getAppointment().getId();
        Doctor doctor = doctorRepository.findDoctorByAppointmentId(appId)
                .orElseThrow(() -> new DoctorNotFoundException(appId + ""));
        dto.setDoctorFullName(doctor.getFirstName() + " " + doctor.getLastName());
        dto.setSpecialization(doctor.getSpecialization());
        dto.setPrescriptions(record.getPrescription()
                .stream()
                .map(prescriptionMapper::toPrescriptionDto)
                .toList());
        dto.setAppointmentDate(record.getAppointment().getDate());
        dto.setSummary(record.getSummary());
        dto.setDiagnosis(record.getDiagnosis());
        return dto;
    }

    public Record toRecord(RecordCreationRequestDto dto) {
        Record record = new Record();
        record.setDiagnosis(dto.getDiagnosis());
        record.setSummary(dto.getSummary());
        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new AppointmentNotFoundException(dto.getAppointmentId()));
        record.setAppointment(appointment);
        return record;
    }
}
