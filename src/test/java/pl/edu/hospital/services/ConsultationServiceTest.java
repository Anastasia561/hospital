package pl.edu.hospital.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.hospital.dto.ConsultationDto;
import pl.edu.hospital.entity.Consultation;
import pl.edu.hospital.mapper.ConsultationMapper;
import pl.edu.hospital.repository.ConsultationRepository;
import pl.edu.hospital.service.ConsultationService;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConsultationServiceTest {
    @Mock
    private ConsultationRepository consultationRepository;
    @Mock
    private ConsultationMapper consultationMapper;
    @InjectMocks
    private ConsultationService consultationService;

    @Test
    void getAllByDoctorUsernameTest_shouldReturnNonEmptyList() {
        String username = "doctor1";
        Consultation consultation1 = new Consultation();
        Consultation consultation2 = new Consultation();
        ConsultationDto dto1 = new ConsultationDto();
        ConsultationDto dto2 = new ConsultationDto();

        when(consultationRepository.findAllByDoctorUsername(username))
                .thenReturn(List.of(consultation1, consultation2));
        when(consultationMapper.toConsultationDto(consultation1)).thenReturn(dto1);
        when(consultationMapper.toConsultationDto(consultation2)).thenReturn(dto2);

        List<ConsultationDto> result = consultationService.getAllByDoctorUsername(username);

        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));
        verify(consultationRepository).findAllByDoctorUsername(username);
        verify(consultationMapper, times(2)).toConsultationDto(any(Consultation.class));
    }

    @Test
    void updateConsultationTest() {
        ConsultationDto dto = new ConsultationDto();
        dto.setId(1);
        dto.setStartTime(LocalTime.of(9, 0));
        dto.setEndTime(LocalTime.of(10, 0));

        Consultation consultation = new Consultation();
        consultation.setStartTime(LocalTime.of(8, 0));
        consultation.setEndTime(LocalTime.of(9, 0));

        when(consultationRepository.findById(dto.getId())).thenReturn(Optional.of(consultation));

        consultationService.updateConsultation(dto);

        assertEquals(dto.getStartTime(), consultation.getStartTime());
        assertEquals(dto.getEndTime(), consultation.getEndTime());
        verify(consultationRepository).save(consultation);
    }

    @Test
    void createConsultationTest() {
        ConsultationDto dto = new ConsultationDto();
        Consultation consultation = new Consultation();

        when(consultationMapper.toConsultation(dto)).thenReturn(consultation);

        consultationService.createConsultation(dto);

        verify(consultationMapper).toConsultation(dto);
        verify(consultationRepository).save(consultation);
    }

    @Test
    void deleteConsultationTest() {
        int id = 1;
        consultationService.deleteConsultation(id);
        verify(consultationRepository).deleteById(id);
    }
}
