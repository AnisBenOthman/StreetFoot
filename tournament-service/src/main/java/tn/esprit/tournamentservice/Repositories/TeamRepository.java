package tn.esprit.tournamentservice.Repositories;

import tn.esprit.tournamentservice.Entities.Team;

import java.util.Optional;

public interface TeamRepository extends BaseRepository<Team, Long> {


    Optional<Team> findByName(String name);
}
