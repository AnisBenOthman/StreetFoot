package tn.esprit.tournamentservice.Repositories;

import tn.esprit.tournamentservice.Entities.Sport;
import tn.esprit.tournamentservice.Entities.Status;
import tn.esprit.tournamentservice.Entities.Tournament;

import java.util.List;

public interface TournamentRepository extends BaseRepository<Tournament, Long> {
    List<Tournament> findByUserId(Long userId);
    List<Tournament> findByStatus(Status status);
    List<Tournament> findBySport(Sport sport);
}
