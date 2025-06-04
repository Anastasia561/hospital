package pl.edu.hospital.service;

//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//    private final PersonService personService;
//
//    public CustomUserDetailsService(PersonService personService) {
//        this.personService = personService;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return personService.findByUsername(username)
//                .map(dto -> User.builder()
//                        .username(dto.getUsername())
//                        .password(dto.getPassword())
//                        .roles(dto.getRole().toString())
//                        .build())
//                .orElseThrow(() -> new UsernameNotFoundException("User with username - " + username + " not found"));
//    }
//
//}
