package pl.edu.hospital.mapper;

import pl.edu.hospital.dto.AppointmentForDoctorDto;
import pl.edu.hospital.dto.AppointmentForPatientDto;
import pl.edu.hospital.entity.Appointment;
import pl.edu.hospital.entity.Doctor;
import pl.edu.hospital.entity.Patient;

public class AppointmentMapper {
    public static AppointmentForDoctorDto toAppointmentForDoctorDto(Appointment appointment, Patient patient) {
        AppointmentForDoctorDto dto = new AppointmentForDoctorDto();
        dto.setId(appointment.getId());
        dto.setDate(appointment.getDate());
        dto.setStartTime(appointment.getTime());
        dto.setEndTime(appointment.getTime().plusHours(1)); //1 hour for appointment
        dto.setStatus(appointment.getStatus());
        dto.setPatientPhone(patient.getPhoneNumber());
        dto.setPatientFullName(patient.getFirstName() + " " + patient.getLastName());
        return dto;
    }

    public static AppointmentForPatientDto toAppointmentForPatientDto(Appointment appointment, Doctor doctor) {
        AppointmentForPatientDto dto = new AppointmentForPatientDto();
        dto.setDate(appointment.getDate());
        dto.setStartTime(appointment.getTime());
        dto.setEndTime(appointment.getTime().plusHours(1)); //1 hour for appointment
        dto.setStatus(appointment.getStatus());
        dto.setDoctorFullName(doctor.getFirstName() + " " + doctor.getLastName());
        dto.setSpecialization(doctor.getSpecialization());
        return dto;
    }
}
