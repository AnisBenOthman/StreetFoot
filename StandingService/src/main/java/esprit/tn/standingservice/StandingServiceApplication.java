package esprit.tn.standingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"esprit.tn.standingservice", "esprit.tn.shared"})
public class StandingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StandingServiceApplication.class, args);
    }

}
