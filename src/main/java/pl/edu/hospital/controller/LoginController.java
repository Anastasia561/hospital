package pl.edu.hospital.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import pl.edu.hospital.dto.patient.PatientForProfileDto;
import pl.edu.hospital.entity.enums.Language;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String showLoginForm() {
        return "login_form";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("patient", new PatientForProfileDto());
        model.addAttribute("languages", Language.values());
        return "registration_form";
    }

    @PostMapping("/register")
    public RedirectView processRegistration(PatientForProfileDto patient) {
//        patientService.registerPatient(patient);
        return new RedirectView("/login", true, false);
    }
}
