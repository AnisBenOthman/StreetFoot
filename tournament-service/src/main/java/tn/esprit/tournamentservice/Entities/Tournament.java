package tn.esprit.tournamentservice.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

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
    @JsonIgnore
    Status status = Status.PENDING;
    @OneToOne()
    @JoinColumn(name = "tournament_rule_id")
    TournamentRules tournamentRules;
    @ElementCollection
    List<Integer> participatingTeamIds;
    String Awards;
    Long userId;

    @AssertTrue(message = "End date must be after start date")
    public boolean isEndDateAfterStartDate(){
        return  startDate != null && endDate != null && endDate.isAfter(startDate);
    }
    @AssertTrue(message = "Team registration deadline must be at least 3 weeks before the tournament starts")
    public boolean isTeamRegistrationDeadlineValid() {
        return teamRegistrationDeadline != null && startDate != null &&
                teamRegistrationDeadline.isBefore(startDate.minusWeeks(3));
    }
    @JsonIgnore
    public boolean isReadyForScheduling() {
        return participatingTeamIds != null
                && tournamentRules != null
                || participatingTeamIds.size() == tournamentRules.getNumberOfTeams()|| participatingTeamIds.size() == tournamentRules.getNumberOfTeamsForGroupStage();
    }




}
