package tn.esprit.tournamentservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TournamentServiceApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(TournamentServiceApplication.class, args);
    }
    @Override
    public void run(String... args) throws Exception {
        System.out.println("hello from Tournament project");
    }
}
