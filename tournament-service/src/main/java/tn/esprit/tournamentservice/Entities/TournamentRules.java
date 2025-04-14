package tn.esprit.tournamentservice.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TournamentRules extends BaseEntity{
    // 1. Tournament type: CHAMPIONSHIP system or GROUP_STAGE.
    @Enumerated(EnumType.STRING)
    TournamentType tournamentType;
    // 2. For championship system: mode is either HOME_ONLY or HOME_AWAY.
    @Enumerated(EnumType.STRING)
    ChampionshipMode championshipMode;
    // 3. Applicable when tournamentType == group_stage
    Integer numberOfGroups;
    Integer teamsPerGroup;
    @Min(value = 4, message = "Championship must have at least 4 teams")
    @Max(value = 32, message = "championship must have at most 32 teams")
    Integer numberOfTeams;
    //Team structure
    int mainRoster;
    int matchLineup;
    int substitutes;
    //Match rules
    boolean isOverTimeRequired;
    Integer overTimeDuration;
    int matchDuration;
    // break duration
    int halftime;
    boolean videoReview;
@Transient
    Integer getNumberOfTeamsForGroupStage(){
        if(tournamentType == TournamentType.GROUP_STAGE && numberOfGroups != null && numberOfTeams != null){
            return numberOfTeams * numberOfGroups;
        }
        return null;
    }


}
