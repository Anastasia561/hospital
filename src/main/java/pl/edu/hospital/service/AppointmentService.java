package pl.edu.hospital.service;

import org.springframework.stereotype.Service;
import pl.edu.hospital.dto.AppointmentDto;
import pl.edu.hospital.entity.Appointment;
import pl.edu.hospital.entity.enums.Status;
import pl.edu.hospital.mapper.AppointmentMapper;
import pl.edu.hospital.repository.AppointmentRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> getAll() {
        return appointmentRepository.findAll();
    }

    public Map<Status, Integer> getStatisticsForDoctorAndPeriod(String username, LocalDate startDate, LocalDate endDate) {
        HashMap<Status, Integer> statistics = new HashMap<>();

        Integer scheduledApp = appointmentRepository.countByDoctorUsername(username, Status.SCHEDULED, startDate, endDate);
        Integer canceledApp = appointmentRepository.countByDoctorUsername(username, Status.SCHEDULED, startDate, endDate);
        Integer completedApp = appointmentRepository.countByDoctorUsername(username, Status.SCHEDULED, startDate, endDate);

        statistics.put(Status.SCHEDULED, scheduledApp);
        statistics.put(Status.CANCELLED, canceledApp);
        statistics.put(Status.COMPLETED, completedApp);
        return statistics;
    }

    public List<AppointmentDto> getAllForDoctorByUsername(String username) {
        return appointmentRepository.findByDoctorUsername(username)
                .stream()
                .map(AppointmentMapper::toAppointmentDto)
                .toList();
    }
}
