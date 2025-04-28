package pl.edu.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.hospital.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
}
