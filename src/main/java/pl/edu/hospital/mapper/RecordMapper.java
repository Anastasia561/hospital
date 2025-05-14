package pl.edu.hospital.mapper;

import pl.edu.hospital.dto.RecordForDoctorDto;
import pl.edu.hospital.entity.Patient;
import pl.edu.hospital.entity.Record;

public class RecordMapper {
    public static RecordForDoctorDto toRecordDto(Record record, Patient patient) {
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
}
