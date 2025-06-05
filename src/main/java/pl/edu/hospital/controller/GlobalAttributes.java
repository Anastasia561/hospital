package pl.edu.hospital.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.edu.hospital.entity.enums.Language;
import pl.edu.hospital.service.PersonService;
import pl.edu.hospital.utils.TranslationUtil;

import java.util.Map;

@ControllerAdvice
public class GlobalAttributes {
    private final PersonService personService;
    private static final Map<Language, Map<String, String>> TRANSLATED_LABELS = TranslationUtil.translateLabels();

    public GlobalAttributes(PersonService personService) {
        this.personService = personService;
    }

    @ModelAttribute("translation")
    public Map<String, String> addTranslation() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Language language = personService.findByUsername(username).get().getLanguage();
        return TRANSLATED_LABELS.get(language);
    }
}
