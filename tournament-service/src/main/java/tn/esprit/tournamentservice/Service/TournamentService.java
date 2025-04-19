package tn.esprit.tournamentservice.Service;

import tn.esprit.tournamentservice.Entities.Status;
import tn.esprit.tournamentservice.Entities.Tournament;

import java.util.List;

public interface TournamentService extends BaseService<Tournament, Long> {
    List<Tournament> findByCreatedByUserId(Long userId);
    List<Tournament> findByStatus(Status status);
    public Tournament registerTeams(Long tournamentId, List<Integer> teamsId);


}
