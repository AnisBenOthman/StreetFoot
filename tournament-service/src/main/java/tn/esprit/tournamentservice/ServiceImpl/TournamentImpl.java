package tn.esprit.tournamentservice.ServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.tournamentservice.Entities.Tournament;


import tn.esprit.tournamentservice.Exception.TournamentException;
import tn.esprit.tournamentservice.Repositories.TournamentRepository;
import tn.esprit.tournamentservice.Service.TournamentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@AllArgsConstructor
@Service
public class TournamentImpl implements TournamentService {

    private static final Logger log = LoggerFactory.getLogger(TournamentImpl.class);
    TournamentRepository tournamentRepository;
    @Override
    public Tournament add(Tournament tournament) {
        try {
            return tournamentRepository.save(tournament);
        } catch (Exception e) {
            log.error("Failed to save tournament: {}", tournament, e);
            throw new TournamentException("Failed to save tournament", e);
        }
    }

    @Override
    public Tournament update(Tournament tournament, Integer id) {
        try {
            return tournamentRepository.findById(id).map(exstingTournament -> {
                exstingTournament.setTournamentRules(tournament.getTournamentRules());
                exstingTournament.setDescription(tournament.getDescription());
                exstingTournament.setName(tournament.getName());
                exstingTournament.setStatus(tournament.getStatus());
                exstingTournament.setEndDate(tournament.getEndDate());
                exstingTournament.setStartDate(tournament.getStartDate());
                return tournamentRepository.save(exstingTournament);
            }).orElseThrow(() -> new EntityNotFoundException("Tournament with id " + id + "not found"));



        } catch (Exception e) {
            log.error("Failed to update tournament with id {}: {}", id, tournament, e);
            return null;
        }
    }

    @Override
    public Tournament retrieveById(Integer id) {
        try{
            return tournamentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(("Tournament with id" + id + "not found")));
        }catch (Exception e){
            log.error("Failed to fetch tournament with id {}: ",id, e);
            return null;
        }

    }

    @Override
    public void delete(Integer id) {
        try {
             if(!tournamentRepository.existsById(id)){
                 log.warn("tournament with id {} not found, deletion skipped",id);
                 return;
             }
             tournamentRepository.deleteById(id);
             log.info("Successful deleted tournament with id {}", id);
        }catch (Exception e){
            log.error("Failed to delete tournament with id {}: ",id, e);

        }

    }

    @Override
    public List<Tournament> getAll() {
        return tournamentRepository.findAll();
    }
}
