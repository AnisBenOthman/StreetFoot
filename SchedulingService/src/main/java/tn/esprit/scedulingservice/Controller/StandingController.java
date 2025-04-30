package tn.esprit.scedulingservice.Controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.scedulingservice.Entities.Standings;
import tn.esprit.scedulingservice.ServiceImpl.Standingimpl;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("standings")
@AllArgsConstructor
public class StandingController {


    public Optional<Standings> retrieveById(String s) {
        return standingimpl.retrieveById(s);
    }
@GetMapping("gellallstandings")
    public List<Standings> retrieveAll() {
        return standingimpl.retrieveAll();
    }

    public Standings add(Standings object) {
        return standingimpl.add(object);
    }

    public Standings update(Standings object, String s) {
        return standingimpl.update(object, s);
    }

    public void delete(String s) {
        standingimpl.delete(s);
    }
    @DeleteMapping("deletall")
    public void deleteAll() {
        standingimpl.deleteAll();
    }
@GetMapping("getstandingsbytournament")
    public List<Standings> findByTournamentId(Long tournamentId) {
        return standingimpl.findByTournamentId(tournamentId);
    }
@GetMapping("getstandingsbyteam/{tournamentId}/{teamId}")
    public Standings  findByTournamentIdAndTeamId(@PathVariable Long tournamentId, @PathVariable Long teamId) {
        return standingimpl.findByTournamentIdAndTeamId(tournamentId, teamId);
    }





    Standingimpl standingimpl;
}
