package tn.esprit.tournamentservice.Controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tournamentservice.Entities.Tournament;
import tn.esprit.tournamentservice.Entities.TournamentRules;
import tn.esprit.tournamentservice.Exception.TournamentException;
import tn.esprit.tournamentservice.ServiceImpl.TournamentImpl;
import tn.esprit.tournamentservice.ServiceImpl.TournamentRulesImpl;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("tournamentRules")
@AllArgsConstructor
public class TournamentRulesController {
    @PostMapping("add")
    public ResponseEntity<?> add(@RequestBody TournamentRules tournamentRules) {
        try {
            return ResponseEntity.ok(tournamentRulesImpl.add(tournamentRules));
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        }
    }
@PutMapping("update/{id}")
    public ResponseEntity<?> update(@RequestBody TournamentRules tournamentRules, @PathVariable Integer id) {
        try {
            return ResponseEntity.ok(tournamentRulesImpl.update(tournamentRules, id));
        } catch (EntityNotFoundException e) {
            return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        }

    }
@GetMapping("getbyid/{id}")
    public ResponseEntity<?> retrieveById(Integer id) {
        try {
            return ResponseEntity.ok(tournamentRulesImpl.retrieveById(id));
        } catch (EntityNotFoundException e) {
            return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        }

    }
@DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            tournamentRulesImpl.delete(id);
        return ResponseEntity.ok(Map.of("message :","delete seccessfully"));

        } catch (EntityNotFoundException e) {
            return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        }


    }
@GetMapping("getAll")
    public ResponseEntity<?> getAll() {
        try{
            return ResponseEntity.ok(tournamentRulesImpl.getAll());
        }catch (Exception e){
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        }
    }

    TournamentRulesImpl tournamentRulesImpl;

    private ResponseEntity<Map<String, String>> buildErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of("error", message));
    }


}
