package pl.edu.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.edu.hospital.entity.Consultation;
import pl.edu.hospital.entity.enums.WorkingDay;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Integer> {
    List<Consultation> findAllByDoctorUsername(String username);

    @Query("select c from Consultation c where c.doctor.username=:username and c.workingDay=:day")
    Optional<Consultation> findByDoctorUsernameAndWorkingDay(String username, WorkingDay day);
}
