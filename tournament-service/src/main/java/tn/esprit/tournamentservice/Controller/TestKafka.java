package tn.esprit.tournamentservice.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test-kafka")
@RequiredArgsConstructor
public class TestKafka {
    private final KafkaTemplate<String,Object> kafkaTemplate;
    @GetMapping("send")
    public String send(){
        kafkaTemplate.send("tournament.generated","Hello from tournament service");
        return "message sent successfully";
    }
}
