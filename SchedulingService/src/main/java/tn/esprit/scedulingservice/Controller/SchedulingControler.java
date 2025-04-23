package tn.esprit.scedulingservice.Controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.scedulingservice.DTO.SchedulingRequest;
import tn.esprit.scedulingservice.ServiceImpl.SchedulingServiceImpl;

@RestController
@RequestMapping("scheduling")
@AllArgsConstructor
public class SchedulingControler {
    @PostMapping("generateschedule")
    public ResponseEntity<?> generateScheduling(@RequestBody SchedulingRequest schedulingRequest) {
        schedulingService.generateScheduling(schedulingRequest);
        return ResponseEntity.ok("tournamenet scedule generated successfully");
    }


    SchedulingServiceImpl schedulingService;
}
