package pl.edu.hospital.mapper;

import pl.edu.hospital.dto.record.RecordCreationRequestDto;
import pl.edu.hospital.dto.record.RecordForDoctorDto;
import pl.edu.hospital.dto.record.RecordForPatientDto;
import pl.edu.hospital.entity.Appointment;
import pl.edu.hospital.entity.Doctor;
import pl.edu.hospital.entity.Patient;
import pl.edu.hospital.entity.Record;

public class RecordMapper {
    public static RecordForDoctorDto toRecordForDoctorDto(Record record, Patient patient) {
        RecordForDoctorDto dto = new RecordForDoctorDto();
        dto.setPatientFullName(patient.getFirstName() + " " + patient.getLastName());
        dto.setPatientPhoneNumber(patient.getPhoneNumber());
        dto.setPatientEmail(patient.getEmail());
        dto.setPrescriptions(record.getPrescription()
                .stream()
                .map(PrescriptionMapper::toPrescriptionDto)
                .toList());
        dto.setAppointmentDate(record.getAppointment().getDate());
        dto.setSummary(record.getSummary());
        dto.setDiagnosis(record.getDiagnosis());
        return dto;
    }

    public static RecordForPatientDto toRecordForPatientDto(Record record, Doctor doctor) {
        RecordForPatientDto dto = new RecordForPatientDto();
        dto.setDoctorFullName(doctor.getFirstName() + " " + doctor.getLastName());
        dto.setSpecialization(doctor.getSpecialization());
        dto.setPrescriptions(record.getPrescription()
                .stream()
                .map(PrescriptionMapper::toPrescriptionDto)
                .toList());
        dto.setAppointmentDate(record.getAppointment().getDate());
        dto.setSummary(record.getSummary());
        dto.setDiagnosis(record.getDiagnosis());
        return dto;
    }

    public static Record toRecord(RecordCreationRequestDto dto, Appointment appointment) {
        Record record = new Record();
        record.setDiagnosis(dto.getDiagnosis());
        record.setSummary(dto.getSummary());
        record.setAppointment(appointment);
        return record;
    }
}
