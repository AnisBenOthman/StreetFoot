package esprit.tn.standingservice.Entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "standings")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Standings extends BaseEntite{
    Long tournamentId;
    Long teamId;
    int goalScored=0;
    int goalConceded=0;
    int matchesPlayed=0;
    int wins=0;
    int draws=0;
    int losses=0;
    int points=0;
    public Standings(Long tournamentId, Long teamId) {
        this.tournamentId   = tournamentId;
        this.teamId         = teamId;
        this.matchesPlayed = 0;
        this.wins           = 0;
        this.draws          = 0;
        this.losses         = 0;
        this.goalScored     = 0;
        this.goalConceded   = 0;
        this.points         = 0;
    }
}
