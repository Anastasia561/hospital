package pl.edu.hospital.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();
            switch (role) {
                case "ROLE_ADMIN" -> {
                    response.sendRedirect("admin/home");
                    return;
                }
                case "ROLE_PATIENT" -> {
                    response.sendRedirect("patient/home");
                    return;
                }
                case "ROLE_DOCTOR" -> {
                    response.sendRedirect("doctor/home");
                    return;
                }
            }
        }
        response.sendRedirect("/login?error=true");
    }
}
