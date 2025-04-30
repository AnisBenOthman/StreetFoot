package tn.esprit.scedulingservice.Repositories;

import tn.esprit.scedulingservice.Entities.Standings;

import java.util.List;
import java.util.Optional;

public interface StandingRepository extends BaseRepository<Standings,String>{
    Optional<List<Standings>>   findByTournamentId(Long tournamentId);
    Optional<Standings>  findByTournamentIdAndTeamId(Long tournamentId, Long teamId);
}
