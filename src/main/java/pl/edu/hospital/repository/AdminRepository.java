package pl.edu.hospital.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.edu.hospital.entity.Admin;

import java.util.Optional;

@Repository
public interface AdminRepository extends CrudRepository<Admin, Integer> {
    Optional<Admin> findByUsername(String username);
}
