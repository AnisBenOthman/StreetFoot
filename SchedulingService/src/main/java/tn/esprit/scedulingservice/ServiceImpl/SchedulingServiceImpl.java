package tn.esprit.scedulingservice.ServiceImpl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.scedulingservice.DTO.SchedulingRequest;
import tn.esprit.scedulingservice.Entities.MatchSchedule;
import tn.esprit.scedulingservice.Entities.Round;
import tn.esprit.scedulingservice.Entities.Status;
import tn.esprit.scedulingservice.Repositories.MatchSceduleRepository;
import tn.esprit.scedulingservice.Repositories.RoundRepository;
import tn.esprit.scedulingservice.Service.SchedulingService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class SchedulingServiceImpl implements SchedulingService {
    RoundRepository roundRepository;
    MatchSceduleRepository matchSceduleRepository;

    @Override
    public void generateScheduling(SchedulingRequest schedulingRequest) {
        if ("GROUP_STAGE".equalsIgnoreCase(schedulingRequest.tournamentType())) {
            generateGroupStageSchedule(schedulingRequest);
        } else if ("CHAMPIONSHIP".equalsIgnoreCase(schedulingRequest.tournamentType())) {
            generateChampionshipSchedule(schedulingRequest);

        } else {
            throw new IllegalArgumentException("Invalid tournament type :" + schedulingRequest.tournamentType());
        }
    }

    @Override
    public void generateChampionshipSchedule(SchedulingRequest schedulingRequest) {
        roundRepository.deleteAll();
        matchSceduleRepository.deleteAll();
        int numberOfTeams = schedulingRequest.numberOfTeams();
        int numberOfRounds = numberOfTeams - 1;
        int matchesPerRound = numberOfTeams / 2;
        List<Integer> teamsId = new ArrayList<>(schedulingRequest.teams());
        LocalDate currentRoundDate = schedulingRequest.startDate();
        LocalDate tournamentEndDate = schedulingRequest.endDate();


        for (int roundNum = 1; roundNum <= numberOfRounds; roundNum++) {
            log.info("les équipes du round n°" +roundNum +  "sont : {}",teamsId);

            Round roundSchedule = new Round();
            roundSchedule.setTournamentId(schedulingRequest.tournamentId());
            roundSchedule.setRoundNumber(roundNum);
            roundSchedule.setRoundDate(currentRoundDate);
            roundSchedule.setStatus(Status.PLANNED);
            roundRepository.save(roundSchedule);
            List<MatchSchedule> roundMatches = new ArrayList<>();
            for (int match = 0; match < matchesPerRound; match++) {
                int homeIndex = match;
                int awayIndex = numberOfTeams - match - 1;
                MatchSchedule matchSchedule = new MatchSchedule();
                matchSchedule.setRoundId(roundSchedule.getId());
                matchSchedule.setHomeTeamId(teamsId.get(homeIndex));
                matchSchedule.setAwayTeamId(teamsId.get(awayIndex));
                roundMatches.add(matchSchedule);


            }

            matchSceduleRepository.saveAll(roundMatches);
            Integer pivot = teamsId.remove(0);
            Integer last = teamsId.remove(teamsId.size() - 1);
            teamsId.add(0, last);
            teamsId.add(0, pivot);

             currentRoundDate = currentRoundDate.plusDays(schedulingRequest.roundFrequency());
        }
    }

    @Override
    public void generateGroupStageSchedule(SchedulingRequest schedulingRequest) {
        int numberOfGroups = schedulingRequest.numberOfGroups();
        List<Integer> allTeams = new ArrayList<>(schedulingRequest.teams());
        Collections.shuffle(allTeams);
        int teamsPerGroup = schedulingRequest.numberOfTeams() / numberOfGroups;
        LocalDate currentDate = schedulingRequest.startDate();

        // Step 1: Split into groups
        List<List<Integer>> groups = new ArrayList<>();
        for (int i = 0; i < numberOfGroups; i++) {
            groups.add(allTeams.subList(i * teamsPerGroup, (i + 1) * teamsPerGroup));
        }

        int numberOfRounds = teamsPerGroup - 1;

        // Step 2: Generate rounds across all groups
        for (int roundIndex = 0; roundIndex < numberOfRounds; roundIndex++) {
            // One global round per date
            Round globalRound = new Round();
            globalRound.setTournamentId(schedulingRequest.tournamentId());
            globalRound.setRoundNumber(roundIndex + 1);
            globalRound.setRoundDate(currentDate);
            globalRound.setStatus(Status.PLANNED);
            roundRepository.save(globalRound);

            // Step 3: For each group, generate match pairings for this round
            for (List<Integer> group : groups) {
                List<Integer> rotation = new ArrayList<>(group);
                Integer fixed = rotation.get(0);
                List<Integer> rotating = rotation.subList(1, rotation.size());
                Collections.rotate(rotating, -roundIndex);
                List<Integer> currentRound = new ArrayList<>();
                currentRound.add(fixed);
                currentRound.addAll(rotating);

                int matchCount = teamsPerGroup / 2;

                for (int i = 0; i < matchCount; i++) {
                    Integer home = currentRound.get(i);
                    Integer away = currentRound.get(teamsPerGroup - 1 - i);

                    MatchSchedule match = new MatchSchedule();
                    match.setRoundId(globalRound.getId());
                    match.setHomeTeamId(home);
                    match.setAwayTeamId(away);
                    matchSceduleRepository.save(match);
                }
            }

            currentDate = currentDate.plusDays(schedulingRequest.roundFrequency()); // Next global round date
        }
    }


}
