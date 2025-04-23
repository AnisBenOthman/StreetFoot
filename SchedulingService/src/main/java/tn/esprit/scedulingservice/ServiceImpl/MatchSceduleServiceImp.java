package tn.esprit.scedulingservice.ServiceImpl;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.scedulingservice.Entities.MatchSchedule;
import tn.esprit.scedulingservice.Repositories.MatchSceduleRepository;
import tn.esprit.scedulingservice.Service.MatchSceduleService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MatchSceduleServiceImp implements MatchSceduleService {
    MatchSceduleRepository matchSceduleRepository;

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
}
