package pl.edu.hospital.mapper;

import org.springframework.stereotype.Component;
import pl.edu.hospital.dto.appointment.AppointmentForDoctorDto;
import pl.edu.hospital.dto.appointment.AppointmentForPatientDto;
import pl.edu.hospital.entity.Appointment;
import pl.edu.hospital.entity.Doctor;
import pl.edu.hospital.entity.Patient;

@Component
public class AppointmentMapper {

    public AppointmentForDoctorDto toAppointmentForDoctorDto(Appointment appointment) {
        AppointmentForDoctorDto dto = new AppointmentForDoctorDto();
        dto.setId(appointment.getId());
        dto.setDate(appointment.getDate());
        dto.setStartTime(appointment.getTime());
        dto.setEndTime(appointment.getTime().plusHours(1)); //1 hour for appointment
        dto.setStatus(appointment.getStatus());

        Patient patient = appointment.getPatient();
        dto.setPatientPhone(patient.getPhoneNumber());
        dto.setPatientFullName(patient.getFirstName() + " " + patient.getLastName());

        return dto;
    }

    public AppointmentForPatientDto toAppointmentForPatientDto(Appointment appointment) {
        AppointmentForPatientDto dto = new AppointmentForPatientDto();
        dto.setId(appointment.getId());
        dto.setDate(appointment.getDate());
        dto.setStartTime(appointment.getTime());
        dto.setEndTime(appointment.getTime().plusHours(1)); //1 hour for appointment
        dto.setStatus(appointment.getStatus());

        Doctor doctor = appointment.getDoctor();
        dto.setDoctorFullName(doctor.getFirstName() + " " + doctor.getLastName());
        dto.setSpecialization(doctor.getSpecialization());

        return dto;
    }
}
