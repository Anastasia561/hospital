package pl.edu.hospital.mapper;

import pl.edu.hospital.dto.AdminForProfileDto;
import pl.edu.hospital.entity.Admin;

public class AdminMapper {
    public static AdminForProfileDto adminForProfileDto(Admin admin) {
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
