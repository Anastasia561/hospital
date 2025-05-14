package pl.edu.hospital.exception;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException(String ident) {
        super("Patient with identifier - " + ident + " not found");
    }
}
