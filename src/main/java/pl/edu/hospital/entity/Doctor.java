package pl.edu.hospital.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import pl.edu.hospital.entity.enums.Specialization;
import pl.edu.hospital.entity.enums.WorkingDay;
import pl.edu.hospital.entity.enums.converter.WorkingDaysListConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
public class Doctor extends Person {
    private Integer experience;
    @Column(name = "employment_date")
    private LocalDate employmentDate;
    @Enumerated(EnumType.STRING)
    private Specialization specialization;
    @Convert(converter = WorkingDaysListConverter.class)
    @Column(name = "working_days")
    private List<WorkingDay> workingDays;
    @Column(name = "start_work_time")
    private LocalTime startTime;
    @Column(name = "end_work_time")
    private LocalTime endTime;

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

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
