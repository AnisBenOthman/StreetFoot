package tn.esprit.scedulingservice.Entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "rounds")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Round extends BaseEntity{
    long tournamentId;
    Integer roundNumber;
    LocalDate roundDate;
    Status status;

}
