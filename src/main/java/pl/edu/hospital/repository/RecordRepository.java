package pl.edu.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.hospital.entity.Record;

@Repository
public interface RecordRepository extends JpaRepository<Record, Integer> {
}
