package tn.esprit.tournamentservice.Repositories;

import tn.esprit.tournamentservice.Entities.Team;

public interface TeamRepository extends BaseRepository<Team, Long> {
    Team findByName(String name);
}
