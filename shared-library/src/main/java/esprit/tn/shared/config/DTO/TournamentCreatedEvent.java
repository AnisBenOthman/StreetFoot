package esprit.tn.shared.config.DTO;

import java.time.LocalDate;
import java.util.List;

public record TournamentCreatedEvent(long tournamentId, String tournamentType, String championshipMode, Integer numberOfTeams, Integer numberOfGroups, Integer teamsPerGroup, List<Integer> teams, int roundFrequency, LocalDate startDate, LocalDate endDate) {
}
