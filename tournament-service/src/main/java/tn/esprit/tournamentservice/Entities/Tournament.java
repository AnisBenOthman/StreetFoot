package tn.esprit.tournamentservice.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tournament extends BaseEntity{
    String name;
    String description;
    LocalDate startDate;
    LocalDate endDate;

    @Enumerated(EnumType.STRING)
    Status status;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tournament_rule_id")
     TournamentRules tournamentRules;
    String Awards;




}
