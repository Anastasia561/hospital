package pl.edu.hospital.exception;

public class AppointmentNotFoundException extends RuntimeException {
    public AppointmentNotFoundException(int id) {
        super("Appointment with id - " + id + " not found");
    }
}
