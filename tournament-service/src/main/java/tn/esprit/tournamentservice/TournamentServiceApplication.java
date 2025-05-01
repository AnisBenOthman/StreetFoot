package tn.esprit.tournamentservice;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication(scanBasePackages = {"tn.esprit.tournamentservice", "esprit.tn.shared.config"})
@EnableFeignClients


public class TournamentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TournamentServiceApplication.class, args);
        System.out.println("hello from Tournament project");
    }


}
