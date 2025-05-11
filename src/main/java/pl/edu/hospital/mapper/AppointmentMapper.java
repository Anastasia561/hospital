package pl.edu.hospital.mapper;

import pl.edu.hospital.dto.AppointmentDto;
import pl.edu.hospital.entity.Appointment;
import pl.edu.hospital.entity.Patient;

public class AppointmentMapper {
    public static AppointmentDto toAppointmentDto(Appointment appointment, Patient patient) {
        AppointmentDto dto = new AppointmentDto();
        dto.setDate(appointment.getDate());
        dto.setStartTime(appointment.getTime());
        dto.setEndTime(appointment.getTime().plusHours(1)); //1 hour for appointment
        dto.setStatus(appointment.getStatus());
        dto.setPatientPhone(patient.getPhoneNumber());
        dto.setPatientFullName(patient.getFirstName() + " " + patient.getLastName());
        return dto;
    }
}
