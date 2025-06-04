package pl.edu.hospital.mapper;

import org.springframework.stereotype.Component;
import pl.edu.hospital.dto.ConsultationDto;
import pl.edu.hospital.entity.Consultation;
import pl.edu.hospital.entity.Doctor;
import pl.edu.hospital.exception.DoctorNotFoundException;
import pl.edu.hospital.repository.DoctorRepository;

@Component
public class ConsultationMapper {
    private final DoctorRepository doctorRepository;

    public ConsultationMapper(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public ConsultationDto toConsultationDto(Consultation consultation) {
        ConsultationDto dto = new ConsultationDto();
        dto.setDoctorUsername(consultation.getDoctor().getUsername());
        dto.setId(consultation.getId());
        dto.setDay(consultation.getWorkingDay());
        dto.setEndTime(consultation.getEndTime());
        dto.setStartTime(consultation.getStartTime());
        return dto;
    }

    public Consultation toConsultation(ConsultationDto dto) {
        Consultation consultation = new Consultation();
        consultation.setWorkingDay(dto.getDay());

        Doctor doctor = doctorRepository.findByUsername(dto.getDoctorUsername())
                .orElseThrow(() -> new DoctorNotFoundException(dto.getDoctorUsername()));
        consultation.setDoctor(doctor);
        consultation.setStartTime(dto.getStartTime());
        consultation.setEndTime(dto.getEndTime());
        return consultation;
    }
}
