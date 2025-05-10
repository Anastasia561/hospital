package pl.edu.hospital.mapper;

import pl.edu.hospital.dto.AppointmentDto;
import pl.edu.hospital.dto.AppointmentForDoctorDto;
import pl.edu.hospital.dto.PatientForScheduleDto;
import pl.edu.hospital.entity.Appointment;

public class AppointmentMapper {
    public static AppointmentDto toAppointmentDto(Appointment appointment) {
        AppointmentDto dto = new AppointmentDto();
        dto.setDate(appointment.getDate());
        dto.setTime(appointment.getTime());
        dto.setStatus(appointment.getStatus());
        dto.setPatientId(appointment.getPatient().getId());
        return dto;
    }

    public static AppointmentForDoctorDto toAppointmentForDoctorDto(AppointmentDto appDto,
                                                                    PatientForScheduleDto patientDto) {
        AppointmentForDoctorDto dto = new AppointmentForDoctorDto();
        dto.setDate(appDto.getDate());
        dto.setStatus(appDto.getStatus());
        dto.setTime(appDto.getTime());
        dto.setPatientEmail(patientDto.getEmail());
        dto.setPatientEmail(patientDto.getFullName());
        return dto;
    }
}
