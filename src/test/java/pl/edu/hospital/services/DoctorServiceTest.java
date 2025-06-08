package pl.edu.hospital.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.hospital.dto.doctor.DoctorForAdminDto;
import pl.edu.hospital.dto.doctor.DoctorForProfileDto;
import pl.edu.hospital.dto.doctor.DoctorRegistrationDto;
import pl.edu.hospital.entity.Doctor;
import pl.edu.hospital.entity.enums.Language;
import pl.edu.hospital.entity.enums.Specialization;
import pl.edu.hospital.exception.DoctorNotFoundException;
import pl.edu.hospital.mapper.DoctorMapper;
import pl.edu.hospital.repository.DoctorRepository;
import pl.edu.hospital.service.DoctorService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorMapper doctorMapper;

    @InjectMocks
    private DoctorService doctorService;

    @Test
    void getAllForAdminTest() {
        Doctor d1 = new Doctor();
        Doctor d2 = new Doctor();
        DoctorForAdminDto dto1 = new DoctorForAdminDto();
        DoctorForAdminDto dto2 = new DoctorForAdminDto();

        when(doctorRepository.findAll()).thenReturn(List.of(d1, d2));
        when(doctorMapper.toDoctorForAdminDto(d1)).thenReturn(dto1);
        when(doctorMapper.toDoctorForAdminDto(d2)).thenReturn(dto2);

        List<DoctorForAdminDto> result = doctorService.getAllForAdmin();

        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));
        verify(doctorRepository).findAll();
        verify(doctorMapper, times(2)).toDoctorForAdminDto(any(Doctor.class));
    }

    @Test
    void getDoctorFullNameByUsernameTest_shouldReturnDto() {
        String username = "docUser";
        Doctor doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");

        when(doctorRepository.findByUsername(username)).thenReturn(Optional.of(doctor));

        String fullName = doctorService.getDoctorFullNameByUsername(username);

        assertEquals("John Doe", fullName);
    }

    @Test
    void getDoctorFullNameByUsername_shouldThrowNotFoundException() {
        String username = "unknown";

        when(doctorRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class, () -> doctorService.getDoctorFullNameByUsername(username));
    }

    @Test
    void getAllBySpecializationTest() {
        Specialization specialization = Specialization.CARDIOLOGIST;
        Doctor d1 = new Doctor();
        DoctorForAdminDto dto1 = new DoctorForAdminDto();

        when(doctorRepository.findAllBySpecialization(specialization)).thenReturn(List.of(d1));
        when(doctorMapper.toDoctorForAdminDto(d1)).thenReturn(dto1);

        List<DoctorForAdminDto> result = doctorService.getAllBySpecialization(specialization);

        assertEquals(1, result.size());
        assertEquals(dto1, result.get(0));
    }

    @Test
    void createDoctorTest() {
        DoctorRegistrationDto dto = new DoctorRegistrationDto();
        Doctor doctor = new Doctor();

        when(doctorMapper.toDoctor(dto)).thenReturn(doctor);

        doctorService.createDoctor(dto);

        verify(doctorMapper).toDoctor(dto);
        verify(doctorRepository).save(doctor);
    }

    @Test
    void findByUsernameForAdminDto_returnsMappedDto() {
        String username = "docUser";
        Doctor doctor = new Doctor();
        DoctorForAdminDto dto = new DoctorForAdminDto();

        when(doctorRepository.findByUsername(username)).thenReturn(Optional.of(doctor));
        when(doctorMapper.toDoctorForAdminDto(doctor)).thenReturn(dto);

        DoctorForAdminDto result = doctorService.findByUsernameForAdminDto(username);

        assertEquals(dto, result);
    }

    @Test
    void findByUsernameForAdminDto_throwsWhenNotFound() {
        String username = "unknown";

        when(doctorRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class, () -> doctorService.findByUsernameForAdminDto(username));
    }

    @Test
    void findByUsernameForProfileDtoTest_shouldReturnDto() {
        String username = "docUser";
        Doctor doctor = new Doctor();
        DoctorForProfileDto dto = new DoctorForProfileDto();

        when(doctorRepository.findByUsername(username)).thenReturn(Optional.of(doctor));
        when(doctorMapper.toDoctorForProfileDto(doctor)).thenReturn(dto);

        DoctorForProfileDto result = doctorService.findByUsernameForProfileDto(username);

        assertEquals(dto, result);
    }

    @Test
    void findByUsernameForProfileDtoTest_shouldThrowExceptionWhenNotFound() {
        String username = "unknown";

        when(doctorRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class, () -> doctorService.findByUsernameForProfileDto(username));
    }

    @Test
    void updateDoctorTest_shouldUpdateFields() {
        DoctorForProfileDto dto = new DoctorForProfileDto();
        dto.setUsername("docUser");
        dto.setFirstName("Jane");
        dto.setLastName("Smith");
        dto.setEmail("jane@example.com");
        dto.setLanguage(Language.ENGLISH);

        Doctor doctor = new Doctor();
        doctor.setUsername("docUser");

        when(doctorRepository.findByUsername(dto.getUsername())).thenReturn(Optional.of(doctor));

        doctorService.updateDoctor(dto);

        assertEquals("Jane", doctor.getFirstName());
        assertEquals("Smith", doctor.getLastName());
        assertEquals("jane@example.com", doctor.getEmail());
        assertEquals(Language.ENGLISH, doctor.getLanguage());

        verify(doctorRepository).save(doctor);
    }

    @Test
    void updateDoctorTest_shouldThrowExceptionWhenDoctorNotFound() {
        DoctorForProfileDto dto = new DoctorForProfileDto();
        dto.setUsername("unknown");

        when(doctorRepository.findByUsername(dto.getUsername())).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class, () -> doctorService.updateDoctor(dto));
    }
}
