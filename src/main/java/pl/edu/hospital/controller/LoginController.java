package pl.edu.hospital.controller;

import jakarta.validation.groups.Default;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import pl.edu.hospital.dto.patient.PatientForProfileDto;
import pl.edu.hospital.entity.enums.Language;
import pl.edu.hospital.service.PatientService;
import pl.edu.hospital.validation.OnCreate;

@Controller
public class LoginController {
    private final PatientService patientService;

    public LoginController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout,
                                Model model) {
        if (error != null) model.addAttribute("errorMessage", "pl.edu.hospital.failure.invalidPass");
        if (logout != null) model.addAttribute("successMessage", "pl.edu.hospital.success.logout");
        return "login_form";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("patient", new PatientForProfileDto());
        model.addAttribute("languages", Language.values());
        return "registration_form";
    }

    @PostMapping("/register")
    public Object processCreateForm(
            @Validated({Default.class, OnCreate.class}) @ModelAttribute("patient") PatientForProfileDto dto,
            BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("languages", Language.values());
            return "registration_form";
        }

        patientService.registerPatient(dto);
        return new RedirectView("/user/login", true, false);
    }
}
