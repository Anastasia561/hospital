package pl.edu.hospital.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pl.edu.hospital.dto.AppointmentForPatientDto;
import pl.edu.hospital.dto.RecordForPatientDto;
import pl.edu.hospital.entity.enums.Specialization;
import pl.edu.hospital.entity.enums.Status;
import pl.edu.hospital.exception.DoctorNotFoundException;
import pl.edu.hospital.exception.PatientNotFoundException;
import pl.edu.hospital.exception.RecordNotFoundException;
import pl.edu.hospital.service.AppointmentService;
import pl.edu.hospital.service.PatientService;
import pl.edu.hospital.service.RecordService;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;

@Controller
@RequestMapping("/patient")
public class PatientController {
    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final RecordService recordService;

    public PatientController(PatientService patientService, AppointmentService appointmentService,
                             RecordService recordService) {
        this.patientService = patientService;
        this.appointmentService = appointmentService;
        this.recordService = recordService;
    }

    @GetMapping("/home")
    public String home() {
        return "patient_pages/patient_home";
    }

    @GetMapping("/appointments")
    public String getAppointmentsForDoctor(Model model) {
        //using authentication
        String username = "jdoe";
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

        //using authentication
        String username = "jdoe";

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

        return new RedirectView("appointments", true, false);
    }

    @PostMapping("/appointments/cancel")
    public RedirectView cancelAppointment(@RequestParam(name = "id") int appointmentId) {
        appointmentService.updateAppointmentStatus(Status.CANCELLED, appointmentId);
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
}
