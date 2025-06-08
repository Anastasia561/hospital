package pl.edu.hospital.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.hospital.dto.PrescriptionDto;
import pl.edu.hospital.dto.record.RecordCreationRequestDto;
import pl.edu.hospital.dto.record.RecordForDoctorDto;
import pl.edu.hospital.dto.record.RecordForPatientDto;
import pl.edu.hospital.entity.Prescription;
import pl.edu.hospital.entity.Record;
import pl.edu.hospital.exception.RecordNotFoundException;
import pl.edu.hospital.mapper.PrescriptionMapper;
import pl.edu.hospital.mapper.RecordMapper;
import pl.edu.hospital.repository.RecordRepository;
import pl.edu.hospital.service.RecordService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecordServiceTest {
    @Mock
    private RecordRepository recordRepository;
    @Mock
    private RecordMapper recordMapper;
    @Mock
    private PrescriptionMapper prescriptionMapper;
    @InjectMocks
    private RecordService recordService;

    @Test
    void getRecordForDoctorByAppointmentIdTest_shouldThrowExceptionWhenRecordNotFound() {
        int appId = 1;
        when(recordRepository.findByAppointmentId(appId)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> {
            recordService.getRecordForDoctorByAppointmentId(appId);
        });
    }

    @Test
    void getRecordForDoctorByAppointmentIdTest_shouldReturnDto() {
        int appointmentId = 1;
        Record record = new Record();
        RecordForDoctorDto dto = new RecordForDoctorDto();

        when(recordRepository.findByAppointmentId(appointmentId)).thenReturn(Optional.of(record));
        when(recordMapper.toRecordForDoctorDto(record)).thenReturn(dto);

        RecordForDoctorDto result = recordService.getRecordForDoctorByAppointmentId(appointmentId);

        assertEquals(dto, result);
        verify(recordRepository).findByAppointmentId(appointmentId);
        verify(recordMapper).toRecordForDoctorDto(record);
    }

    @Test
    void getRecordForPatientByAppointmentIdTest_shouldReturnDto() {
        int appointmentId = 2;
        Record record = new Record();
        RecordForPatientDto dto = new RecordForPatientDto();

        when(recordRepository.findByAppointmentId(appointmentId)).thenReturn(Optional.of(record));
        when(recordMapper.toRecordForPatientDto(record)).thenReturn(dto);

        RecordForPatientDto result = recordService.getRecordForPatientByAppointmentId(appointmentId);

        assertEquals(dto, result);
        verify(recordRepository).findByAppointmentId(appointmentId);
        verify(recordMapper).toRecordForPatientDto(record);
    }

    @Test
    void getRecordForPatientByAppointmentIdTest_shouldThrowExceptionWhenRecordNotFound() {
        int appointmentId = 2;

        when(recordRepository.findByAppointmentId(appointmentId)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () ->
                recordService.getRecordForPatientByAppointmentId(appointmentId)
        );
    }

    @Test
    void saveRecordTest() {
        RecordCreationRequestDto dto = new RecordCreationRequestDto();
        Record record = new Record();

        PrescriptionDto prescriptionDto1 = new PrescriptionDto();
        PrescriptionDto prescriptionDto2 = new PrescriptionDto();
        dto.setPrescriptions(List.of(prescriptionDto1, prescriptionDto2));

        Prescription prescription1 = new Prescription();
        Prescription prescription2 = new Prescription();

        when(recordMapper.toRecord(dto)).thenReturn(record);
        when(prescriptionMapper.toPrescription(prescriptionDto1)).thenReturn(prescription1);
        when(prescriptionMapper.toPrescription(prescriptionDto2)).thenReturn(prescription2);

        recordService.saveRecord(dto);

        assertEquals(record, prescription1.getRecord());
        assertEquals(record, prescription2.getRecord());

        verify(recordRepository).save(record);
    }
}
