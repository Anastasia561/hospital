package pl.edu.hospital.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.edu.hospital.dto.DoctorForAdminDto;
import pl.edu.hospital.dto.PatientForAdminDto;
import pl.edu.hospital.entity.enums.Status;
import pl.edu.hospital.service.AdminService;
import pl.edu.hospital.service.AppointmentService;
import pl.edu.hospital.service.DoctorService;
import pl.edu.hospital.service.PatientService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AppointmentService appointmentService;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final AdminService adminService;

    public AdminController(AppointmentService appointmentService, PatientService patientService,
                           DoctorService doctorService, AdminService adminService) {
        this.appointmentService = appointmentService;
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.adminService = adminService;
    }

    @GetMapping("/info")
    public String getStatistics(Model model) {
        model.addAttribute("doctors", doctorService.getAllForAdmin()); // list of doctors
        return "admin_pages/admin_statistics";
    }

    @PostMapping("/statistics")
    public RedirectView postStatistics(@RequestParam String data,
                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                       RedirectAttributes redirectAttributes) {

        List<DoctorForAdminDto> doctors = doctorService.getAllForAdmin();
        redirectAttributes.addFlashAttribute("doctors", doctors);

        if (startDate.isAfter(endDate)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid date range");
        } else {
            String dUsername = data.split(" ")[0];
            String dFullName = data.split(" ", 2)[1];
            redirectAttributes.addFlashAttribute("doctorFullName", dFullName);
            redirectAttributes.addFlashAttribute("startDate", startDate);
            redirectAttributes.addFlashAttribute("endDate", endDate);

            Map<Status, Integer> statistics = appointmentService.getStatisticsForDoctorAndPeriod(dUsername, startDate, endDate);
            redirectAttributes.addFlashAttribute("statistics", statistics);
        }
        return new RedirectView("info");
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
