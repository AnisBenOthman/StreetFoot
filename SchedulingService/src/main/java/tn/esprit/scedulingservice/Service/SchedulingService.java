package tn.esprit.scedulingservice.Service;

import tn.esprit.scedulingservice.DTO.SchedulingRequest;

public interface SchedulingService {
    void generateScheduling(SchedulingRequest schedulingRequest);
    void generateChampionshipSchedule(SchedulingRequest schedulingRequest);
    void generateGroupStageSchedule(SchedulingRequest schedulingRequest);

}
