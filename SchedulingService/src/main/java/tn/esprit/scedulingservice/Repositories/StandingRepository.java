package tn.esprit.scedulingservice.Repositories;

import tn.esprit.scedulingservice.Entities.Standings;

import java.util.List;

public interface StandingRepository extends BaseRepository<Standings,String>{
    List<Standings> findByTournamentId(Long tournamentId);
    Standings findByTournamentIdAndTeamId(Long tournamentId, Integer teamId);
}
