package pl.edu.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.hospital.entity.Doctor;
import pl.edu.hospital.entity.enums.Specialization;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    Doctor findByUsername(String username);

    List<Doctor> findAllBySpecialization(Specialization specialization);
}
