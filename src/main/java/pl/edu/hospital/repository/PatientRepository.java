package pl.edu.hospital.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.hospital.entity.Patient;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends CrudRepository<Patient, Integer> {
    List<Patient> findAll();

    Optional<Patient> findByUsername(String username);

    List<Patient> getPatientsByEmail(String email);

    @Query("select p from Patient p join p.appointments a WHERE a.id = :id")
    Optional<Patient> findPatientByAppointmentId(int id);
}
