package tn.esprit.scedulingservice.ServiceImpl;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.scedulingservice.Entities.MatchSchedule;
import tn.esprit.scedulingservice.Repositories.MatchSceduleRepository;
import tn.esprit.scedulingservice.Repositories.StandingRepository;
import tn.esprit.scedulingservice.Service.MatchSceduleService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchSceduleServiceImp implements MatchSceduleService {
    private final MatchSceduleRepository matchSceduleRepository;
    private final Standingimpl standingimpl;

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
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Match with id " + s + "not found"));

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
    public MatchSchedule updateMatchScore( String matchId, int homeScore, int awayScore) {
        MatchSchedule match = matchSceduleRepository.findById(matchId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Match with id " + matchId + "not found"));
        match.setHomeScore(homeScore);
        match.setAwayScore(awayScore);
        match.computeResult();
        matchSceduleRepository.save(match);
        standingimpl.updateStandingsForMatch(match.getId());

        return match;
    }
}
