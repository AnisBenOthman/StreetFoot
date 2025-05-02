package esprit.tn.shared.config.DTO;

public record TeamRegistredEvent(Long tournamentId, Long teamId, int regitredCount, int neededCount) {
}
