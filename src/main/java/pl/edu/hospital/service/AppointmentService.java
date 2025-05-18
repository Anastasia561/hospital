package pl.edu.hospital.service;

import org.springframework.stereotype.Service;
import pl.edu.hospital.dto.appointment.AppointmentForDoctorDto;
import pl.edu.hospital.dto.appointment.AppointmentForPatientDto;
import pl.edu.hospital.entity.Appointment;
import pl.edu.hospital.entity.Consultation;
import pl.edu.hospital.entity.Doctor;
import pl.edu.hospital.entity.Patient;
import pl.edu.hospital.entity.enums.Specialization;
import pl.edu.hospital.entity.enums.Status;
import pl.edu.hospital.entity.enums.WorkingDay;
import pl.edu.hospital.exception.AppointmentNotFoundException;
import pl.edu.hospital.exception.ConsultationNotFoundException;
import pl.edu.hospital.exception.DoctorNotFoundException;
import pl.edu.hospital.exception.PatientNotFoundException;
import pl.edu.hospital.mapper.AppointmentMapper;
import pl.edu.hospital.repository.AppointmentRepository;
import pl.edu.hospital.repository.ConsultationRepository;
import pl.edu.hospital.repository.DoctorRepository;
import pl.edu.hospital.repository.PatientRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
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
    private final ConsultationRepository consultationRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository,
                              DoctorRepository doctorRepository, ConsultationRepository consultationRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.consultationRepository = consultationRepository;
    }

    public Map<Status, Integer> getStatisticsForDoctorAndPeriod(String username, LocalDate startDate, LocalDate endDate) {
        HashMap<Status, Integer> statistics = new HashMap<>();

        for (Status s : Status.values()) {
            Integer count = appointmentRepository.countByDoctorUsername(username, s, startDate, endDate);
            statistics.put(s, count);
        }

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
                    Doctor doctor = doctorRepository.findById(a.getDoctor().getId())
                            .orElseThrow(() -> new DoctorNotFoundException(a.getDoctor().getUsername()));
                    return AppointmentMapper.toAppointmentForPatientDto(a, doctor);
                })
                .filter(a -> status == null || a.getStatus() == status)
                .filter(a -> specialization == null || a.getSpecialization() == specialization)
                .collect(Collectors.groupingBy(
                        AppointmentForPatientDto::getDate,
                        LinkedHashMap::new,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparing(AppointmentForPatientDto::getStartTime))
                                        .collect(Collectors.toList())
                        )
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
                    Patient patient = patientRepository.findById(a.getPatient().getId())
                            .orElseThrow(() -> new PatientNotFoundException(a.getPatient().getUsername()));
                    return AppointmentMapper.toAppointmentForDoctorDto(a, patient);
                })
                .filter(a -> status == null || a.getStatus() == status)
                .collect(Collectors.groupingBy(
                        AppointmentForDoctorDto::getDate,
                        LinkedHashMap::new,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparing(AppointmentForDoctorDto::getStartTime))
                                        .collect(Collectors.toList())
                        )
                ));
    }

    public void updateAppointmentStatus(Status newStatus, int id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));
        appointment.setStatus(newStatus);
        appointmentRepository.save(appointment);
    }

    public LocalDate getAppointmentDateById(int id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id)).getDate();
    }

    public void cancelForDeletedConsultation(int consultationId) {
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new ConsultationNotFoundException(consultationId + ""));
        List<Appointment> apps = appointmentRepository.getAllDayOfWeekAndInTimeRange(consultation.getWorkingDay().getMysqlDay(),
                consultation.getStartTime(), consultation.getEndTime());
        apps.forEach(a -> updateAppointmentStatus(Status.CANCELLED, a.getId()));
    }

    public void cancelForUpdatedConsultation(int consultationId) {
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new ConsultationNotFoundException(consultationId + ""));
        List<Appointment> apps = appointmentRepository.getAllDayOfWeekAndNotInTimeRange(consultation.getWorkingDay().getMysqlDay(),
                consultation.getStartTime(), consultation.getEndTime());
        apps.forEach(a -> updateAppointmentStatus(Status.CANCELLED, a.getId()));
    }

    public List<LocalTime> getAvailableTimeSlotsForDoctorByUsername(String username, LocalDate date) {
        WorkingDay day = WorkingDay.valueOf(date.getDayOfWeek().toString());
        Consultation consultation = consultationRepository.findByDoctorUsernameAndWorkingDay(username, day)
                .orElseThrow(() -> new ConsultationNotFoundException(username));
        List<LocalTime> apps = appointmentRepository.findForDoctorByDateAndTimeRange(
                        username, date, consultation.getStartTime(), consultation.getEndTime())
                .stream()
                .filter(a -> a.getStatus() == Status.SCHEDULED)
                .map(Appointment::getTime)
                .toList();

        LocalTime lastTime = consultation.getEndTime();
        LocalTime timeSlot = consultation.getStartTime();
        List<LocalTime> result = new ArrayList<>();
        while (!timeSlot.equals(lastTime)) {
            if (!apps.contains(timeSlot)) {
                result.add(timeSlot);
            }
            timeSlot = timeSlot.plusHours(1);
        }
        if (result.isEmpty()) {
            throw new RuntimeException("No time available");
        }
        return result;
    }

    public void createAppointment(String patientUsername, String doctorUsername, LocalDate date, LocalTime time) {
        Patient patient = patientRepository.findByUsername(patientUsername)
                .orElseThrow(() -> new PatientNotFoundException(patientUsername));
        Doctor doctor = doctorRepository.findByUsername(doctorUsername)
                .orElseThrow(() -> new DoctorNotFoundException(doctorUsername));
        Appointment appointment = new Appointment();
        appointment.setStatus(Status.SCHEDULED);
        appointment.setDate(date);
        appointment.setTime(time);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointmentRepository.save(appointment);
    }
}
