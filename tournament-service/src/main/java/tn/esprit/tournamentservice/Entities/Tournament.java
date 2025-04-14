package tn.esprit.tournamentservice.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message = "Tournament name is required")
    String name;
    String description;
    @NotNull(message = "start date is required")
    LocalDate startDate;
    @NotNull(message = "end date is required")
    LocalDate endDate;
    @NotNull(message = "deadline registration is required")
    LocalDate teamRegistrationDeadline;

    @Enumerated(EnumType.STRING)
    Status status;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tournament_rule_id")
     TournamentRules tournamentRules;
    String Awards;

    @AssertTrue(message = "End date must be after start date")
    boolean isEndDateAfterStartDate(){
        return  startDate != null && endDate != null && endDate.isAfter(startDate);
    }




}
