package pl.edu.hospital.service;

import org.springframework.stereotype.Service;
import pl.edu.hospital.dto.ConsultationDto;
import pl.edu.hospital.entity.Consultation;
import pl.edu.hospital.mapper.ConsultationMapper;
import pl.edu.hospital.repository.ConsultationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ConsultationService {
    private final ConsultationRepository consultationRepository;

    public ConsultationService(ConsultationRepository consultationRepository) {
        this.consultationRepository = consultationRepository;
    }

    public List<ConsultationDto> getAllByDoctorUsername(String username) {
        return consultationRepository.findAllByDoctorUsername(username)
                .stream()
                .map(ConsultationMapper::toConsultationDto)
                .toList();
    }

    public void updateConsultation(ConsultationDto dto) {
        Optional<Consultation> optional = consultationRepository.findById(dto.getId());
        if (optional.isPresent()) {
            Consultation consultation = optional.get();
            System.out.println(consultation.getId());
            consultation.setEndTime(dto.getEndTime());
            consultation.setStartTime(dto.getStartTime());
            consultationRepository.saveAndFlush(consultation);
        }
    }
}
