package tn.esprit.scedulingservice.ServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.scedulingservice.Entities.Round;
import tn.esprit.scedulingservice.Repositories.RoundRepository;
import tn.esprit.scedulingservice.Service.RoundService;

import java.util.List;
import java.util.Optional;
@Service
@AllArgsConstructor
public class RoundServiceImpl implements RoundService {
    RoundRepository roundRepository;
    @Override
    public Optional<Round> retrieveById(String s) {

        return roundRepository.findById(s);
    }

    @Override
    public List<Round> retrieveAll() {

        return roundRepository.findAll();
    }

    @Override
    public Round add(Round object) {

        return roundRepository.save(object);
    }

    @Override
    public Round update(Round object, String s) {

        return roundRepository.findById(s).map(round -> {
            round.setRoundDate(object.getRoundDate());
            round.setRoundNumber(object.getRoundNumber());
            round.setTournamentId(object.getTournamentId());
            return roundRepository.save(round);
        }).orElseThrow(() -> new EntityNotFoundException("Round with id " + s + "not found"));
    }

    @Override
    public void delete(String s) {
        roundRepository.deleteById(s);

    }

    @Override
    public void deleteAll() {
        roundRepository.deleteAll();

    }

    @Override
    public List<Round> findAllRoundByTournament(Long tournamentId) {
        return roundRepository.findByTournamentId(tournamentId);
    }
}
