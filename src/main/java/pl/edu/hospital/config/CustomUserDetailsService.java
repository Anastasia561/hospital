package pl.edu.hospital.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.edu.hospital.service.PersonService;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final PersonService personService;

    public CustomUserDetailsService(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return personService.findByUsername(username)
                .map(u -> User.builder()
                        .username(u.getUsername())
                        .password(u.getPassword())
                        .roles(u.getRole().name())
                        .build()
                ).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
