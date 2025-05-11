package pl.edu.hospital.service;

import org.springframework.stereotype.Service;
import pl.edu.hospital.dto.AppointmentForDoctorDto;
import pl.edu.hospital.dto.AppointmentForPatientDto;
import pl.edu.hospital.entity.Appointment;
import pl.edu.hospital.entity.Doctor;
import pl.edu.hospital.entity.Patient;
import pl.edu.hospital.entity.enums.Specialization;
import pl.edu.hospital.entity.enums.Status;
import pl.edu.hospital.mapper.AppointmentMapper;
import pl.edu.hospital.repository.AppointmentRepository;
import pl.edu.hospital.repository.DoctorRepository;
import pl.edu.hospital.repository.PatientRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository,
                              DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
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

    public LinkedHashMap<LocalDate, List<AppointmentForPatientDto>> getAppointmentsForPatientFiltered(
            String username,
            LocalDate startDate,
            LocalDate endDate,
            Status status,
            Specialization specialization
    ) {
        List<Appointment> allAppointments =
                appointmentRepository.findByPatientUsernameInRange(username, startDate, endDate);

        return allAppointments.stream()
                .map(a -> {
                    Doctor doctor = doctorRepository.findById(a.getDoctor().getId()).get();
                    return AppointmentMapper.toAppointmentForPatientDto(a, doctor);
                })
                .filter(a -> status == null || a.getStatus() == status)
                .filter(a -> specialization == null || a.getSpecialization() == specialization)
                .collect(Collectors.groupingBy(
                        AppointmentForPatientDto::getDate,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }

    public LinkedHashMap<LocalDate, List<AppointmentForDoctorDto>> getAppointmentsForDoctorFiltered(
            String username,
            LocalDate startDate,
            LocalDate endDate,
            Status status
    ) {
        List<Appointment> allAppointments =
                appointmentRepository.findByDoctorUsernameInRange(username, startDate, endDate);

        return allAppointments.stream()
                .map(a -> {
                    Patient patient = patientRepository.findById(a.getPatient().getId()).get();
                    return AppointmentMapper.toAppointmentForDoctorDto(a, patient);
                })
                .filter(a -> status == null || a.getStatus() == status)
                .collect(Collectors.groupingBy(
                        AppointmentForDoctorDto::getDate,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }
}
