package pl.edu.hospital.dto.doctor;

import pl.edu.hospital.dto.PersonForProfileDto;
import pl.edu.hospital.entity.enums.Specialization;

import java.time.LocalDate;

public class DoctorForProfileDto extends PersonForProfileDto {
    private LocalDate employmentDate;
    private Specialization specialization;

    public LocalDate getEmploymentDate() {
        return employmentDate;
    }

    public void setEmploymentDate(LocalDate employmentDate) {
        this.employmentDate = employmentDate;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }
}
