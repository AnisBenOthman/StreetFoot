package tn.esprit.scedulingservice.FeignClient;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tn.esprit.scedulingservice.DTO.Team;

@FeignClient(name = "tournament-service")
public interface TournamentClient {


    @GetMapping("team/getteambyname/{name}")
    public ResponseEntity<Team> findbyname(@PathVariable String name);
}
