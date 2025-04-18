package tn.esprit.tournamentservice.Repositories;

import tn.esprit.tournamentservice.Entities.Status;
import tn.esprit.tournamentservice.Entities.Tournament;

import java.util.List;

public interface TournamentRepository extends BaseRepository<Tournament, Integer> {
    List<Tournament> findByUserId(Long userId);
    List<Tournament> findByStatus(Status status);
}
