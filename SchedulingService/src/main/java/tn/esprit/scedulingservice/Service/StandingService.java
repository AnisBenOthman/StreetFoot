package tn.esprit.scedulingservice.Service;

import tn.esprit.scedulingservice.Entities.Standings;

import java.util.List;

public interface StandingService extends BaseService<Standings, String> {
    List<Standings> findByTournamentId(Long tournamentId);
    Standings findByTournamentIdAndTeamId(Long tournamentId, Integer teamId);
}
