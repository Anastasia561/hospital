package pl.edu.hospital.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.edu.hospital.entity.enums.Language;
import pl.edu.hospital.service.AdminService;
import pl.edu.hospital.utils.TranslationUtil;

import java.util.Map;

@ControllerAdvice
public class GlobalAttributes {
    private AdminService adminService;
    private static final Map<Language, Map<String, String>> TRANSLATED_LABELS = TranslationUtil.translateLabels();

    public GlobalAttributes(AdminService adminService) {
        this.adminService = adminService;
    }

    @ModelAttribute("translation")
    public Map<String, String> addTranslation() {
        //using authentication
        String username = "pscott";
        Language language = adminService.findByUsernameForProfileDto(username).getLanguage();
        return TRANSLATED_LABELS.get(language);
    }
}
