package pl.edu.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.edu.hospital.entity.Appointment;
import pl.edu.hospital.entity.enums.Status;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByPatientUsername(String username);

    @Query("select count(a) from Appointment a where a.doctor.username=:username and a.status=:status")
    Integer countByDoctorUsername(String username, Status status);

}
