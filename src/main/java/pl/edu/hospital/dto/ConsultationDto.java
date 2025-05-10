package pl.edu.hospital.dto;

import pl.edu.hospital.entity.enums.WorkingDay;

import java.time.LocalTime;

public class ConsultationDto {
    private int id;
    private String doctorUsername;
    private WorkingDay day;
    private LocalTime startTime;
    private LocalTime endTime;

    public String getDoctorUsername() {
        return doctorUsername;
    }

    public void setDoctorUsername(String doctorUsername) {
        this.doctorUsername = doctorUsername;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public WorkingDay getDay() {
        return day;
    }

    public void setDay(WorkingDay day) {
        this.day = day;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
