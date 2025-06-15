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

    public long getTournamentId() {
        return tournamentId;
    }
    public void setTournamentId(long tournamentId) {
        this.tournamentId = tournamentId;
    }

    // Getter & Setter pour roundNumber
    public Integer getRoundNumber() {
        return roundNumber;
    }
    public void setRoundNumber(Integer roundNumber) {
        this.roundNumber = roundNumber;
    }

    // Getter & Setter pour roundDate
    public LocalDate getRoundDate() {
        return roundDate;
    }
    public void setRoundDate(LocalDate roundDate) {
        this.roundDate = roundDate;
    }

    // Getter & Setter pour status
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

}
