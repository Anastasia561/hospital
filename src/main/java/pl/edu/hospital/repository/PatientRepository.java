package pl.edu.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.edu.hospital.entity.Patient;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Optional<Patient> findByUsername(String username);

    List<Patient> getPatientsByEmail(String email);

    @Query("select p from Patient p join p.appointments a WHERE a.id = :id")
    Optional<Patient> findPatientByAppointmentId(int id);
}
