package pl.edu.hospital.mapper;

import pl.edu.hospital.dto.ConsultationDto;
import pl.edu.hospital.entity.Consultation;
import pl.edu.hospital.entity.Doctor;

public class ConsultationMapper {
    public static ConsultationDto toConsultationDto(Consultation consultation) {
        ConsultationDto dto = new ConsultationDto();
        dto.setDoctorUsername(consultation.getDoctor().getUsername());
        dto.setId(consultation.getId());
        dto.setDay(consultation.getWorkingDay());
        dto.setEndTime(consultation.getEndTime());
        dto.setStartTime(consultation.getStartTime());
        return dto;
    }

    public static Consultation toConsultation(ConsultationDto dto, Doctor doctor) {
        Consultation consultation = new Consultation();
        consultation.setWorkingDay(dto.getDay());
        consultation.setDoctor(doctor);
        consultation.setStartTime(dto.getStartTime());
        consultation.setEndTime(dto.getEndTime());
        return consultation;
    }
}
