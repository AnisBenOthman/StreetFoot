package tn.esprit.scedulingservice.Controller;

import jakarta.ws.rs.PUT;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.scedulingservice.Entities.MatchSchedule;
import tn.esprit.scedulingservice.ServiceImpl.MatchSceduleServiceImp;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("match-schedules")
@AllArgsConstructor
public class MatchSceduleController {
    @GetMapping("getmatchbyid/{id}")
    public ResponseEntity<?> retrieveById(@PathVariable String id) {
        return matchSceduleServiceImp.retrieveById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("getallmatch")
    public ResponseEntity<List<MatchSchedule>> retrieveAll() {
        List<MatchSchedule> matches = matchSceduleServiceImp.retrieveAll();
        return ResponseEntity.ok(matches);
    }

    @PostMapping("addmatch/{match}")
    public ResponseEntity<MatchSchedule> add(@RequestBody MatchSchedule match) {
        MatchSchedule save = matchSceduleServiceImp.add(match);
        return ResponseEntity.ok(save);
    }

    @PutMapping("updatematch/{id}")
    public ResponseEntity<MatchSchedule> update(@RequestBody MatchSchedule object, @PathVariable String id) {
        try {
            MatchSchedule save = matchSceduleServiceImp.update(object, id);
            return ResponseEntity.ok(save);


        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("deletematchbyid/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        try{
            matchSceduleServiceImp.delete(id);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("deleteallmatch")
    public ResponseEntity<String> deleteAll() {
        matchSceduleServiceImp.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("getallmatchbyround/{roundId}")
    public ResponseEntity<List<MatchSchedule>>  findAllMatchByRound(@PathVariable String roundId) {
        try{
            List<MatchSchedule> list = matchSceduleServiceImp.findAllMatchByRound(roundId);
            return ResponseEntity.ok(list);
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }

    }

    @PutMapping("updatescore/{matchId}/{homeScore}/{awayScore}")
    public ResponseEntity<MatchSchedule>  updateMatchScore(@PathVariable String matchId, @PathVariable int homeScore, @PathVariable int awayScore) {
        try{
            MatchSchedule match = matchSceduleServiceImp.updateMatchScore(matchId, homeScore, awayScore);
            return ResponseEntity.ok(match);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }

    }
    @GetMapping("getallmatcheswithpaination")
    public List<MatchSchedule> findAllMatch(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        return matchSceduleServiceImp.findAllMatch(page,size);
    }

    MatchSceduleServiceImp matchSceduleServiceImp;
}
