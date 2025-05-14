package pl.edu.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.edu.hospital.entity.Record;

import java.util.Optional;

@Repository
public interface RecordRepository extends JpaRepository<Record, Integer> {
    @Query(value = "SELECT * FROM record WHERE appointment_id = :id", nativeQuery = true)
    Optional<Record> findByAppointmentId(int id);
}
