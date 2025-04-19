package tn.esprit.scedulingservice.Controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Round> retrieveById(@PathVariable String id) {


           return roundService.retrieveById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());

    }
@GetMapping("getallround")
    public ResponseEntity<List<Round>> retrieveAll() {
        return ResponseEntity.ok(roundService.retrieveAll()) ;
    }
@PostMapping("addround")
    public ResponseEntity<Round> add(@RequestBody Round round) {
        Round save = roundService.add(round);
        return ResponseEntity.ok(save);
    }
@PutMapping("updateround/{id}")
    public ResponseEntity<Round>  update(@RequestBody Round round, @PathVariable String id) {
        try{
            Round save = roundService.update(round, id);
            return ResponseEntity.ok(save);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }
@DeleteMapping("deleteroundbyid/{id}")
    public ResponseEntity<Void> delete(String id) {
        try{
            roundService.delete(id);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }

    }
@DeleteMapping("deleteallrounds")
    public ResponseEntity<Void> deleteAll() {
        roundService.deleteAll();
        return ResponseEntity.ok().build();
    }
@GetMapping("getallroundbytournament/{tournamentId}")
    public ResponseEntity<List<Round>>  findAllRoundByTournament(@PathVariable Long tournamentId) {
        try{
            List<Round> list = roundService.findAllRoundByTournament(tournamentId);
            return ResponseEntity.ok(list);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }

    }

    RoundServiceImpl roundService;
}
