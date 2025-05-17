package pl.edu.hospital.exception;

public class ConsultationNotFoundException extends RuntimeException {
    public ConsultationNotFoundException(String id) {
        super("Consultation with id - " + id + " not found");
    }
}
