package pl.edu.hospital.exception;

public class ConsultationNotFoundException extends RuntimeException {
    public ConsultationNotFoundException(int id) {
        super("Consultation with id - " + id + " not found");
    }
}
