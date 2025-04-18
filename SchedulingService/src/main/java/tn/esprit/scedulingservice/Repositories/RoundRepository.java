package tn.esprit.scedulingservice.Repositories;

import tn.esprit.scedulingservice.Entities.Round;

import java.util.List;

public interface RoundRepository extends BaseRepository<Round,String> {
    List<Round> findByTournamentId(Long tournamentId);
}
