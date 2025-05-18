package pl.edu.hospital.exception;

public class AdminNotFoundException extends RuntimeException {
    public AdminNotFoundException(String username) {
        super("Admin with username - " + username + " not found");
    }
}
