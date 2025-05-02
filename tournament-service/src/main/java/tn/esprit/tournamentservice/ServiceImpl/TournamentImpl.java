package tn.esprit.tournamentservice.ServiceImpl;

import esprit.tn.shared.config.DTO.TournamentCreatedEvent;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import tn.esprit.tournamentservice.DTO.SchedulingRequest;
import tn.esprit.tournamentservice.Entities.Sport;
import tn.esprit.tournamentservice.Entities.Status;
import tn.esprit.tournamentservice.Entities.Tournament;


import tn.esprit.tournamentservice.Entities.TournamentType;
import tn.esprit.tournamentservice.Exception.TournamentException;
import tn.esprit.tournamentservice.FeignClient.SchedulingClient;
import tn.esprit.tournamentservice.Repositories.TournamentRepository;
import tn.esprit.tournamentservice.Service.TournamentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import esprit.tn.shared.config.EventEnvelop;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
@Slf4j
public class TournamentImpl implements TournamentService {


    final TournamentRepository tournamentRepository;
    final SchedulingClient schedulingClient;
    final KafkaTemplate<String,Object> kafkaTemplate;


    @Override
    public Tournament add(Tournament tournament) {
        try {
            Tournament saved = tournamentRepository.save(tournament);
            TournamentCreatedEvent evt = new TournamentCreatedEvent(saved.getId(),saved.getTournamentRules().getTournamentType().toString(),saved.getTournamentRules().getChampionshipMode().toString(),saved.getTournamentRules().getNumberOfTeams(),saved.getTournamentRules().getNumberOfGroups(),saved.getTournamentRules().getTeamsPerGroup(),saved.getParticipatingTeamIds(),saved.getTournamentRules().getRoundFrequency(),saved.getStartDate(),saved.getEndDate());
             EventEnvelop<TournamentCreatedEvent> envelop = EventEnvelop.<TournamentCreatedEvent>builder()
                     .eventId(UUID.randomUUID().toString())
                    .type("TOURNAMENT_CREATED")
                    .payload(evt)
                    .timestamp(Instant.now())
                    .build();


            kafkaTemplate.send("tournament.generated", envelop );

            return saved;

        } catch (Exception e) {
            log.error("Failed to save tournament: {}", tournament, e);
            throw new TournamentException("Failed to save tournament", e);
        }
    }


    @Override
    public Tournament update(Tournament tournament, Long id) {
        try {
            return tournamentRepository.findById(id).map(exstingTournament -> {
                exstingTournament.setTournamentRules(tournament.getTournamentRules());
                exstingTournament.setDescription(tournament.getDescription());
                exstingTournament.setName(tournament.getName());
                exstingTournament.setStatus(tournament.getStatus());
                exstingTournament.setEndDate(tournament.getEndDate());
                exstingTournament.setStartDate(tournament.getStartDate());
                exstingTournament.setParticipatingTeamIds(tournament.getParticipatingTeamIds());
                exstingTournament.setAwards(tournament.getAwards());
                exstingTournament.setSport(tournament.getSport());
                exstingTournament.setUserId(tournament.getUserId());
                return tournamentRepository.save(exstingTournament);
            }).orElseThrow(() -> new EntityNotFoundException("Tournament with id " + id + "not found"));


        } catch (Exception e) {
            log.error("Failed to update tournament with id {}: {}", id, tournament, e);
            return null;
        }
    }

    @Override
    public Tournament retrieveById(Long id) {
        try {
            return tournamentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(("Tournament with id" + id + "not found")));
        } catch (Exception e) {
            log.error("Failed to fetch tournament with id {}: ", id, e);
            return null;
        }

    }

    @Override
    public void delete(Long id) {
        try {
            if (!tournamentRepository.existsById(id)) {
                log.warn("tournament with id {} not found, deletion skipped", id);
                return;
            }
            tournamentRepository.deleteById(id);
            log.info("Successful deleted tournament with id {}", id);
        } catch (Exception e) {
            log.error("Failed to delete tournament with id {}: ", id, e);

        }

    }

    @Override
    public void deleteAll() {
        tournamentRepository.deleteAll();
    }

    @Override
    public List<Tournament> getAll() {
        return tournamentRepository.findAll();
    }

    @Override
    public List<Tournament> findByCreatedByUserId(Long userId) {
        return tournamentRepository.findByUserId(userId);
    }

    @Override
    public List<Tournament> findByStatus(Status status) {
        return tournamentRepository.findByStatus(status);
    }

    @Override
    public Tournament registerTeams(Long tournamentId, List<Integer> teamsId) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow(() -> new EntityNotFoundException(("Tournament with id" + tournamentId + "not found")));
        tournament.getParticipatingTeamIds().addAll(teamsId);
        tournamentRepository.save(tournament);
        if(tournament.isReadyForScheduling()){
            SchedulingRequest req = new SchedulingRequest(tournament.getId(),tournament.getTournamentRules().getTournamentType().toString(),tournament.getTournamentRules().getChampionshipMode().toString(),tournament.getTournamentRules().getNumberOfTeams(),tournament.getTournamentRules().getNumberOfGroups(),tournament.getTournamentRules().getTeamsPerGroup(),tournament.getParticipatingTeamIds(),tournament.getTournamentRules().getRoundFrequency(),tournament.getStartDate(),tournament.getEndDate());
            schedulingClient.generateScheduling(req);
        }
        return tournament;
    }

    @Override
    public List<Tournament> findBySport(Sport sport) {
        return tournamentRepository.findBySport(sport);
    }

    @Override
    public Tournament participateINTournament(Long tournamentId, Integer teamId) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow(() -> new EntityNotFoundException(("Tournament with id" + tournamentId + "not found")));
        List<Integer> teamsIds = tournament.getParticipatingTeamIds();
        if(teamsIds == null){
            teamsIds = new ArrayList<>();
        }
        if(tournament.getTournamentRules().getTournamentType() == TournamentType.CHAMPIONSHIP){
            if (teamsIds.size() >= tournament.getTournamentRules().getNumberOfTeams()) {
                throw new IllegalStateException("Le nombre maximal d'équipes a déjà été atteint");
            }
        }
        if(tournament.getTournamentRules().getTournamentType() == TournamentType.GROUP_STAGE){
            if (teamsIds.size() >=  tournament.getTournamentRules().getNumberOfGroups() * tournament.getTournamentRules().getTeamsPerGroup()) {
                throw new IllegalStateException("Le nombre maximal d'équipes a déjà été atteint");
            }

        }


        if(!teamsIds.contains(teamId)){
            teamsIds.add(teamId);
        }else{
            throw new IllegalStateException("Cette équipe est déja inscrite au tournoi !");
        }
        Tournament saved = tournamentRepository.save(tournament);
        if(saved.isReadyForScheduling()){
            saved.setStatus(Status.PLANNED);
            tournamentRepository.save(saved);
            SchedulingRequest req = new SchedulingRequest(saved.getId(),saved.getTournamentRules().getTournamentType().toString(),saved.getTournamentRules().getChampionshipMode().toString(),saved.getTournamentRules().getNumberOfTeams(),tournament.getTournamentRules().getNumberOfGroups(),saved.getTournamentRules().getTeamsPerGroup(),saved.getParticipatingTeamIds(),saved.getTournamentRules().getRoundFrequency(),saved.getStartDate(),saved.getEndDate());
            log.info("requete sent is : {}", req);
            schedulingClient.generateScheduling(req);
        }
        return saved;

    }
}
