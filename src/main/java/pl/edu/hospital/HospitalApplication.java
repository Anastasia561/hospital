package pl.edu.hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import pl.edu.hospital.entity.Appointment;
import pl.edu.hospital.service.AppointmentService;

import java.util.List;

@SpringBootApplication
public class HospitalApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(HospitalApplication.class, args);
    }

}
