package pl.edu.hospital.exception;

public class RecordNotFoundException extends RuntimeException {
    public RecordNotFoundException(int id) {
        super("Record with identifier - " + id + " not found");
    }
}
