package pl.edu.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.hospital.entity.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
}
