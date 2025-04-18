package tn.esprit.scedulingservice.Repositories;

import tn.esprit.scedulingservice.Entities.MatchSchedule;

import java.util.List;

public interface MatchSceduleRepository extends BaseRepository<MatchSchedule,String> {
    List<MatchSchedule> findByRoundId(String roundId);
}
