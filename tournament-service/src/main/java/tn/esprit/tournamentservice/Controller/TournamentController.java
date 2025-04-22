package tn.esprit.tournamentservice.Controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tournamentservice.Entities.Status;
import tn.esprit.tournamentservice.Entities.Tournament;
import tn.esprit.tournamentservice.Exception.TournamentException;
import tn.esprit.tournamentservice.ServiceImpl.TournamentImpl;


import java.util.List;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("tournament")
@AllArgsConstructor
public class TournamentController {
    @PostMapping("addtournament")
    public ResponseEntity<?> add(@Valid @RequestBody Tournament tournament) {
        try {
            if (tournament.getStartDate() != null && tournament.getEndDate() != null &&
                    !tournament.getEndDate().isAfter(tournament.getStartDate())) {
                return buildErrorResponse(HttpStatus.BAD_REQUEST, "La date de fin doit être postérieure à la date de début");
            }
            if (tournament.getTeamRegistrationDeadline().isAfter(tournament.getStartDate().minusWeeks(3))) {
                return buildErrorResponse(HttpStatus.BAD_REQUEST, "La date limite d'inscription doit être au moins 3 semaines avant le début du tournoi");
            }
            Tournament createdTournament = tournamentImpl.add(tournament);
            return ResponseEntity.ok(createdTournament);
        } catch (TournamentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }catch (Exception e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        }

    }

    @PutMapping("updateTournament/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Tournament tournament, @PathVariable Long id) {
        try {
            if (tournament.getStartDate() != null && tournament.getEndDate() != null &&
                    !tournament.getEndDate().isAfter(tournament.getStartDate())) {
                return buildErrorResponse(HttpStatus.BAD_REQUEST, "La date de fin doit être postérieure à la date de début");
            }
            if (tournament.getTeamRegistrationDeadline().isAfter(tournament.getStartDate().minusWeeks(3))) {
                return buildErrorResponse(HttpStatus.BAD_REQUEST, "La date limite d'inscription doit être au moins 3 semaines avant le début du tournoi");
            }
            return ResponseEntity.ok(tournamentImpl.update(tournament, id));
        } catch (EntityNotFoundException e) {
            return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (Exception e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        }


    }

    @GetMapping("gettournamentbyid/{id}")
    public ResponseEntity<?> retrieveById(@PathVariable Long id) {

        try {
            return ResponseEntity.ok(tournamentImpl.retrieveById(id));
        } catch (EntityNotFoundException e) {
            return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        }
    }

    @DeleteMapping("deletetournament/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
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
    @GetMapping("getall")
    public ResponseEntity<?> getAll() {
        try{
            return ResponseEntity.ok(tournamentImpl.getAll());
        }catch (Exception e){
            e.printStackTrace();
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        }
    }
    @DeleteMapping("deleteall")
    public ResponseEntity<?> deleteAll() {
        try {
            tournamentImpl.deleteAll();
            return ResponseEntity.ok(Map.of("message", "All Tournaments deleted successfully"));
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        }
    }
    @GetMapping("gettournamentbyuser/{userId}")
    public ResponseEntity<?> findByCreatedByUserId(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(tournamentImpl.findByCreatedByUserId(userId));
        } catch (EntityNotFoundException e) {
            return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        }
    }

    @GetMapping("gettournamentbystatus/{status}")
    public ResponseEntity<?> findByStatus(@PathVariable Status status) {
        try {
            return ResponseEntity.ok(tournamentImpl.findByStatus(status));
        } catch (Exception e) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        }
    }

@PostMapping("registreteams/{tournamentId}")
    public ResponseEntity<Tournament> registerTeams(@PathVariable Long tournamentId, @RequestBody List<Integer> teamsId) {
        Tournament tournament = tournamentImpl.registerTeams(tournamentId, teamsId);
        return ResponseEntity.ok(tournament);
    }

    TournamentImpl tournamentImpl;

}
