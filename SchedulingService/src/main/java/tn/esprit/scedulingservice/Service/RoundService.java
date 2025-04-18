package tn.esprit.scedulingservice.Service;

import tn.esprit.scedulingservice.Entities.Round;

import java.util.List;

public interface RoundService extends BaseService<Round,String> {
    List<Round> findAllRoundByTournament(Long tournamentId);
}
