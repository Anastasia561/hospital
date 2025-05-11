package pl.edu.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.edu.hospital.entity.Appointment;
import pl.edu.hospital.entity.enums.Status;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByDoctorUsername(String username);

    @Query("select count(a) from Appointment a where a.doctor.username=:username and a.status=:status and (a.date between :start and :end)")
    Integer countByDoctorUsername(String username, Status status, LocalDate start, LocalDate end);

    @Query("select a from Appointment a where a.doctor.username=:username and (a.date between :start and :end)")
    List<Appointment> findByDoctorUsernameInRange(String username, LocalDate start, LocalDate end);
}
