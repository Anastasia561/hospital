package pl.edu.hospital.service;

import org.springframework.stereotype.Service;
import pl.edu.hospital.dto.AppointmentDto;
import pl.edu.hospital.entity.Appointment;
import pl.edu.hospital.entity.Patient;
import pl.edu.hospital.entity.enums.Status;
import pl.edu.hospital.mapper.AppointmentMapper;
import pl.edu.hospital.repository.AppointmentRepository;
import pl.edu.hospital.repository.PatientRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
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

    public LinkedHashMap<LocalDate, List<AppointmentDto>> getAllForDoctorByUsernameInRange(String username, LocalDate start, LocalDate end) {
        return appointmentRepository.findByDoctorUsernameInRange(username, start, end)
                .stream()
                .map(a -> {
                    Optional<Patient> patient = patientRepository.findById(a.getPatient().getId());
                    return AppointmentMapper.toAppointmentDto(a, patient.get());
                })
                .sorted(Comparator.comparing(AppointmentDto::getDate).thenComparing(AppointmentDto::getStartTime))
                .collect(Collectors.groupingBy(AppointmentDto::getDate, LinkedHashMap::new, Collectors.toList()));
    }

    public LinkedHashMap<LocalDate, List<AppointmentDto>> getAllForDoctorByUsernameInRangeWithStatus(String username,
                                                                                                     LocalDate start, LocalDate end, Status status) {
        return appointmentRepository.findByDoctorUsernameInRange(username, start, end)
                .stream()
                .map(a -> {
                    Optional<Patient> patient = patientRepository.findById(a.getPatient().getId());
                    return AppointmentMapper.toAppointmentDto(a, patient.get());
                })
                .filter(a -> a.getStatus() == status)
                .sorted(Comparator.comparing(AppointmentDto::getDate).thenComparing(AppointmentDto::getStartTime))
                .collect(Collectors.groupingBy(AppointmentDto::getDate, LinkedHashMap::new, Collectors.toList()));
    }
}
