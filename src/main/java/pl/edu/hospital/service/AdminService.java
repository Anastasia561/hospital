package pl.edu.hospital.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.hospital.dto.AdminForProfileDto;
import pl.edu.hospital.entity.Admin;
import pl.edu.hospital.exception.AdminNotFoundException;
import pl.edu.hospital.mapper.AdminMapper;
import pl.edu.hospital.repository.AdminRepository;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;

    public AdminService(AdminRepository adminRepository, AdminMapper adminMapper) {
        this.adminRepository = adminRepository;
        this.adminMapper = adminMapper;
    }

    public AdminForProfileDto findByUsernameForProfileDto(String username) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new AdminNotFoundException(username));
        return adminMapper.adminForProfileDto(admin);
    }

    @Transactional
    public void updateAdmin(AdminForProfileDto dto) {
        Admin admin = adminRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new AdminNotFoundException(dto.getUsername()));

        admin.setFirstName(dto.getFirstName());
        admin.setLanguage(dto.getLanguage());
        admin.setLastName(dto.getLastName());
        admin.setEmail(dto.getEmail());

        adminRepository.save(admin);
    }
}
