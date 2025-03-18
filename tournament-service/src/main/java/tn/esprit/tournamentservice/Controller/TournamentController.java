package tn.esprit.tournamentservice.Controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tournamentservice.Entities.Tournament;
import tn.esprit.tournamentservice.Exception.TournamentException;
import tn.esprit.tournamentservice.ServiceImpl.TournamentImpl;

import java.util.Map;
@Slf4j
@RestController
@RequestMapping("tournament")
@AllArgsConstructor
public class TournamentController {
    @PostMapping("addtournament")
    public ResponseEntity<?> add(@Valid @RequestBody Tournament tournament) {
        try {
            Tournament createdTournament = tournamentImpl.add(tournament);
            return ResponseEntity.ok(createdTournament);
        } catch (TournamentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }catch (Exception e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        }

    }

    @PutMapping("updateTournament/{id}")
    public ResponseEntity<?> update(@RequestBody Tournament tournament, @PathVariable Integer id) {
        try {
            return ResponseEntity.ok(tournamentImpl.update(tournament, id));
        } catch (EntityNotFoundException e) {
            return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (Exception e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        }


    }

    @GetMapping("gettournamentbyid/{id}")
    public ResponseEntity<?> retrieveById(Integer id) {

        try {
            return ResponseEntity.ok(tournamentImpl.retrieveById(id));
        } catch (EntityNotFoundException e) {
            return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        }
    }

    @DeleteMapping("deletetournament/{id}")
    public ResponseEntity<?> delete(Integer id) {
        try {
            tournamentImpl.delete(id);
            return ResponseEntity.ok(Map.of("message", "Tournament deleted successfully"));
        } catch (EntityNotFoundException e) {
            return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        }
    }

    private ResponseEntity<Map<String, String>> buildErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of("error", message));
    }

    TournamentImpl tournamentImpl;

}
