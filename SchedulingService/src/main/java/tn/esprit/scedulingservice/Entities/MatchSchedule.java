package tn.esprit.scedulingservice.Entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "matchSchedules")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MatchSchedule extends BaseEntity{
    String roundId;
    String stadium;
    long homeTeamId;
    long awayTeamId;
    Integer homeScore;
    Integer awayScore;
    Long winnerTeamId;
    public void computeResult() {
        if (homeScore == null || awayScore == null) {
            winnerTeamId = null;

        } else if (homeScore > awayScore) {
            winnerTeamId = homeTeamId;

        } else if (awayScore > homeScore) {
            winnerTeamId = awayTeamId;

        } else {
            winnerTeamId = null;

        }
    }
    public String getRoundId() { return roundId; }
    public void setRoundId(String roundId) { this.roundId = roundId; }

    public String getStadium() { return stadium; }
    public void setStadium(String stadium) { this.stadium = stadium; }

    public long getHomeTeamId() { return homeTeamId; }
    public void setHomeTeamId(long homeTeamId) { this.homeTeamId = homeTeamId; }

    public long getAwayTeamId() { return awayTeamId; }
    public void setAwayTeamId(long awayTeamId) { this.awayTeamId = awayTeamId; }

}
