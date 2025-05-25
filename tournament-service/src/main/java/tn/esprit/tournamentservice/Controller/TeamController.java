package tn.esprit.tournamentservice.Controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tournamentservice.Entities.Team;
import tn.esprit.tournamentservice.ServiceImpl.TeamImpl;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("team")
@AllArgsConstructor
public class TeamController {
    @PostMapping("addteam")
    public ResponseEntity<Team> add(@Valid @RequestBody Team entity) {
        Team team = teamimpl.add(entity);
        return ResponseEntity.ok(team);
    }

    @PutMapping("updateteam/{id}")
    public ResponseEntity<Team> update(@Valid @RequestBody Team entity, @PathVariable Long id) {
        Team team = teamimpl.update(entity, id);
        return ResponseEntity.ok(team);
    }

    @GetMapping("getteambyid/{id}")
    public ResponseEntity<Team> retrieveById(@PathVariable Long id) {
        Team team = teamimpl.retrieveById(id);
        return ResponseEntity.ok(team);
    }

    @DeleteMapping("deletebyid/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        teamimpl.delete(id);
        return ResponseEntity.ok().build();
    }
@DeleteMapping("deleteall")
    public ResponseEntity<Void> deleteAll() {
        teamimpl.deleteAll();
        return ResponseEntity.ok().build();
    }
@GetMapping("getall")
    public ResponseEntity<List<Team>> getAll() {
        List<Team> list = teamimpl.getAll();
        return ResponseEntity.ok(list);
    }
@GetMapping("getteambyname/{name}")
    public ResponseEntity<Team> findbyname(@Valid @PathVariable String name) {
        Team team = teamimpl.findbyname(name);
        return ResponseEntity.ok(team);
    }

    TeamImpl teamimpl;
}
