package pl.edu.hospital.service;

import org.springframework.stereotype.Service;
import pl.edu.hospital.dto.ConsultationDto;
import pl.edu.hospital.entity.Consultation;
import pl.edu.hospital.entity.Doctor;
import pl.edu.hospital.exception.DoctorNotFoundException;
import pl.edu.hospital.mapper.ConsultationMapper;
import pl.edu.hospital.repository.ConsultationRepository;
import pl.edu.hospital.repository.DoctorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ConsultationService {
    private final ConsultationRepository consultationRepository;
    private final DoctorRepository doctorRepository;

    public ConsultationService(ConsultationRepository consultationRepository, DoctorRepository doctorRepository) {
        this.consultationRepository = consultationRepository;
        this.doctorRepository = doctorRepository;
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
            consultation.setEndTime(dto.getEndTime());
            consultation.setStartTime(dto.getStartTime());
            consultationRepository.save(consultation);
        }
    }

    public void createConsultation(ConsultationDto dto) throws DoctorNotFoundException {
        Doctor doctor = doctorRepository.findByUsername(dto.getDoctorUsername())
                .orElseThrow(() -> new DoctorNotFoundException(dto.getDoctorUsername()));
        Consultation consultation = ConsultationMapper.toConsultation(dto, doctor);
        consultationRepository.save(consultation);
    }

    public void deleteConsultation(int id) {
        consultationRepository.deleteById(id);
    }
}
