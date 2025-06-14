package pl.edu.hospital.controller;

import jakarta.validation.groups.Default;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.edu.hospital.dto.appointment.AppointmentForPatientDto;
import pl.edu.hospital.dto.ConsultationDto;
import pl.edu.hospital.dto.doctor.DoctorForAdminDto;
import pl.edu.hospital.dto.patient.PatientForProfileDto;
import pl.edu.hospital.dto.record.RecordForPatientDto;
import pl.edu.hospital.entity.enums.Language;
import pl.edu.hospital.entity.enums.Specialization;
import pl.edu.hospital.entity.enums.Status;
import pl.edu.hospital.entity.enums.WorkingDay;
import pl.edu.hospital.exception.DoctorNotFoundException;
import pl.edu.hospital.exception.PatientNotFoundException;
import pl.edu.hospital.exception.RecordNotFoundException;
import pl.edu.hospital.service.AppointmentService;
import pl.edu.hospital.service.ConsultationService;
import pl.edu.hospital.service.DoctorService;
import pl.edu.hospital.service.PatientService;
import pl.edu.hospital.service.RecordService;
import pl.edu.hospital.validation.OnUpdate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/patient")
public class PatientController {
    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final RecordService recordService;
    private final DoctorService doctorService;
    private final ConsultationService consultationService;

    public PatientController(PatientService patientService, AppointmentService appointmentService,
                             RecordService recordService, DoctorService doctorService,
                             ConsultationService consultationService) {
        this.patientService = patientService;
        this.appointmentService = appointmentService;
        this.recordService = recordService;
        this.doctorService = doctorService;
        this.consultationService = consultationService;
    }

    @GetMapping("/home")
    public String home() {
        return "patient_pages/patient_home";
    }

    @GetMapping("/appointments")
    public String getAppointmentsForDoctor(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            model.addAttribute("pFullName", patientService.getPatientFullNameByUsername(username));
            model.addAttribute("statuses", Status.values());
            model.addAttribute("specializations", Specialization.values());
        } catch (DoctorNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "patient_pages/patient_appointments";
    }

    @PostMapping("/apps")
    public RedirectView getAppointmentsForPatientInRange(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                         LocalDate startDate,
                                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                         LocalDate endDate,
                                                         @RequestParam(required = false) Status status,
                                                         @RequestParam(required = false) Specialization specialization,
                                                         RedirectAttributes redirectAttributes) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        LinkedHashMap<LocalDate, List<AppointmentForPatientDto>> appointments =
                appointmentService.getAppointmentsForPatientFiltered(username, startDate, endDate, status, specialization);

        redirectAttributes.addFlashAttribute("pFullName", patientService.getPatientFullNameByUsername(username));
        redirectAttributes.addFlashAttribute("username", username);
        redirectAttributes.addFlashAttribute("endDate", endDate);
        redirectAttributes.addFlashAttribute("startDate", startDate);
        redirectAttributes.addFlashAttribute("selectedStatus", status);
        redirectAttributes.addFlashAttribute("selectedSpecialization", specialization);
        if (startDate.isAfter(endDate)) {
            redirectAttributes.addFlashAttribute("errorMessage", "pl.edu.hospital.failure.date");
        } else if (appointments.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "pl.edu.hospital.failure.noData");
        } else {
            redirectAttributes.addFlashAttribute("appointments", appointments);
        }
        return new RedirectView("appointments", true, false);
    }

    @PostMapping("/appointments/cancel")
    public RedirectView cancelAppointment(@RequestParam(name = "id") int appointmentId,
                                          RedirectAttributes redirectAttributes) {
        try {
            appointmentService.updateAppointmentStatus(Status.CANCELLED, appointmentId, false);
            redirectAttributes.addFlashAttribute("successMessage", "pl.edu.hospital.success.appCancel");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return new RedirectView("/patient/appointments", true, false);
    }

    @GetMapping("/appointments/record/{appId}")
    public String showRecordForm(@PathVariable int appId, Model model) {
        try {
            RecordForPatientDto record = recordService.getRecordForPatientByAppointmentId(appId);
            model.addAttribute("record", record);
        } catch (PatientNotFoundException | RecordNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "patient_pages/patient_record";
    }

    @GetMapping("/doctors/book/{username}")
    public Object showBookingPage(@PathVariable String username,
                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                  RedirectAttributes redirectAttributes, Model model) {

        redirectAttributes.addFlashAttribute("selectedDate", date);
        if (date.isBefore(LocalDate.now())) {
            redirectAttributes.addFlashAttribute("errorMessage", "pl.edu.hospital.failure.appPast");
            return new RedirectView("/patient/doctors", true, false);
        } else {
            DoctorForAdminDto dto = doctorService.findByUsernameForAdminDto(username);
            List<LocalTime> timeSlots;
            try {
                timeSlots = appointmentService.getAvailableTimeSlotsForDoctorByUsername(username, date);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "pl.edu.hospital.failure.docAval");
                return new RedirectView("/patient/doctors", true, false);
            }
            model.addAttribute("doctor", dto);
            model.addAttribute("date", date);
            model.addAttribute("availableSlots", timeSlots);
            return "patient_pages/patient_appointment_booking";
        }
    }

    @PostMapping("/appointments/book")
    public RedirectView bookAppointment(@RequestParam String doctorUsername,
                                        @RequestParam LocalDate date,
                                        @RequestParam LocalTime time,
                                        RedirectAttributes redirectAttributes) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String patientUsername = authentication.getName();

        try {
            appointmentService.createAppointment(patientUsername, doctorUsername, date, time);
            redirectAttributes.addFlashAttribute("successMessage", "pl.edu.hospital.success.appBook");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return new RedirectView("/patient/doctors", true, false);
    }

    @GetMapping("/doctors")
    public String getDoctors(@RequestParam(required = false) String specialization, Model model) {
        List<DoctorForAdminDto> doctors = (specialization == null || specialization.isBlank())
                ? doctorService.getAllForAdmin()
                : doctorService.getAllBySpecialization(Specialization.valueOf(specialization));

        if (doctors.isEmpty()) {
            model.addAttribute("errorMessage", "pl.edu.hospital.failure.noData");
        }

        model.addAttribute("specializations", Specialization.values());
        model.addAttribute("selectedSpecialization", specialization);
        model.addAttribute("doctors", doctors);
        return "patient_pages/patient_doctors";
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
        return "patient_pages/patient_doctor_schedule";
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        PatientForProfileDto dto = patientService.getPatientByUsername(username);
        model.addAttribute("patient", dto);
        model.addAttribute("editMode", false);
        return "patient_pages/patient_profile";
    }


    @GetMapping("/profile/edit")
    public String editProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        PatientForProfileDto dto = patientService.getPatientByUsername(username);
        model.addAttribute("patient", dto);
        model.addAttribute("languages", Language.values());
        model.addAttribute("editMode", true);
        return "patient_pages/patient_profile";
    }

    @PostMapping("/profile/update")
    public Object updateProfile(@Validated({Default.class, OnUpdate.class}) @ModelAttribute("patient") PatientForProfileDto patient,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getModel().values());
            model.addAttribute("languages", Language.values());
            model.addAttribute("editMode", true);
            return "patient_pages/patient_profile";
        }
        patientService.updatePatient(patient);
        return new RedirectView("/patient/profile", true, false);
    }
}
