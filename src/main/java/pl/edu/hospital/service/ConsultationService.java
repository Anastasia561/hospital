package pl.edu.hospital.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.hospital.dto.ConsultationDto;
import pl.edu.hospital.entity.Consultation;
import pl.edu.hospital.mapper.ConsultationMapper;
import pl.edu.hospital.repository.ConsultationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ConsultationService {
    private final ConsultationRepository consultationRepository;
    private final ConsultationMapper consultationMapper;

    public ConsultationService(ConsultationRepository consultationRepository, ConsultationMapper consultationMapper) {
        this.consultationRepository = consultationRepository;
        this.consultationMapper = consultationMapper;
    }

    public List<ConsultationDto> getAllByDoctorUsername(String username) {
        return consultationRepository.findAllByDoctorUsername(username)
                .stream()
                .map(consultationMapper::toConsultationDto)
                .toList();
    }

    @Transactional
    public void updateConsultation(ConsultationDto dto) {
        Optional<Consultation> optional = consultationRepository.findById(dto.getId());
        if (optional.isPresent()) {
            Consultation consultation = optional.get();
            consultation.setEndTime(dto.getEndTime());
            consultation.setStartTime(dto.getStartTime());
            consultationRepository.save(consultation);
        }
    }

    public void createConsultation(ConsultationDto dto) {
        Consultation consultation = consultationMapper.toConsultation(dto);
        consultationRepository.save(consultation);
    }

    public void deleteConsultation(int id) {
        consultationRepository.deleteById(id);
    }
}
