package tn.esprit.scedulingservice.ServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.scedulingservice.Entities.MatchSchedule;
import tn.esprit.scedulingservice.Entities.Round;
import tn.esprit.scedulingservice.Entities.Standings;
import tn.esprit.scedulingservice.Repositories.MatchSceduleRepository;
import tn.esprit.scedulingservice.Repositories.RoundRepository;
import tn.esprit.scedulingservice.Repositories.StandingRepository;
import tn.esprit.scedulingservice.Service.StandingService;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class Standingimpl implements StandingService {
    private final MatchSceduleRepository matchRepo;
    private final RoundRepository roundRepository;
    private final StandingRepository standingRepository;

    @Override
    public Optional<Standings> retrieveById(String s) {
        return Optional.empty();
    }

    @Override
    public List<Standings> retrieveAll() {
        return standingRepository.findAll();
    }

    @Override
    public Standings add(Standings object) {
        return standingRepository.save(object);
    }

    @Override
    public Standings update(Standings object, String s) {
        return null;
    }

    @Override
    public void delete(String s) {
        standingRepository.deleteById(s);

    }

    @Override
    public void deleteAll() {
        standingRepository.deleteAll();

    }

    @Override
    public List<Standings>  findByTournamentId(Long tournamentId) {
        return standingRepository.findByTournamentId(tournamentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"tournament with id " + tournamentId + "not found"));
    }

    @Override
    public Standings findByTournamentIdAndTeamId(Long tournamentId, Long teamId) {
        return standingRepository.findByTournamentIdAndTeamId(tournamentId, teamId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team with id" + teamId + "not found"));
    }
    public void updateStandingsForMatch(String matchId) {
        MatchSchedule match = matchRepo.findById(matchId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match with id " + matchId + "not found"));
        Round round = roundRepository.findById(match.getRoundId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Round with id " + match.getRoundId() + "not found"));
        Standings homeStanding = standingRepository.findByTournamentIdAndTeamId(round.getTournamentId(), match.getHomeTeamId()).orElseGet(() -> new Standings(round.getTournamentId(),match.getHomeTeamId()));
        Standings awayStanding = standingRepository.findByTournamentIdAndTeamId(round.getTournamentId(),match.getAwayTeamId()).orElseGet(() -> new Standings(round.getTournamentId(),match.getAwayTeamId()));
        applyResult(homeStanding,match.getHomeScore(),match.getAwayScore());
        applyResult(awayStanding,match.getAwayScore(),match.getHomeScore());
        standingRepository.save(homeStanding);
        standingRepository.save(awayStanding);
    }

    public void applyResult(Standings s, int scored, int conceded){
        s.setMatchesPlayed(s.getMatchesPlayed() + 1);
        s.setGoalScored(s.getGoalScored()+scored);
        s.setGoalConceded(s.getGoalConceded()+conceded);
        if(scored>conceded){
            s.setWins(s.getWins()+1);

        } else if (scored<conceded) {
            s.setLosses(s.getLosses()+1);
        }else s.setDraws(s.getDraws()+1);
        s.setPoints(s.getWins()*3 + s.getDraws());
    }
}
