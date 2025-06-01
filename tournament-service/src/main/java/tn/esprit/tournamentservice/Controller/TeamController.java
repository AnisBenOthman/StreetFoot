package tn.esprit.tournamentservice.Controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tournamentservice.Entities.Team;
import tn.esprit.tournamentservice.ServiceImpl.TeamImpl;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("team")
@AllArgsConstructor
public class TeamController {
    @PostMapping("addteam")
    public ResponseEntity<?> add(@Valid @RequestBody Team entity) {
        try {
            // Attempt to find the team by name. If it exists, findbyname() will return it.
            // If it doesn't exist, findbyname() (as modified below) will return an Optional.empty()
            // or a null, depending on its implementation if you choose not to throw an exception.
            // Here, we'll assume findByName returns an Optional.
            Optional<Team> exist = teamimpl.findbyname(entity.getName());

            if (exist.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Team with name " + entity.getName() + " already exists.");
            } else {
                Team team = teamimpl.add(entity);
                return ResponseEntity.status(HttpStatus.CREATED).body(team); // Use CREATED for successful resource creation
            }
        } catch (Exception e) { // Catch broader exceptions during the add process if necessary
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }


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
        Optional<Team> teamOptional = teamimpl.findbyname(name);

        if (teamOptional.isPresent()) {
            return ResponseEntity.ok(teamOptional.get());
        } else {
            // Return 404 Not Found if the team doesn't exist
            return ResponseEntity.notFound().build();
        }
    }

    TeamImpl teamimpl;
}
