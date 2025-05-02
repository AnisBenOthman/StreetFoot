package tn.esprit.scedulingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"tn.esprit.scedulingservice", "esprit.tn.shared.config"})
public class ScedulingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScedulingServiceApplication.class, args);
	}

}
