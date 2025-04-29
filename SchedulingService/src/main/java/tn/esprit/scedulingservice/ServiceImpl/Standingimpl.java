package tn.esprit.scedulingservice.ServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.scedulingservice.Entities.Standings;
import tn.esprit.scedulingservice.Repositories.MatchSceduleRepository;
import tn.esprit.scedulingservice.Repositories.StandingRepository;
import tn.esprit.scedulingservice.Service.StandingService;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class Standingimpl implements StandingService {
    private final MatchSceduleRepository matchRepo;
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
    public List<Standings> findByTournamentId(Long tournamentId) {
        return standingRepository.findByTournamentId(tournamentId);
    }

    @Override
    public Standings findByTournamentIdAndTeamId(Long tournamentId, Integer teamId) {
        return standingRepository.findByTournamentIdAndTeamId(tournamentId,teamId);
    }
}
