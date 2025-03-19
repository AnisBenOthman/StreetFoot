package tn.esprit.scedulingservice.Entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rounds")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MatchSchedule extends BaseEntity{
    String roundId;
    String Stadium;
    long homeTeamId;
    long awayTeamId;
    Status status;
}
