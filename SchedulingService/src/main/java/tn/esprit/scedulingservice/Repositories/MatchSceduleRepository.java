package tn.esprit.scedulingservice.Repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import tn.esprit.scedulingservice.Entities.MatchSchedule;

import java.util.List;

public interface MatchSceduleRepository extends BaseRepository<MatchSchedule,String> {
    Page<MatchSchedule> findAll(Pageable pageable);
    List<MatchSchedule> findByRoundId(String roundId);
    List<MatchSchedule> findByHomeTeamId(long homeTeamId);
    List<MatchSchedule> findByAwayTeamId(long awayTeamId);
    @Query("{'homeTeamId': ?0, 'awayTeamId': ?1}")
    List<MatchSchedule> findHeadToHead(long teamA, long teamB, Pageable pageable);
    @Query("{'homeTeamId': ?0}")
    List<MatchSchedule> findLastThreeHomeMatches(long teamA, Pageable pageable);
    @Query("{'awayTeamId': ?0}")
    List<MatchSchedule> findLastThreeAwayMatches(long teamB, Pageable pageable);

}
