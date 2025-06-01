package esprit.tn.shared.config.DTO;

public record StandingDTO(Long tournamentId, Long teamId, int played, int wins, int loses, int draws, int goalsFor, int conceded, int points) {
}
