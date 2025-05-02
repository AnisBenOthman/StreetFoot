package tn.esprit.scedulingservice.ServiceImpl;


import esprit.tn.shared.config.DTO.MatchScoreUpdateEvent;
import esprit.tn.shared.config.EventEnvelop;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.scedulingservice.Entities.MatchSchedule;
import tn.esprit.scedulingservice.Entities.Round;
import tn.esprit.scedulingservice.Repositories.MatchSceduleRepository;
import tn.esprit.scedulingservice.Repositories.RoundRepository;
import tn.esprit.scedulingservice.Repositories.StandingRepository;
import tn.esprit.scedulingservice.Service.MatchSceduleService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchSceduleServiceImp implements MatchSceduleService {
    private final MatchSceduleRepository matchSceduleRepository;
    private final Standingimpl standingimpl;
    private final RoundRepository roundRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public Optional<MatchSchedule> retrieveById(String s) {
        return matchSceduleRepository.findById(s);
    }

    @Override
    public List<MatchSchedule> retrieveAll() {

        return matchSceduleRepository.findAll();
    }

    @Override
    public MatchSchedule add(MatchSchedule object) {

        return matchSceduleRepository.save(object);
    }

    @Override
    public MatchSchedule update(MatchSchedule object, String s) {

        return matchSceduleRepository.findById(s).map(m -> {

            m.setHomeTeamId(object.getHomeTeamId());
            m.setAwayTeamId(object.getAwayTeamId());
            m.setStadium(object.getStadium());
            m.setAwayScore(object.getAwayScore());
            m.setHomeScore(object.getHomeScore());
            return matchSceduleRepository.save(m);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match with id " + s + "not found"));

    }

    @Override
    public void delete(String s) {
        matchSceduleRepository.deleteById(s);

    }

    @Override
    public void deleteAll() {
        matchSceduleRepository.deleteAll();
    }

    @Override
    public List<MatchSchedule> findAllMatchByRound(String roundId) {
        return matchSceduleRepository.findByRoundId(roundId);
    }

    @Override
    public MatchSchedule updateMatchScore(String matchId, int homeScore, int awayScore) {
        MatchSchedule match = matchSceduleRepository.findById(matchId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Match with id " + matchId + "not found"));
        match.setHomeScore(homeScore);
        match.setAwayScore(awayScore);
        match.computeResult();
        Round round = roundRepository.findById(match.getRoundId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Round with id " + match.getRoundId() + "not found"));
        Long tournamentId = round.getTournamentId();
        matchSceduleRepository.save(match);
        MatchScoreUpdateEvent matchScoreUpdateEvent = new MatchScoreUpdateEvent(tournamentId, match.getRoundId(), matchId, match.getHomeTeamId(), match.getAwayTeamId(), match.getHomeScore(), match.getAwayScore(), match.getWinnerTeamId());
        EventEnvelop<MatchScoreUpdateEvent> envelop = EventEnvelop.<MatchScoreUpdateEvent>builder().eventId(UUID.randomUUID().toString()).type("updatematchscore").payload(matchScoreUpdateEvent).timestamp(Instant.now()).build();
        //standingimpl.updateStandingsForMatch(match.getId());
        kafkaTemplate.send("schedule.updatematchscore", envelop);
        return match;
    }
}
