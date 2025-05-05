package esprit.tn.standingservice.Controllers;

import esprit.tn.shared.config.DTO.MatchScoreUpdateEvent;
import esprit.tn.standingservice.Entities.Standings;
import esprit.tn.standingservice.ServiceImp.StandingImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("standings")
@AllArgsConstructor
public class StandingController {
    public Optional<Standings> retrieveById(String s) {
        return standingImp.retrieveById(s);
    }
@GetMapping("getallstandings")
    public List<Standings> retrieveAll() {
        return standingImp.retrieveAll();
    }

    public Standings add(Standings object) {
        return standingImp.add(object);
    }

    public Standings update(Standings object, String s) {
        return standingImp.update(object, s);
    }

    public void delete(String s) {
        standingImp.delete(s);
    }
@DeleteMapping("deleteall")
    public void deleteAll() {
        standingImp.deleteAll();
    }
@GetMapping("getstandingoftournament/{tournamentId}")
    public ResponseEntity<List<Standings> > findByTournamentId(@PathVariable Long tournamentId) {
        List<Standings> list = standingImp.findByTournamentId(tournamentId);
        return ResponseEntity.ok(list);
    }
@GetMapping("getstandingfortournamentandteam/{tournamentId}/{teamId}")
    public ResponseEntity<Standings>  findByTournamentIdAndTeamId(@PathVariable Long tournamentId, @PathVariable Long teamId) {
        Standings saved = standingImp.findByTournamentIdAndTeamId(tournamentId, teamId);
        return ResponseEntity.ok(saved);
    }



    StandingImpl standingImp;

}
