package pl.edu.hospital.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
        if (error != null) model.addAttribute("errorMessage", "Invalid username or password");
        if (logout != null) model.addAttribute("successMessage", "You have been logged out successfully");
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
            @Validated(OnCreate.class) @ModelAttribute("patient") PatientForProfileDto dto,
            BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("languages", Language.values());
            return "registration_form";
        }

        patientService.registerPatient(dto);
        return new RedirectView("/user/login", true, false);
    }
}
