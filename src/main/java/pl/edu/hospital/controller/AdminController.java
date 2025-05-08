package pl.edu.hospital.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.edu.hospital.dto.DoctorForAdminDto;
import pl.edu.hospital.dto.PatientForAdminDto;
import pl.edu.hospital.entity.enums.Status;
import pl.edu.hospital.service.AppointmentService;
import pl.edu.hospital.service.DoctorService;
import pl.edu.hospital.service.PatientService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AppointmentService appointmentService;
    private final PatientService patientService;
    private final DoctorService doctorService;

    public AdminController(AppointmentService appointmentService, PatientService patientService,
                           DoctorService doctorService) {
        this.appointmentService = appointmentService;
        this.patientService = patientService;
        this.doctorService = doctorService;
    }

    @GetMapping("/statistics")
    public String getStatistics(Model model) {
        Map<Status, Integer> statistics = appointmentService.getAppointmentStatisticsByDoctorUsername("rgarcia");
        model.addAttribute("statistics", statistics);
        return "admin_pages/admin_statistics";
    }

    @GetMapping("/patients")
    public String getPatients(Model model) {
        List<PatientForAdminDto> patients = patientService.getAllForAdmin();
        model.addAttribute("patients", patients);
        return "admin_pages/admin_patients";
    }

    @GetMapping("/doctors")
    public String getDoctors(Model model) {
        List<DoctorForAdminDto> doctors = doctorService.getAllForAdmin();
        model.addAttribute("doctors", doctors);
        return "admin_pages/admin_doctors";
    }
}
