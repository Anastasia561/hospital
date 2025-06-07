package pl.edu.hospital.utils;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import pl.edu.hospital.entity.enums.Language;
import pl.edu.hospital.service.PersonService;

import java.util.Locale;

@Component
public class LocaleInterceptor implements HandlerInterceptor {

    private final PersonService personService;
    private final LocaleResolver localeResolver;

    public LocaleInterceptor(PersonService personService, LocaleResolver localeResolver) {
        this.personService = personService;
        this.localeResolver = localeResolver;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {

            String username = authentication.getName();

            personService.findByUsername(username).ifPresent(person -> {
                String lang = switch (person.getLanguage()) {
                    case Language.POLISH -> "pl";
                    case Language.SPANISH -> "es";
                    default -> "en";
                };
                Locale locale = new Locale(lang);
                localeResolver.setLocale(request, response, locale);
            });
        }

        return true;
    }
}
