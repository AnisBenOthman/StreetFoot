package tn.esprit.tournamentservice.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tn.esprit.tournamentservice.Entities.Tournament;

@FeignClient(name="ScedulingService")
public interface SchedulingClient {
    @PostMapping("schedulingservice/addscedhule")
    public  String addScedule(@RequestBody Tournament tournament);
}
