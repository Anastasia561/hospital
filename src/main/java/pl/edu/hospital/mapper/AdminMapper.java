package pl.edu.hospital.mapper;

import org.springframework.stereotype.Component;
import pl.edu.hospital.dto.AdminForProfileDto;
import pl.edu.hospital.entity.Admin;

@Component
public class AdminMapper {
    public AdminForProfileDto adminForProfileDto(Admin admin) {
        AdminForProfileDto dto = new AdminForProfileDto();
        dto.setFirstName(admin.getFirstName());
        dto.setLastName(admin.getLastName());
        dto.setUsername(admin.getUsername());
        dto.setLanguage(admin.getLanguage());
        dto.setEmail(admin.getEmail());
        dto.setEmploymentDate(admin.getEmploymentDate());
        return dto;
    }
}
