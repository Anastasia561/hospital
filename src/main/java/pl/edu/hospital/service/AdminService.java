package pl.edu.hospital.service;

import org.springframework.stereotype.Service;
import pl.edu.hospital.dto.AdminDto;
import pl.edu.hospital.mapper.AdminMapper;
import pl.edu.hospital.repository.AdminRepository;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public AdminDto getAdminByUsername(String username) {
        return AdminMapper.toAdminDto(adminRepository.getAdminByUsername(username));
    }
}
