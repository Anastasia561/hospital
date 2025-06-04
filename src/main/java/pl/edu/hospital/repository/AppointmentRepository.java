package pl.edu.hospital.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.hospital.entity.Appointment;
import pl.edu.hospital.entity.enums.Status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends CrudRepository<Appointment, Integer> {

    @Query("select count(a) from Appointment a where a.doctor.username=:username and a.status=:status " +
            "and (a.date between :start and :end)")
    Integer countByDoctorUsername(String username, Status status, LocalDate start, LocalDate end);

    @Query("select a from Appointment a where a.doctor.username=:username and (a.date between :start and :end)")
    List<Appointment> findByDoctorUsernameInRange(String username, LocalDate start, LocalDate end);

    @Query("select a from Appointment a where a.patient.username=:username and (a.date between :start and :end)")
    List<Appointment> findByPatientUsernameInRange(String username, LocalDate start, LocalDate end);

    @Query("select a from Appointment a WHERE a.time between :start and :end and DAYOFWEEK(a.date) = :workingDay")
    List<Appointment> getAllDayOfWeekAndInTimeRange(int workingDay, LocalTime start, LocalTime end);

    @Query("select a from Appointment a WHERE (a.time not between :start and :end) and DAYOFWEEK(a.date) = :workingDay")
    List<Appointment> getAllDayOfWeekAndNotInTimeRange(int workingDay, LocalTime start, LocalTime end);

    @Query("select a from Appointment a where a.doctor.username=:username and a.date=:date and a.time" +
            " between :start and :end")
    List<Appointment> findForDoctorByDateAndTimeRange(String username, LocalDate date, LocalTime start, LocalTime end);
}
