package esprit.tn.standingservice.ServiceImp;

import esprit.tn.shared.config.DTO.MatchScoreUpdateEvent;
import esprit.tn.shared.config.EventEnvelop;
import esprit.tn.standingservice.Entities.Standings;
import esprit.tn.standingservice.Repositories.StandingRepo;
import esprit.tn.standingservice.Services.StandingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StandingImpl implements StandingService {
    final StandingRepo standingRepo;
    final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public Optional<Standings> retrieveById(String s) {
        return Optional.empty();
    }

    @Override
    public List<Standings> retrieveAll() {
        return null;
    }

    @Override
    public Standings add(Standings object) {
        return null;
    }

    @Override
    public Standings update(Standings object, String s) {
        return null;
    }

    @Override
    public void delete(String s) {

    }

    @Override
    public void deleteAll() {
        standingRepo.deleteAll();

    }

    @Override
    public Optional<List<Standings>> findByTournamentId(Long tournamentId) {
        return Optional.empty();
    }

    @Override
    public Optional<Standings> findByTournamentIdAndTeamId(Long tournamentId, Long teamId) {
        return Optional.empty();
    }

    @KafkaListener(topics = "schedule.updatematchscore")
    public void updateStandingsForMatch(EventEnvelop<MatchScoreUpdateEvent > evt) {
        Standings homeStanding = standingRepo.findByTournamentIdAndTeamId(evt.getPayload().tournamentId(), evt.getPayload().homeTeamId()).orElseGet(() -> new Standings(evt.getPayload().tournamentId(), evt.getPayload().homeTeamId()));
        Standings awayStanding = standingRepo.findByTournamentIdAndTeamId(evt.getPayload().tournamentId(), evt.getPayload().awayTeamId()).orElseGet(() -> new Standings(evt.getPayload().tournamentId(), evt.getPayload().awayTeamId()));
        applyResult(homeStanding, evt.getPayload().homeScore(), evt.getPayload().awayScore());
        applyResult(awayStanding, evt.getPayload().awayScore(), evt.getPayload().homeScore());
        standingRepo.save(homeStanding);
        standingRepo.save(awayStanding);

    }

    public void applyResult(Standings s, int scored, int conceded) {
        s.setMatchesPlayed(s.getMatchesPlayed() + 1);
        s.setGoalScored(s.getGoalScored() + scored);
        s.setGoalConceded(s.getGoalConceded() + conceded);
        if (scored > conceded) {
            s.setWins(s.getWins() + 1);

        } else if (scored < conceded) {
            s.setLosses(s.getLosses() + 1);
        } else s.setDraws(s.getDraws() + 1);
        s.setPoints(s.getWins() * 3 + s.getDraws());
    }
}
