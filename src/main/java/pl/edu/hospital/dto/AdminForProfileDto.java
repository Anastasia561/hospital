package pl.edu.hospital.dto;

import java.time.LocalDate;

public class AdminForProfileDto extends PersonForProfileDto {
    private LocalDate employmentDate;

    public LocalDate getEmploymentDate() {
        return employmentDate;
    }

    public void setEmploymentDate(LocalDate employmentDate) {
        this.employmentDate = employmentDate;
    }
}
