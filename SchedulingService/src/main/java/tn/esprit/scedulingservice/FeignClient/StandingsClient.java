package tn.esprit.scedulingservice.FeignClient;

import esprit.tn.shared.config.DTO.StandingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "standing-service")
public interface StandingsClient {
    @GetMapping("standings/getstandingfortournamentandteam/{tournamentId}/{teamId}")
    public ResponseEntity<StandingDTO> findByTournamentIdAndTeamId(@PathVariable Long tournamentId, @PathVariable Long teamId);
}
