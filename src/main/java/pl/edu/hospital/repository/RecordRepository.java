package pl.edu.hospital.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.hospital.entity.Record;

import java.util.Optional;

@Repository
public interface RecordRepository extends CrudRepository<Record, Integer> {
    @Query(value = "SELECT * FROM record WHERE appointment_id = :id", nativeQuery = true)
    Optional<Record> findByAppointmentId(int id);
}
