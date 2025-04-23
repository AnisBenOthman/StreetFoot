package tn.esprit.tournamentservice.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tn.esprit.tournamentservice.DTO.SchedulingRequest;
import tn.esprit.tournamentservice.Entities.Tournament;

@FeignClient(name="sceduling-service")
public interface SchedulingClient {
    @PostMapping("scheduling/generateschedule")
    public void generateScheduling(@RequestBody SchedulingRequest schedulingRequest);
}
