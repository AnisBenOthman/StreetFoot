package tn.esprit.scedulingservice.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tn.esprit.scedulingservice.DTO.MatchDataInput;
import tn.esprit.scedulingservice.DTO.PredictionResult;

@FeignClient(name = "model-service", url = "http://127.0.0.1:5000")
public interface ModelClient {
    @PostMapping("/predict")
    public PredictionResult predict_match_result(@RequestBody MatchDataInput matchDataInput);
}
