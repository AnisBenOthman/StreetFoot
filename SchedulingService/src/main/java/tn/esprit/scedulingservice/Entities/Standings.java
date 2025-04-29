package tn.esprit.scedulingservice.Entities;

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
public class Standings extends BaseEntity{
    Long tournamentId;
    Integer teamId;

    int matchesPlayed;
    int wins;
    int draws;
    int losses;
    int points;
}
