package pl.edu.hospital.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import pl.edu.hospital.entity.enums.WorkingDay;

import java.time.LocalTime;

@Entity
public class Consultation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    @Column(name = "day")
    private WorkingDay workingDay;
    @Column(name = "start_work_time")
    private LocalTime startTime;
    @Column(name = "end_work_time")
    private LocalTime endTime;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
}
