package pl.edu.hospital.exception;

public class DoctorNotFoundException extends RuntimeException {
    public DoctorNotFoundException(String username) {
        super("Doctor with username - " + username + " not found");
    }
}
