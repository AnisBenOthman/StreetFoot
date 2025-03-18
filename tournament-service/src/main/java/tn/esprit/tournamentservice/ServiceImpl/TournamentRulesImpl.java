package tn.esprit.tournamentservice.ServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.tournamentservice.Entities.Tournament;
import tn.esprit.tournamentservice.Entities.TournamentRules;
import tn.esprit.tournamentservice.Exception.TournamentException;
import tn.esprit.tournamentservice.Repositories.TournamentRulesRepository;
import tn.esprit.tournamentservice.Service.TournamentRulesService;
import tn.esprit.tournamentservice.Service.TournamentService;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class TournamentRulesImpl implements TournamentRulesService {

    TournamentRulesRepository tournamentRulesRepository;

    @Override
    public TournamentRules add(TournamentRules tournamentRules) {
        try {
            TournamentRules savedTournamentRules = tournamentRulesRepository.save(tournamentRules);
            return savedTournamentRules;
        } catch (Exception e) {
            log.error("Failed to save tournament: {}", tournamentRules, e);
            throw new TournamentException("Failed to save tournamentRules", e);
        }
    }

    @Override
    public TournamentRules update(TournamentRules tournamentRules, Integer id) {

        return tournamentRulesRepository.findById(id).map(existingTournamentRules -> {
            existingTournamentRules.setChampionshipMode(tournamentRules.getChampionshipMode());
            existingTournamentRules.setHalftime(tournamentRules.getHalftime());
            existingTournamentRules.setMainRoster(tournamentRules.getMainRoster());
            existingTournamentRules.setMatchLineup(tournamentRules.getMatchLineup());
            existingTournamentRules.setMatchDuration(tournamentRules.getMatchDuration());
            existingTournamentRules.setSubstitutes(tournamentRules.getSubstitutes());
            existingTournamentRules.setNumberOfTeams(tournamentRules.getNumberOfTeams());
            existingTournamentRules.setTeamsPerGroup(tournamentRules.getTeamsPerGroup());
            existingTournamentRules.setOverTimeRequired(tournamentRules.isOverTimeRequired());
            existingTournamentRules.setOverTimeDuration(tournamentRules.getOverTimeDuration());
            return tournamentRulesRepository.save(existingTournamentRules);
        }).orElseThrow(() -> new EntityNotFoundException("TournamentRules with id {} not found" + id));


    }

    @Override
    public TournamentRules retrieveById(Integer id) {
        return tournamentRulesRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("TournamentRules with id" + id + "not found"));
    }

    @Override
    public void delete(Integer id) {
        if (!tournamentRulesRepository.existsById(id)) {
            log.warn("tournamentRules with id {} not found, deletion skipped", id);
            return;
        }
        tournamentRulesRepository.deleteById(id);
    }

}

