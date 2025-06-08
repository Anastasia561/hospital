package pl.edu.hospital.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.hospital.entity.Appointment;
import pl.edu.hospital.entity.Consultation;
import pl.edu.hospital.entity.Doctor;
import pl.edu.hospital.entity.Patient;
import pl.edu.hospital.entity.enums.Status;
import pl.edu.hospital.entity.enums.WorkingDay;
import pl.edu.hospital.exception.AppointmentNotFoundException;
import pl.edu.hospital.exception.ConsultationNotFoundException;
import pl.edu.hospital.repository.AppointmentRepository;
import pl.edu.hospital.repository.ConsultationRepository;
import pl.edu.hospital.repository.DoctorRepository;
import pl.edu.hospital.repository.PatientRepository;
import pl.edu.hospital.service.AppointmentService;
import pl.edu.hospital.service.EmailService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {
    @Mock
    AppointmentRepository appointmentRepository;
    @Mock
    PatientRepository patientRepository;
    @Mock
    DoctorRepository doctorRepository;
    @Mock
    ConsultationRepository consultationRepository;
    @Mock
    EmailService emailService;
    @InjectMocks
    AppointmentService appointmentService;

    @Test
    void getStatisticsForDoctorAndPeriodTest() {
        String username = "doc1";
        LocalDate start = LocalDate.now().minusDays(5);
        LocalDate end = LocalDate.now();

        for (Status status : Status.values()) {
            when(appointmentRepository.countByDoctorUsername(username, status, start, end)).thenReturn(status.ordinal());
        }

        Map<Status, Integer> stats = appointmentService.getStatisticsForDoctorAndPeriod(username, start, end);

        for (Status status : Status.values()) {
            assertEquals(status.ordinal(), stats.get(status));
        }
    }

    @Test
    void updateAppointmentStatusTest() {
        int id = 1;
        Appointment appointment = mock(Appointment.class);

        when(appointmentRepository.findById(id)).thenReturn(Optional.of(appointment));

        appointmentService.updateAppointmentStatus(Status.CANCELLED, id, true);

        verify(appointment).setStatus(Status.CANCELLED);
        verify(appointmentRepository).save(appointment);
        verify(emailService).sendCancellationEmailForDoctorByDoctor(id);
        verify(emailService).sendCancellationEmailForPatientByDoctor(id);

        clearInvocations(appointment, appointmentRepository, emailService);

        appointmentService.updateAppointmentStatus(Status.CANCELLED, id, false);

        verify(emailService).sendCancellationEmailToPatientByPatient(id);
        verify(emailService).sendCancellationEmailForDoctorByPatient(id);
    }

    @Test
    void getAppointmentDateByIdTest_shouldReturnDate() {
        int appointmentId = 1;
        LocalDate expectedDate = LocalDate.of(2025, 6, 8);
        Appointment mockAppointment = new Appointment();
        mockAppointment.setDate(expectedDate);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(mockAppointment));

        LocalDate actualDate = appointmentService.getAppointmentDateById(appointmentId);

        assertEquals(expectedDate, actualDate);
        verify(appointmentRepository).findById(appointmentId);
    }

    @Test
    void getAppointmentDateByIdTest_shouldThrowExceptionWhenAppointmentNotFound() {
        int appointmentId = 1;

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        assertThrows(AppointmentNotFoundException.class, () ->
                appointmentService.getAppointmentDateById(appointmentId)
        );

        verify(appointmentRepository).findById(appointmentId);
    }

    @Test
    void createAppointmentTest() {
        String patientUsername = "p1";
        String doctorUsername = "d1";
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.NOON;

        Patient patient = mock(Patient.class);
        Doctor doctor = mock(Doctor.class);

        when(patientRepository.findByUsername(patientUsername)).thenReturn(Optional.of(patient));
        when(doctorRepository.findByUsername(doctorUsername)).thenReturn(Optional.of(doctor));

        appointmentService.createAppointment(patientUsername, doctorUsername, date, time);

        ArgumentCaptor<Appointment> captor = ArgumentCaptor.forClass(Appointment.class);
        verify(appointmentRepository).save(captor.capture());

        Appointment saved = captor.getValue();
        assertEquals(Status.SCHEDULED, saved.getStatus());
        assertEquals(date, saved.getDate());
        assertEquals(time, saved.getTime());
        assertEquals(patient, saved.getPatient());
        assertEquals(doctor, saved.getDoctor());

        verify(emailService).sendConfirmationEmail(patientUsername, doctorUsername, date, time);
    }

    @Test
    void cancelForDeletedConsultationTest_shouldCancelMatchingAppointments() {
        int consultationId = 42;
        WorkingDay workingDay = WorkingDay.MONDAY;
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(12, 0);

        Consultation consultation = new Consultation();
        consultation.setId(consultationId);
        consultation.setStartTime(startTime);
        consultation.setEndTime(endTime);
        consultation.setWorkingDay(workingDay);

        Appointment app1 = new Appointment();
        app1.setId(1);
        Appointment app2 = new Appointment();
        app2.setId(2);

        when(appointmentRepository.findById(1)).thenReturn(Optional.of(app1));
        when(appointmentRepository.findById(2)).thenReturn(Optional.of(app2));

        when(consultationRepository.findById(consultationId)).thenReturn(Optional.of(consultation));
        when(appointmentRepository.getAllDayOfWeekAndInTimeRange(workingDay.getMysqlDay(), startTime, endTime))
                .thenReturn(List.of(app1, app2));

        appointmentService.cancelForDeletedConsultation(consultationId);

        verify(consultationRepository).findById(consultationId);
        verify(appointmentRepository).getAllDayOfWeekAndInTimeRange(workingDay.getMysqlDay(), startTime, endTime);
        verify(appointmentRepository, times(2)).findById(anyInt());
    }

    @Test
    void cancelForDeletedConsultationTest_shouldThrowExceptionWhenConsultationNotFound() {
        int consultationId = 999;
        when(consultationRepository.findById(consultationId)).thenReturn(Optional.empty());

        assertThrows(ConsultationNotFoundException.class, () ->
                appointmentService.cancelForDeletedConsultation(consultationId));

        verify(consultationRepository).findById(consultationId);
        verifyNoInteractions(appointmentRepository);
    }

    @Test
    void cancelForUpdatedConsultationTest_shouldCancelAppointmentsOutsideNewTimeRange() {
        int consultationId = 42;
        WorkingDay workingDay = WorkingDay.MONDAY;
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(12, 0);

        Consultation consultation = new Consultation();
        consultation.setId(consultationId);
        consultation.setStartTime(startTime);
        consultation.setEndTime(endTime);
        consultation.setWorkingDay(workingDay);

        Appointment app1 = new Appointment();
        app1.setId(1);
        Appointment app2 = new Appointment();
        app2.setId(2);

        when(consultationRepository.findById(consultationId)).thenReturn(Optional.of(consultation));
        when(appointmentRepository.getAllDayOfWeekAndNotInTimeRange(
                workingDay.getMysqlDay(), startTime, endTime))
                .thenReturn(List.of(app1, app2));

        when(appointmentRepository.findById(1)).thenReturn(Optional.of(app1));
        when(appointmentRepository.findById(2)).thenReturn(Optional.of(app2));

        appointmentService.cancelForUpdatedConsultation(consultationId);

        verify(appointmentRepository).findById(1);
        verify(appointmentRepository).findById(2);
        verify(emailService, times(2)).sendCancellationEmailForDoctorByDoctor(anyInt());
        verify(emailService, times(2)).sendCancellationEmailForPatientByDoctor(anyInt());
    }

    @Test
    void getAvailableTimeSlotsForDoctorByUsernameTest() {
        String username = "doc";
        LocalDate date = LocalDate.of(2023, 6, 8);
        WorkingDay workingDay = WorkingDay.valueOf(date.getDayOfWeek().toString());

        Consultation consultation = new Consultation();
        consultation.setStartTime(LocalTime.of(9, 0));
        consultation.setEndTime(LocalTime.of(12, 0));
        consultation.setWorkingDay(workingDay);

        Appointment a1 = new Appointment();
        a1.setTime(LocalTime.of(9, 0));
        a1.setStatus(Status.SCHEDULED);

        Appointment a2 = new Appointment();
        a2.setTime(LocalTime.of(10, 0));
        a2.setStatus(Status.CANCELLED);

        when(consultationRepository.findByDoctorUsernameAndWorkingDay(username, workingDay))
                .thenReturn(Optional.of(consultation));

        when(appointmentRepository.findForDoctorByDateAndTimeRange(username, date, consultation.getStartTime(), consultation.getEndTime()))
                .thenReturn(List.of(a1, a2));

        List<LocalTime> slots = appointmentService.getAvailableTimeSlotsForDoctorByUsername(username, date);

        assertTrue(slots.contains(LocalTime.of(11, 0)));
    }
}
