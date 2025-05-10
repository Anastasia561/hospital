package pl.edu.hospital.dto;

import pl.edu.hospital.entity.enums.Status;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentDto {
    private LocalDate date;
    private LocalTime time;
    private Status status;
    private Integer patientId;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }
}
