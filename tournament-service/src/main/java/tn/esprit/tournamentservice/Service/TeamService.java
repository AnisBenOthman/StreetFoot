package tn.esprit.tournamentservice.Service;

import tn.esprit.tournamentservice.Entities.Team;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface TeamService extends BaseService<Team, Long>{
    Optional<Team > findbyname(String name);

}
