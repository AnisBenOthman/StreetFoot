package tn.esprit.tournamentservice.DTO;

import java.time.LocalDate;
import java.util.List;

public record SchedulingRequest(long tournamentId, String tournamentType, String championshipMode, Integer numberOfTeams, Integer numberOfGroups, Integer teamsPerGroup, List<Integer> teams, int roundFrequency, LocalDate startDate, LocalDate endDate) {
}
