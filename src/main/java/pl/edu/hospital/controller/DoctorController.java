package pl.edu.hospital.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.edu.hospital.dto.ConsultationDto;
import pl.edu.hospital.entity.enums.WorkingDay;
import pl.edu.hospital.exception.DoctorNotFoundException;
import pl.edu.hospital.service.ConsultationService;
import pl.edu.hospital.service.DoctorService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/doctor")
public class DoctorController {
    private final DoctorService doctorService;
    private final ConsultationService consultationService;

    public DoctorController(DoctorService doctorService, ConsultationService consultationService) {
        this.doctorService = doctorService;
        this.consultationService = consultationService;
    }

    @GetMapping("/home")
    public String home(Model model) {
        //using authentication
        String dFullName = "Rafael Garcia";
        model.addAttribute("dFullName", dFullName);
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
}
