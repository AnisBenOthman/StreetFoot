package esprit.tn.standingservice.Services;

import esprit.tn.standingservice.Entities.Standings;

import java.util.List;
import java.util.Optional;

public interface StandingService extends BaseService<Standings,String> {
    List<Standings> findByTournamentId(Long tournamentId);
    Standings  findByTournamentIdAndTeamId(Long tournamentId, Long teamId);
}
