package esprit.tn.standingservice.Entities;

import esprit.tn.shared.config.DTO.StandingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StandingMapper {
    @Mapping(source = "played", target = "matchesPlayed")
    @Mapping(source = "wins", target = "wins")
    @Mapping(source = "loses", target = "losses")
    @Mapping(source = "draws", target = "draws")
    @Mapping(source = "goalsFor", target = "goalScored")
    @Mapping(source = "conceded", target = "goalConceded")
    @Mapping(source = "points", target = "points")
    @Mapping(source = "tournamentId", target = "tournamentId")
    @Mapping(source = "teamId", target = "teamId")
    Standings stangindDTOToStanding(StandingDTO standingDTO);
    @Mapping(source = "matchesPlayed", target = "played")
    @Mapping(source = "wins", target = "wins")
    @Mapping(source = "losses", target = "loses")
    @Mapping(source = "draws", target = "draws")
    @Mapping(source = "goalScored", target = "goalsFor")
    @Mapping(source = "goalConceded", target = "conceded")
    @Mapping(source = "points", target = "points")
    @Mapping(source = "tournamentId", target = "tournamentId")
    @Mapping(source = "teamId", target = "teamId")
    StandingDTO standingToStandingDTO(Standings standings);
}
