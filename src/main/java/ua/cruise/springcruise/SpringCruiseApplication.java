package ua.cruise.springcruise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SpringCruiseApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCruiseApplication.class, args);
    }

}
