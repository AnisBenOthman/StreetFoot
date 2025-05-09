package esprit.tn.shared.config.DTO;

import java.util.List;

public record ScheduleGeneratedEvent(Long tournamentId, List<Integer> teamsId, List<String> roundIds) {
}
