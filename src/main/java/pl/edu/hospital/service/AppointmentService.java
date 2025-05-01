package pl.edu.hospital.service;

import org.springframework.stereotype.Service;
import pl.edu.hospital.entity.Appointment;
import pl.edu.hospital.repository.AppointmentRepository;

import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> getAll() {
        return appointmentRepository.findAll();
    }
}
