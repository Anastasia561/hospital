package pl.edu.hospital.mapper;

import org.springframework.stereotype.Component;
import pl.edu.hospital.dto.PersonDto;
import pl.edu.hospital.entity.Person;

@Component
public class PersonMapper {
    public PersonDto toPersonDto(Person person) {
        PersonDto dto = new PersonDto();
        dto.setPassword(person.getPassword());
        dto.setUsername(person.getUsername());
        dto.setRole(person.getRole());
        return dto;
    }
}
