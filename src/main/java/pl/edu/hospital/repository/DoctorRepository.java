package pl.edu.hospital.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.hospital.entity.Doctor;
import pl.edu.hospital.entity.enums.Specialization;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends CrudRepository<Doctor, Integer> {
    List<Doctor> findAll();

    Optional<Doctor> findByUsername(String username);

    List<Doctor> findAllBySpecialization(Specialization specialization);

    @Query("select d from Doctor d join d.appointments a WHERE a.id = :id")
    Optional<Doctor> findDoctorByAppointmentId(int id);
}
