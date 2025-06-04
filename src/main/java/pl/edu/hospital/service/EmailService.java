package pl.edu.hospital.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import org.springframework.mail.javamail.JavaMailSender;
import pl.edu.hospital.entity.Appointment;
import pl.edu.hospital.entity.Doctor;
import pl.edu.hospital.entity.Patient;
import pl.edu.hospital.exception.AppointmentNotFoundException;
import pl.edu.hospital.exception.DoctorNotFoundException;
import pl.edu.hospital.exception.PatientNotFoundException;
import pl.edu.hospital.repository.AppointmentRepository;
import pl.edu.hospital.repository.DoctorRepository;
import pl.edu.hospital.repository.PatientRepository;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;


    public EmailService(JavaMailSender mailSender, PatientRepository patientRepository,
                        DoctorRepository doctorRepository, AppointmentRepository appointmentRepository) {
        this.mailSender = mailSender;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Async
    public void sendConfirmationEmail(String patientUsername, String doctorUsername,
                                      LocalDate date, LocalTime time) {
        SimpleMailMessage message = new SimpleMailMessage();

        Doctor doctor = doctorRepository.findByUsername(doctorUsername)
                .orElseThrow(() -> new DoctorNotFoundException(doctorUsername));
        Patient patient = patientRepository.findByUsername(patientUsername)
                .orElseThrow(() -> new PatientNotFoundException(patientUsername));

        message.setTo(patient.getEmail());
        message.setSubject("Appointment confirmation – MediCore");
        message.setText("Dear " + patient.getFirstName() + " " + patient.getLastName() + ",\n\n" +
                "Thank you for booking your appointment with us at MediCore.\n\n" +
                "Here are the details of your appointment:\n" +
                "- Doctor: Dr. " + doctor.getFirstName() + " " + doctor.getLastName() + "\n" +
                "- Specialization: " + doctor.getSpecialization() + "\n" +
                "- Date: " + date + "\n" +
                "- Time: " + time + "\n" +
                "If you need to reschedule or cancel, please navigate to medicore.com.\n\n" +
                "Best regards,\n" +
                "The MediCore Team");

        mailSender.send(message);
    }

    @Async
    public void sendCancellationEmailToPatientByPatient(int appId) {
        SimpleMailMessage message = new SimpleMailMessage();

        Appointment appointment = appointmentRepository.findById(appId)
                .orElseThrow(() -> new AppointmentNotFoundException(appId));

        Doctor doctor = appointment.getDoctor();
        Patient patient = appointment.getPatient();

        message.setTo(appointment.getPatient().getEmail());
        message.setSubject("Appointment cancellation – MediCore");
        message.setText("Dear " + patient.getFirstName() + " " + patient.getLastName() + ",\n\n" +
                "We would like to inform you that your appointment at MediCore has been successfully canceled.\n\n" +
                "Canceled appointment details:\n" +
                "- Doctor: Dr. " + doctor.getFirstName() + " " + doctor.getLastName() + "\n" +
                "- Specialization: " + doctor.getSpecialization() + "\n" +
                "- Date: " + appointment.getDate() + "\n" +
                "- Time: " + appointment.getTime() + "\n\n" +
                "We hope to see you soon.\n\n" +
                "Best regards,\n" +
                "The MediCore Team");

        mailSender.send(message);
    }

    @Async
    public void sendCancellationEmailForDoctorByPatient(int appId) {
        SimpleMailMessage message = new SimpleMailMessage();

        Appointment appointment = appointmentRepository.findById(appId)
                .orElseThrow(() -> new AppointmentNotFoundException(appId));

        Doctor doctor = appointment.getDoctor();
        Patient patient = appointment.getPatient();

        message.setTo(patient.getEmail());
        message.setSubject("Appointment cancellation – MediCore");
        message.setText("Dear Dr. " + doctor.getFirstName() + " " + doctor.getLastName() + ",\n\n" +
                "We would like to inform you that an appointment has been canceled by the patient.\n\n" +
                "Canceled appointment details:\n" +
                "- Patient: " + patient.getFirstName() + " " + patient.getLastName() + "\n" +
                "- Date: " + appointment.getDate() + "\n" +
                "- Time: " + appointment.getTime() + "\n\n" +
                "Please update your schedule accordingly.\n\n" +
                "Best regards,\n" +
                "The MediCore Team");

        mailSender.send(message);
    }

    @Async
    public void sendCancellationEmailForDoctorByDoctor(int appId) {
        SimpleMailMessage message = new SimpleMailMessage();

        Appointment appointment = appointmentRepository.findById(appId)
                .orElseThrow(() -> new AppointmentNotFoundException(appId));

        Patient patient = appointment.getPatient();

        message.setTo(patient.getEmail());
        message.setSubject("Appointment cancellation – MediCore");
        message.setText("Dear " + patient.getFirstName() + " " + patient.getLastName() + ",\n\n" +
                "We would like to inform you that your appointment at MediCore has been successfully canceled.\n\n" +
                "Canceled appointment details:\n" +
                "- Patient: " + patient.getFirstName() + " " + patient.getLastName() + "\n" +
                "- Date: " + appointment.getDate() + "\n" +
                "- Time: " + appointment.getTime() + "\n\n" +
                "Best regards,\n" +
                "The MediCore Team");

        mailSender.send(message);
    }

    @Async
    public void sendCancellationEmailForPatientByDoctor(int appId) {
        SimpleMailMessage message = new SimpleMailMessage();

        Appointment appointment = appointmentRepository.findById(appId)
                .orElseThrow(() -> new AppointmentNotFoundException(appId));

        Patient patient = appointment.getPatient();
        Doctor doctor = appointment.getDoctor();

        message.setTo(patient.getEmail());
        message.setSubject("Appointment cancellation – MediCore");
        message.setText("Dear " + patient.getFirstName() + " " + patient.getLastName() + ",\n\n" +
                "We would like to inform you that your appointment at MediCore has been canceled by the doctor.\n\n" +
                "Canceled appointment details:\n" +
                "- Doctor: Dr. " + doctor.getFirstName() + " " + doctor.getLastName() + "\n" +
                "- Specialization: " + doctor.getSpecialization() + "\n" +
                "- Date: " + appointment.getDate() + "\n" +
                "- Time: " + appointment.getTime() + "\n\n" +
                "We apologize for any inconvenience this may cause.\n" +
                "Thank you for understanding.\n\n" +
                "We hope to see you soon.\n\n" +
                "Best regards,\n" +
                "The MediCore Team");

        mailSender.send(message);
    }
}
