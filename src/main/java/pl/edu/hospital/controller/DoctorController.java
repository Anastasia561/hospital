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
import pl.edu.hospital.dto.AppointmentForDoctorDto;
import pl.edu.hospital.dto.ConsultationDto;
import pl.edu.hospital.dto.PatientForRecordDto;
import pl.edu.hospital.dto.RecordCreationRequestDto;
import pl.edu.hospital.dto.RecordForDoctorDto;
import pl.edu.hospital.entity.enums.Frequency;
import pl.edu.hospital.entity.enums.Status;
import pl.edu.hospital.entity.enums.WorkingDay;
import pl.edu.hospital.exception.AppointmentNotFoundException;
import pl.edu.hospital.exception.DoctorNotFoundException;
import pl.edu.hospital.exception.PatientNotFoundException;
import pl.edu.hospital.exception.RecordNotFoundException;
import pl.edu.hospital.service.AppointmentService;
import pl.edu.hospital.service.ConsultationService;
import pl.edu.hospital.service.DoctorService;
import pl.edu.hospital.service.PatientService;
import pl.edu.hospital.service.RecordService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/doctor")
public class DoctorController {
    private final DoctorService doctorService;
    private final ConsultationService consultationService;
    private final AppointmentService appointmentService;
    private final RecordService recordService;
    private final PatientService patientService;

    public DoctorController(DoctorService doctorService, ConsultationService consultationService,
                            AppointmentService appointmentService, RecordService recordService, PatientService patientService) {
        this.doctorService = doctorService;
        this.consultationService = consultationService;
        this.appointmentService = appointmentService;
        this.recordService = recordService;
        this.patientService = patientService;
    }

    @GetMapping("/home")
    public String home() {
        return "doctor_pages/doctor_home";
    }

    @GetMapping("/schedule")
    public String getDoctorSchedule(Model model) {
        try {
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            String username = auth.getName();
//            --using security authentication

            String username = "rgarcia";

            Map<WorkingDay, List<ConsultationDto>> scheduleByDay = consultationService
                    .getAllByDoctorUsername(username).stream()
                    .collect(Collectors.groupingBy(ConsultationDto::getDay, TreeMap::new, Collectors.toList()));

            model.addAttribute("days", WorkingDay.values());
            List<WorkingDay> freeDays = Arrays.stream(WorkingDay.values()).filter(d -> !scheduleByDay.containsKey(d)).toList();
            model.addAttribute("freeDays", freeDays);
            model.addAttribute("username", username);
            model.addAttribute("scheduleByDay", scheduleByDay);

        } catch (DoctorNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "doctor_pages/doctor_schedule";
    }

    @PostMapping("/apps")
    public RedirectView getAppointmentsForDoctorInRange(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                        LocalDate startDate,
                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                        LocalDate endDate,
                                                        @RequestParam(required = false) Status status,
                                                        RedirectAttributes redirectAttributes) {

        //using authentication
        String username = "rgarcia";

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

        return new RedirectView("appointments", true, false);
    }

    @GetMapping("/appointments")
    public String getAppointmentsForDoctor(Model model) {
        //using authentication
        String username = "rgarcia";
        try {
            model.addAttribute("dFullName", doctorService.getDoctorFullNameByUsername(username));
            model.addAttribute("statuses", Status.values());
        } catch (DoctorNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "doctor_pages/doctor_appointments";
    }

    @PostMapping("/appointments/cancel")
    public RedirectView cancelAppointment(@RequestParam(name = "id") int appointmentId) {
        appointmentService.updateAppointmentStatus(Status.CANCELLED, appointmentId);
        return new RedirectView("/doctor/appointments", true, false);
    }

    @GetMapping("/appointments/record/{appId}")
    public String showRecordForm(@PathVariable int appId, Model model) {
        try {
            RecordForDoctorDto record = recordService.getRecordForDoctorByAppointmentId(appId);
            model.addAttribute("record", record);
        } catch (PatientNotFoundException | RecordNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "doctor_pages/doctor_record";
    }

    @GetMapping("/appointments/record/form/{appId}")
    public String showMedicalRecordForm(@PathVariable int appId, Model model) {
        RecordCreationRequestDto dto = new RecordCreationRequestDto();
        PatientForRecordDto patient = patientService.getPatientByAppointmentId(appId);
        LocalDate date = appointmentService.getAppointmentDateById(appId);
        model.addAttribute("recordDto", dto);
        model.addAttribute("patient", patient);
        model.addAttribute("appDate", date);
        model.addAttribute("frequencies", Frequency.values());

        return "doctor_pages/doctor_record_form";
    }

    @PostMapping("/appointments/record/save")
    public RedirectView saveRecord(@ModelAttribute RecordCreationRequestDto dto,
                                   @RequestParam int appId, RedirectAttributes redirectAttributes) {

        dto.setAppointmentId(appId);
        try {
            recordService.saveRecord(dto);
            appointmentService.updateAppointmentStatus(Status.COMPLETED, appId);
        } catch (AppointmentNotFoundException e) {
            redirectAttributes.addAttribute("errorMessage", e.getMessage());
        }
        return new RedirectView("/doctor/appointments/record/" + appId, true, false);
    }
}
