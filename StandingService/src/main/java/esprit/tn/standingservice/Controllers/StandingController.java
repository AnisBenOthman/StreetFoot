package esprit.tn.standingservice.Controllers;

import esprit.tn.shared.config.DTO.MatchScoreUpdateEvent;
import esprit.tn.standingservice.Entities.Standings;
import esprit.tn.standingservice.ServiceImp.StandingImpl;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    public Optional<List<Standings>> findByTournamentId(Long tournamentId) {
        return standingImp.findByTournamentId(tournamentId);
    }

    public Optional<Standings> findByTournamentIdAndTeamId(Long tournamentId, Long teamId) {
        return standingImp.findByTournamentIdAndTeamId(tournamentId, teamId);
    }



    StandingImpl standingImp;

}
