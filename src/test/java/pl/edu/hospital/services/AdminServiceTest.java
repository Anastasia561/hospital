package pl.edu.hospital.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.hospital.dto.AdminForProfileDto;
import pl.edu.hospital.entity.Admin;
import pl.edu.hospital.entity.enums.Language;
import pl.edu.hospital.exception.AdminNotFoundException;
import pl.edu.hospital.mapper.AdminMapper;
import pl.edu.hospital.repository.AdminRepository;
import pl.edu.hospital.service.AdminService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;
    @Mock
    private AdminMapper adminMapper;

    @InjectMocks
    private AdminService adminService;

    @Test
    void findByUsernameForProfileDtoTest_shouldReturnAdminDtoWhenUsernameExists() {
        String username = "adminUser";
        Admin admin = new Admin();
        admin.setUsername(username);
        AdminForProfileDto dto = new AdminForProfileDto();
        dto.setUsername(username);

        when(adminRepository.findByUsername(username)).thenReturn(Optional.of(admin));
        when(adminMapper.adminForProfileDto(admin)).thenReturn(dto);

        AdminForProfileDto result = adminService.findByUsernameForProfileDto(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(adminRepository).findByUsername(username);
        verify(adminMapper).adminForProfileDto(admin);
    }

    @Test
    void findByUsernameForProfileDtoTest_shouldThrowExceptionWhenUsernameNotFound() {
        String username = "unknownUser";
        when(adminRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(AdminNotFoundException.class, () -> {
            adminService.findByUsernameForProfileDto(username);
        });
        verify(adminRepository).findByUsername(username);
    }

    @Test
    void updateAdminTest_shouldUpdateAdminFields() {
        String username = "adminUser";
        Admin admin = new Admin();
        admin.setUsername(username);

        AdminForProfileDto dto = new AdminForProfileDto();
        dto.setUsername(username);
        dto.setFirstName("Anna");
        dto.setLastName("Kowalski");
        dto.setEmail("anna@example.com");
        dto.setLanguage(Language.POLISH);

        when(adminRepository.findByUsername(username)).thenReturn(Optional.of(admin));

        adminService.updateAdmin(dto);

        assertEquals("Anna", admin.getFirstName());
        assertEquals("Kowalski", admin.getLastName());
        assertEquals("anna@example.com", admin.getEmail());
        assertEquals(Language.POLISH, admin.getLanguage());

        verify(adminRepository).findByUsername(username);
        verify(adminRepository).save(admin);
    }

    @Test
    void updateAdmin_shouldThrowWhenUpdatingNonExistingAdmin() {
        AdminForProfileDto dto = new AdminForProfileDto();
        dto.setUsername("nonexistent");

        when(adminRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(AdminNotFoundException.class, () -> adminService.updateAdmin(dto));
        verify(adminRepository).findByUsername("nonexistent");
    }
}
