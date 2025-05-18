package pl.edu.hospital.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.edu.hospital.dto.AdminForProfileDto;
import pl.edu.hospital.dto.appointment.AppointmentForDoctorDto;
import pl.edu.hospital.dto.appointment.AppointmentForPatientDto;
import pl.edu.hospital.dto.ConsultationDto;
import pl.edu.hospital.dto.doctor.DoctorForAdminDto;
import pl.edu.hospital.dto.doctor.DoctorRegistrationDto;
import pl.edu.hospital.dto.patient.PatientForAdminDto;
import pl.edu.hospital.entity.enums.Language;
import pl.edu.hospital.entity.enums.Specialization;
import pl.edu.hospital.entity.enums.Status;
import pl.edu.hospital.entity.enums.WorkingDay;
import pl.edu.hospital.exception.DoctorNotFoundException;
import pl.edu.hospital.exception.PatientNotFoundException;
import pl.edu.hospital.service.AdminService;
import pl.edu.hospital.service.AppointmentService;
import pl.edu.hospital.service.ConsultationService;
import pl.edu.hospital.service.DoctorService;
import pl.edu.hospital.service.PatientService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AppointmentService appointmentService;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final ConsultationService consultationService;
    private final AdminService adminService;

    public AdminController(AppointmentService appointmentService, PatientService patientService,
                           DoctorService doctorService, ConsultationService consultationService, AdminService adminService) {
        this.appointmentService = appointmentService;
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.consultationService = consultationService;
        this.adminService = adminService;
    }

    @GetMapping("/home")
    public String home() {
        return "admin_pages/admin_home";
    }

    @GetMapping("/info")
    public String getStatistics(Model model) {
        if (doctorService.getAllForAdmin().isEmpty()) {
            model.addAttribute("errorMessage", "No data found");
        }
        model.addAttribute("doctors", doctorService.getAllForAdmin());
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
        return new RedirectView("info", true, false);
    }

    @GetMapping("/patients")
    public String getPatients(@RequestParam(required = false) String email, Model model) {
        List<PatientForAdminDto> patients = (email == null || email.isBlank())
                ? patientService.getAllForAdmin()
                : patientService.getAllForAdminByEmail(email);
        if (patients.isEmpty()) {
            model.addAttribute("errorMessage", "No data found");
        }
        model.addAttribute("patients", patients);
        return "admin_pages/admin_patients";
    }

    @GetMapping("/doctors")
    public String getDoctors(@RequestParam(required = false) String specialization, Model model) {
        List<DoctorForAdminDto> doctors = (specialization == null || specialization.isBlank())
                ? doctorService.getAllForAdmin()
                : doctorService.getAllBySpecialization(Specialization.valueOf(specialization));

        if (doctors.isEmpty()) {
            model.addAttribute("errorMessage", "No data found");
        }

        model.addAttribute("specializations", Specialization.values());
        model.addAttribute("selectedSpecialization", specialization);
        model.addAttribute("doctors", doctors);
        return "admin_pages/admin_doctors";
    }

    @PostMapping("/consultations/edit")
    public RedirectView saveEditedConsultation(@ModelAttribute ConsultationDto consultation,
                                               RedirectAttributes redirectAttributes) {
        if (consultation.getStartTime().isAfter(consultation.getEndTime()) ||
                consultation.getStartTime().equals(consultation.getEndTime())) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Invalid time range for consultation hours");
        } else {
            consultationService.updateConsultation(consultation);
            appointmentService.cancelForUpdatedConsultation(consultation.getId());
            redirectAttributes.addFlashAttribute("successMessage", "Consultation updated successfully");
        }
        return new RedirectView("/admin/doctors/" + consultation.getDoctorUsername() + "/schedule",
                true, false);
    }

    @PostMapping("/consultations/create")
    public RedirectView createConsultation(@ModelAttribute ConsultationDto consultation,
                                           RedirectAttributes redirectAttributes) {
        if (consultation.getStartTime().isAfter(consultation.getEndTime()) ||
                consultation.getStartTime().equals(consultation.getEndTime())) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Invalid time range for consultation hours");
        } else {
            consultationService.createConsultation(consultation);
            redirectAttributes.addFlashAttribute("successMessage", "Consultation created successfully");
        }
        return new RedirectView("/admin/doctors/" + consultation.getDoctorUsername() + "/schedule",
                true, false);
    }

    @PostMapping("/consultations/delete")
    public RedirectView deleteConsultation(@RequestParam int id, @RequestParam String doctorUsername,
                                           RedirectAttributes redirectAttributes) {
        try {
            appointmentService.cancelForDeletedConsultation(id);
            consultationService.deleteConsultation(id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("successMessage", "Consultation deleted successfully");
        }

        return new RedirectView("/admin/doctors/" + doctorUsername + "/schedule", true, false);
    }

    @PostMapping("/doctors/{username}/apps")
    public RedirectView getAppointmentsForDoctorInRange(@PathVariable String username,
                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                        @RequestParam(required = false) Status status,
                                                        RedirectAttributes redirectAttributes) {

        LinkedHashMap<LocalDate, List<AppointmentForDoctorDto>> appointments =
                appointmentService.getAppointmentsForDoctorFiltered(username, startDate, endDate, status);

        redirectAttributes.addFlashAttribute("dFullName", doctorService.getDoctorFullNameByUsername(username));
        redirectAttributes.addFlashAttribute("username", username);
        if (startDate.isAfter(endDate)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid date range");
        } else if (appointments.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Not data found");
        } else {
            redirectAttributes.addFlashAttribute("appointments", appointments);
        }

        return new RedirectView("/admin/doctors/" + username + "/appointments", true, false);
    }

    @GetMapping("/doctors/{username}/appointments")
    public String getAppointmentsForDoctor(@PathVariable String username, Model model) {
        try {
            model.addAttribute("dFullName", doctorService.getDoctorFullNameByUsername(username));
            model.addAttribute("statuses", Status.values());
        } catch (DoctorNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "admin_pages/admin_doctor_appointment";
    }

    @PostMapping("/patients/{username}/apps")
    public RedirectView getAppointmentsForPatientInRange(@PathVariable String username,
                                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                         LocalDate startDate,
                                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                         LocalDate endDate,
                                                         @RequestParam(required = false) Status status,
                                                         @RequestParam(required = false) Specialization specialization,
                                                         RedirectAttributes redirectAttributes) {

        LinkedHashMap<LocalDate, List<AppointmentForPatientDto>> appointments =
                appointmentService.getAppointmentsForPatientFiltered(username, startDate, endDate, status, specialization);

        redirectAttributes.addFlashAttribute("pFullName", patientService.getPatientFullNameByUsername(username));
        redirectAttributes.addFlashAttribute("username", username);
        if (startDate.isAfter(endDate)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid date range");
        } else if (appointments.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Not data found");
        } else {
            redirectAttributes.addFlashAttribute("appointments", appointments);
        }

        return new RedirectView("/admin/patients/" + username + "/appointments", true, false);
    }

    @GetMapping("/patients/{username}/appointments")
    public String getAppointmentsForPatient(@PathVariable String username, Model model) {
        try {
            model.addAttribute("specializations", Specialization.values());
            model.addAttribute("statuses", Status.values());
            model.addAttribute("pFullName", patientService.getPatientFullNameByUsername(username));
        } catch (PatientNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "admin_pages/admin_patient_appointment";
    }

    @GetMapping("/doctors/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("doctorDto", new DoctorRegistrationDto());
        model.addAttribute("languages", Language.values());
        model.addAttribute("specializations", Specialization.values());
        return "admin_pages/admin_doctor_registration";
    }

    @PostMapping("/doctors/form")
    public RedirectView registerDoctor(@ModelAttribute("doctorDto") DoctorRegistrationDto dto,
                                       RedirectAttributes redirectAttributes) {
        //validation
        doctorService.createDoctor(dto);
//        System.out.println(dto);

        redirectAttributes.addFlashAttribute("successMessage", "Doctor registered successfully");
        return new RedirectView("/admin/doctors", true, false);
    }

    @GetMapping("/doctors/{username}/schedule")
    public String getDoctorSchedule(@PathVariable String username, Model model) {
        try {
            String dFullName = doctorService.getDoctorFullNameByUsername(username);
            Map<WorkingDay, List<ConsultationDto>> scheduleByDay = consultationService
                    .getAllByDoctorUsername(username).stream()
                    .collect(Collectors.groupingBy(ConsultationDto::getDay, TreeMap::new, Collectors.toList()));

            model.addAttribute("days", WorkingDay.values());
            List<WorkingDay> freeDays = Arrays.stream(WorkingDay.values()).filter(d -> !scheduleByDay.containsKey(d)).toList();
            model.addAttribute("freeDays", freeDays);
            model.addAttribute("username", username);
            model.addAttribute("dFullName", dFullName);
            model.addAttribute("scheduleByDay", scheduleByDay);

        } catch (DoctorNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "admin_pages/admin_doctor_schedule";
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        //using authentication
        String username = "pscott";
        AdminForProfileDto dto = adminService.findByUsernameForProfileDto(username);
        model.addAttribute("admin", dto);
        model.addAttribute("editMode", false);
        return "admin_pages/admin_profile";
    }


    @GetMapping("/profile/edit")
    public String editProfile(Model model) {
        //using authentication
        String username = "pscott";
        AdminForProfileDto dto = adminService.findByUsernameForProfileDto(username);
        model.addAttribute("admin", dto);
        model.addAttribute("languages", Language.values());
        model.addAttribute("editMode", true);
        return "admin_pages/admin_profile";
    }

    @PostMapping("/profile/update")
    public RedirectView updateProfile(@ModelAttribute AdminForProfileDto dto) {
        adminService.updateAdmin(dto);
        return new RedirectView("/admin/profile", true, false);
    }
}
