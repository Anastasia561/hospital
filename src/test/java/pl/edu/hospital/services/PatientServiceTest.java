package pl.edu.hospital.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.edu.hospital.dto.patient.PatientForAdminDto;
import pl.edu.hospital.dto.patient.PatientForProfileDto;
import pl.edu.hospital.dto.patient.PatientForRecordDto;
import pl.edu.hospital.entity.Address;
import pl.edu.hospital.entity.City;
import pl.edu.hospital.entity.Country;
import pl.edu.hospital.entity.Patient;
import pl.edu.hospital.entity.enums.Language;
import pl.edu.hospital.exception.PatientNotFoundException;
import pl.edu.hospital.mapper.PatientMapper;
import pl.edu.hospital.repository.PatientRepository;
import pl.edu.hospital.service.PatientService;

import java.time.LocalDate;
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
public class PatientServiceTest {
    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientService patientService;

    @Test
    void getAllForAdminTest() {
        Patient p1 = new Patient();
        Patient p2 = new Patient();
        PatientForAdminDto dto1 = new PatientForAdminDto();
        PatientForAdminDto dto2 = new PatientForAdminDto();

        when(patientRepository.findAll()).thenReturn(List.of(p1, p2));
        when(patientMapper.toPatientForAdminDto(p1)).thenReturn(dto1);
        when(patientMapper.toPatientForAdminDto(p2)).thenReturn(dto2);

        List<PatientForAdminDto> result = patientService.getAllForAdmin();

        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));
        verify(patientRepository).findAll();
        verify(patientMapper, times(2)).toPatientForAdminDto(any(Patient.class));
    }

    @Test
    void getPatientFullNameByUsernameTest_shouldReturnFullName() {
        String username = "patientUser";
        Patient patient = new Patient();
        patient.setFirstName("Alice");
        patient.setLastName("Wonderland");

        when(patientRepository.findByUsername(username)).thenReturn(Optional.of(patient));

        String fullName = patientService.getPatientFullNameByUsername(username);

        assertEquals("Alice Wonderland", fullName);
    }

    @Test
    void getPatientFullNameByUsernameTest_shouldThrowExceptionWhenNotFound() {
        String username = "unknown";
        when(patientRepository.findByUsername(username)).thenReturn(Optional.empty());
        assertThrows(PatientNotFoundException.class, () -> patientService.getPatientFullNameByUsername(username));
    }

    @Test
    void getAllForAdminByEmailTest() {
        String email = "test@example.com";
        Patient p1 = new Patient();
        PatientForAdminDto dto1 = new PatientForAdminDto();

        when(patientRepository.getPatientsByEmail(email)).thenReturn(List.of(p1));
        when(patientMapper.toPatientForAdminDto(p1)).thenReturn(dto1);

        List<PatientForAdminDto> result = patientService.getAllForAdminByEmail(email);

        assertEquals(1, result.size());
        assertEquals(dto1, result.get(0));
    }

    @Test
    void getPatientByAppointmentIdTest_shouldReturnDto() {
        int appId = 123;
        Patient patient = new Patient();
        PatientForRecordDto dto = new PatientForRecordDto();

        when(patientRepository.findPatientByAppointmentId(appId)).thenReturn(Optional.of(patient));
        when(patientMapper.toPatientForRecordDto(patient)).thenReturn(dto);

        PatientForRecordDto result = patientService.getPatientByAppointmentId(appId);

        assertEquals(dto, result);
    }

    @Test
    void getPatientByAppointmentIdTest_shouldThrowExceptionWhenNotFound() {
        int appId = 999;

        when(patientRepository.findPatientByAppointmentId(appId)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> patientService.getPatientByAppointmentId(appId));
    }

    @Test
    void getPatientByUsernameTest_shouldReturnDto() {
        String username = "patientUser";
        Patient patient = new Patient();
        PatientForProfileDto dto = new PatientForProfileDto();

        when(patientRepository.findByUsername(username)).thenReturn(Optional.of(patient));
        when(patientMapper.toPatientForProfileDto(patient)).thenReturn(dto);

        PatientForProfileDto result = patientService.getPatientByUsername(username);

        assertEquals(dto, result);
    }

    @Test
    void getPatientByUsernameTest_shouldThrowExceptionWhenNotFound() {
        String username = "unknown";

        when(patientRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> patientService.getPatientByUsername(username));
    }

    @Test
    void updatePatientTest_shouldUpdateFields() {
        PatientForProfileDto dto = new PatientForProfileDto();
        dto.setUsername("patientUser");
        dto.setFirstName("Bob");
        dto.setLastName("Builder");
        dto.setEmail("bob@example.com");
        dto.setLanguage(Language.ENGLISH);
        dto.setBirthDate(LocalDate.of(1990, 1, 1));
        dto.setPhoneNumber("1234567890");
        dto.setNumber(12);
        dto.setStreet("Main St");
        dto.setCity("CityName");
        dto.setCountry("CountryName");

        Patient patient = new Patient();
        patient.setUsername("patientUser");
        patient.setAddress(new Address());
        patient.getAddress().setCity(new City());
        patient.getAddress().getCity().setCountry(new Country());

        when(patientRepository.findByUsername(dto.getUsername())).thenReturn(Optional.of(patient));

        patientService.updatePatient(dto);

        assertEquals("Bob", patient.getFirstName());
        assertEquals("Builder", patient.getLastName());
        assertEquals("bob@example.com", patient.getEmail());
        assertEquals(Language.ENGLISH, patient.getLanguage());
        assertEquals(LocalDate.of(1990, 1, 1), patient.getBirthDate());
        assertEquals("1234567890", patient.getPhoneNumber());

        assertEquals(12, patient.getAddress().getNumber());
        assertEquals("Main St", patient.getAddress().getStreet());
        assertEquals("CityName", patient.getAddress().getCity().getName());
        assertEquals("CountryName", patient.getAddress().getCity().getCountry().getName());

        verify(patientRepository).save(patient);
    }

    @Test
    void updatePatientTest_shouldThrowExceptionWhenNotFound() {
        PatientForProfileDto dto = new PatientForProfileDto();
        dto.setUsername("unknown");

        when(patientRepository.findByUsername(dto.getUsername())).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> patientService.updatePatient(dto));
    }

    @Test
    void registerPatientTes() {
        PatientForProfileDto dto = new PatientForProfileDto();
        Patient patient = new Patient();

        when(patientMapper.toPatient(dto)).thenReturn(patient);

        patientService.registerPatient(dto);

        verify(patientMapper).toPatient(dto);
        verify(patientRepository).save(patient);
    }
}
