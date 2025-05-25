package tn.esprit.tournamentservice.Entities;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Team extends BaseEntity{
    @NotBlank(message = "Team name is required")
    String name;
    List<String> homeMatches;
    List<String> awayMatches;
}
