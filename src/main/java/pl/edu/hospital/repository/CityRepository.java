package pl.edu.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.hospital.entity.City;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
}
