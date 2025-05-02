package esprit.tn.standingservice.Repositories;

import esprit.tn.standingservice.Entities.Standings;

import java.util.List;
import java.util.Optional;

public interface StandingRepo extends BaseRepository<Standings,String> {
    Optional<List<Standings>> findByTournamentId(Long tournamentId);
    Optional<Standings>  findByTournamentIdAndTeamId(Long tournamentId, Long teamId);
}
