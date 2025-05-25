package tn.esprit.tournamentservice.Service;

import tn.esprit.tournamentservice.Entities.Team;

public interface TeamService extends BaseService<Team, Long>{
    Team findbyname(String name);

}
