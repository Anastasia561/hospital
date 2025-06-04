package pl.edu.hospital.service;

import org.springframework.stereotype.Service;
import pl.edu.hospital.dto.record.RecordCreationRequestDto;
import pl.edu.hospital.dto.record.RecordForDoctorDto;
import pl.edu.hospital.dto.record.RecordForPatientDto;
import pl.edu.hospital.entity.Prescription;
import pl.edu.hospital.entity.Record;
import pl.edu.hospital.exception.RecordNotFoundException;
import pl.edu.hospital.mapper.PrescriptionMapper;
import pl.edu.hospital.mapper.RecordMapper;
import pl.edu.hospital.repository.RecordRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecordService {
    private final RecordRepository recordRepository;
    private final RecordMapper recordMapper;
    private final PrescriptionMapper prescriptionMapper;

    public RecordService(RecordRepository recordRepository,
                         RecordMapper recordMapper, PrescriptionMapper prescriptionMapper) {
        this.recordRepository = recordRepository;
        this.recordMapper = recordMapper;
        this.prescriptionMapper = prescriptionMapper;
    }

    public RecordForDoctorDto getRecordForDoctorByAppointmentId(int id) {
        Record record = recordRepository.findByAppointmentId(id)
                .orElseThrow(() -> new RecordNotFoundException(id));
        return recordMapper.toRecordForDoctorDto(record);
    }

    public RecordForPatientDto getRecordForPatientByAppointmentId(int id) {
        Record record = recordRepository.findByAppointmentId(id)
                .orElseThrow(() -> new RecordNotFoundException(id));
        return recordMapper.toRecordForPatientDto(record);
    }

    public void saveRecord(RecordCreationRequestDto dto) {
        Record record = recordMapper.toRecord(dto);

        List<Prescription> prescriptions = dto.getPrescriptions().stream()
                .map(prescriptionMapper::toPrescription)
                .peek(p -> p.setRecord(record))
                .collect(Collectors.toCollection(ArrayList::new));

        record.setPrescription(prescriptions);
        recordRepository.save(record);
    }
}
