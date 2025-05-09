package esprit.tn.standingservice.ServiceImp;

import esprit.tn.shared.config.DTO.MatchScoreUpdateEvent;
import esprit.tn.shared.config.DTO.ScheduleGeneratedEvent;
import esprit.tn.shared.config.DTO.TournamentCreatedEvent;
import esprit.tn.shared.config.EventEnvelop;
import esprit.tn.standingservice.Entities.Standings;
import esprit.tn.standingservice.Repositories.StandingRepo;
import esprit.tn.standingservice.Services.StandingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Cast;
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

        return standingRepo.findById(s);
    }

    @Override
    public List<Standings> retrieveAll() {

        return standingRepo.findAll();
    }

    @Override
    public Standings add(Standings object) {
        return standingRepo.save(object);
    }

    @Override
    public Standings update(Standings object, String s) {
        return null;
    }

    @Override
    public void delete(String s) {
        standingRepo.deleteById(s);

    }

    @Override
    public void deleteAll() {
        standingRepo.deleteAll();

    }

    @Override
    public List<Standings> findByTournamentId(Long tournamentId) {
        return standingRepo.findByTournamentId(tournamentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tournament with id " + tournamentId + "not found"));
    }

    @Override
    public Standings findByTournamentIdAndTeamId(Long tournamentId, Long teamId) {

        return standingRepo.findByTournamentIdAndTeamId(tournamentId,teamId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Team with id" + teamId + "not found"));
    }
    @KafkaListener(topics = "schedule.generated", groupId = "standings-service")
    @Transactional
    public void addStandingsForTournament(EventEnvelop<ScheduleGeneratedEvent> evt){
        List<Integer> teams = evt.getPayload().teamsId();
        for(Integer teamid : teams){
            Standings standings = new Standings(evt.getPayload().tournamentId(), teamid.longValue(),0,0,0,0,0,0,0);
            standingRepo.save(standings);
        }
    }

    @KafkaListener(topics = "schedule.updatematchscore",groupId = "standings-service")
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
