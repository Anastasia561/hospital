package pl.edu.hospital.mapper;

import pl.edu.hospital.dto.AdminDto;
import pl.edu.hospital.entity.Admin;

public class AdminMapper {
    public static AdminDto toAdminDto(Admin admin) {
        AdminDto dto = new AdminDto();
        dto.setUsername(admin.getUsername());
        dto.setFirstName(admin.getFirstName());
        dto.setLastName(admin.getLastName());
        dto.setEmail(admin.getEmail());
        dto.setEmploymentDate(admin.getEmploymentDate());
        dto.setLanguage(admin.getLanguage());
        return dto;
    }
}
