package tn.esprit.scedulingservice.Service;

import tn.esprit.scedulingservice.Entities.MatchSchedule;

import java.util.List;

public interface MatchSceduleService extends BaseService<MatchSchedule,String> {
    List<MatchSchedule> findAllMatchByRound(String roundId);
}
