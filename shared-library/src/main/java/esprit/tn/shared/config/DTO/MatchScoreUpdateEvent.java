package esprit.tn.shared.config.DTO;

public record MatchScoreUpdateEvent(Long tournamentId, String roundId, String matchId, Long homeTeamId, Long awayTeamId, Integer homeScore, Integer awayScore, Long winnerTeamId ) {
}
