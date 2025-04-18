package tn.esprit.scedulingservice.Controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.scedulingservice.Entities.Round;
import tn.esprit.scedulingservice.ServiceImpl.RoundServiceImpl;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/round-scedules")
@AllArgsConstructor
public class RoundController {
    @GetMapping("getroundbyid/{id}")
    public Optional<Round> retrieveById(@PathVariable String id) {
        return roundService.retrieveById(id);
    }
@GetMapping("getallround")
    public List<Round> retrieveAll() {
        return roundService.retrieveAll();
    }
@PostMapping("addround")
    public Round add(@PathVariable Round round) {
        return roundService.add(round);
    }
@PutMapping("updateround/{id}")
    public Round update(@RequestBody Round round, @PathVariable String id) {
        return roundService.update(round, id);
    }
@DeleteMapping("deleteroundbyid/{id}")
    public void delete(String id) {
        roundService.delete(id);
    }
@DeleteMapping("deleteallrounds")
    public void deleteAll() {
        roundService.deleteAll();
    }
@GetMapping("getallroundbytournament/{tournamentId}")
    public List<Round> findAllRoundByTournament(@PathVariable Long tournamentId) {
        return roundService.findAllRoundByTournament(tournamentId);
    }

    RoundServiceImpl roundService;
}
