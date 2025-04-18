package tn.esprit.scedulingservice.Controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.scedulingservice.Entities.MatchSchedule;
import tn.esprit.scedulingservice.ServiceImpl.MatchSceduleServiceImp;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/match-schedules")
@AllArgsConstructor
public class MatchSceduleController {
    @GetMapping("getmatchbyid/{id}")
    public Optional<MatchSchedule> retrieveById(@PathVariable String id) {
        return matchSceduleServiceImp.retrieveById(id);
    }
    @GetMapping("getallmatch")
    public List<MatchSchedule> retrieveAll() {
        return matchSceduleServiceImp.retrieveAll();
    }
    @PostMapping("addmatch/{match}")
    public MatchSchedule add(@RequestBody MatchSchedule match) {
        return matchSceduleServiceImp.add(match);
    }
@PutMapping("updatematch/{id}")
    public MatchSchedule update(@RequestBody MatchSchedule object,@PathVariable String id) {
        return matchSceduleServiceImp.update(object, id);
    }
@DeleteMapping("deletematchbyid/{id}")
    public void delete(@PathVariable String id) {
        matchSceduleServiceImp.delete(id);
    }
@DeleteMapping("deleteallmatch")
    public void deleteAll() {
        matchSceduleServiceImp.deleteAll();
    }
@GetMapping("getallmatchbyround/{roundId}")
    public List<MatchSchedule> findAllMatchByRound(@PathVariable String roundId) {
        return matchSceduleServiceImp.findAllMatchByRound(roundId);
    }

    MatchSceduleServiceImp matchSceduleServiceImp;
}
