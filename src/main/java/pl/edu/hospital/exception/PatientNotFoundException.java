package pl.edu.hospital.exception;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException(String username) {
        super("Patient with username - " + username + " not found");
    }
}
