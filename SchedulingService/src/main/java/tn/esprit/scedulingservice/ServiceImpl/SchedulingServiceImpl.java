package tn.esprit.scedulingservice.ServiceImpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.scedulingservice.DTO.SchedulingRequest;
import tn.esprit.scedulingservice.Entities.MatchSchedule;
import tn.esprit.scedulingservice.Entities.Round;
import tn.esprit.scedulingservice.Repositories.MatchSceduleRepository;
import tn.esprit.scedulingservice.Repositories.RoundRepository;
import tn.esprit.scedulingservice.Service.SchedulingService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Service
@AllArgsConstructor
public class SchedulingServiceImpl implements SchedulingService {
    RoundRepository roundRepository;
    MatchSceduleRepository matchSceduleRepository;
    @Override
    public void generateScheduling(SchedulingRequest schedulingRequest) {
        if("GROUP_STAGE".equalsIgnoreCase(schedulingRequest.tournamentType())){
            generateGroupStageSchedule(schedulingRequest);
        } else if ("CHAMPIONSHIP".equalsIgnoreCase(schedulingRequest.tournamentType())) {
            generateChampionshipSchedule(schedulingRequest);

        }else {
            throw new IllegalArgumentException("Invalid tournament type :" + schedulingRequest.tournamentType());
        }
    }

    @Override
    public void generateChampionshipSchedule(SchedulingRequest schedulingRequest) {
        int numberOfTeams = schedulingRequest.numberOfTeams();
        int numberOfRounds = numberOfTeams - 1;
        int matchesPerRound = numberOfTeams / 2;
        List<Long> teamsId = new ArrayList<>();
        LocalDate currentRoundDate = schedulingRequest.startDate();
        LocalDate tournamentEndDate = schedulingRequest.endDate();

        String mode = schedulingRequest.championshipMode();
        for(int round = 0; round< numberOfRounds; round++){
            List<MatchSchedule> roundMatches = new ArrayList<>();
            Round roundSchedule = new Round();
            roundSchedule.setTournamentId(schedulingRequest.tournamentId());
            roundSchedule.setRoundNumber(round);
            roundSchedule.setRoundDate(currentRoundDate);

            for( int match=0; match<matchesPerRound; match++){
                int homeIndex = match;
                int awayIndex = numberOfTeams - match - 1;
                MatchSchedule matchSchedule = new MatchSchedule();
                matchSchedule.setHomeTeamId(schedulingRequest.teams().get(homeIndex));
                matchSchedule.setAwayTeamId(schedulingRequest.teams().get(awayIndex));



            }
        }
    }

    @Override
    public void generateGroupStageSchedule(SchedulingRequest schedulingRequest) {

    }

}
