package pl.edu.hospital.mapper;

import pl.edu.hospital.dto.PrescriptionDto;
import pl.edu.hospital.entity.Prescription;

public class PrescriptionMapper {
    public static PrescriptionDto toPrescriptionDto(Prescription prescription) {
        PrescriptionDto dto = new PrescriptionDto();
        dto.setMedicine(prescription.getMedicine());
        dto.setDosage(prescription.getDosage());
        dto.setFrequency(prescription.getFrequency());
        dto.setStartDate(prescription.getStartDate());
        dto.setEndDate(prescription.getEndDate());
        return dto;
    }
}
