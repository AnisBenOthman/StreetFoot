package tn.esprit.scedulingservice.Controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.scedulingservice.DTO.PredictionResult;
import tn.esprit.scedulingservice.ServiceImpl.PredictionServiceImpl;

import java.util.Map;

@RestController
@RequestMapping("prediction")
@RequiredArgsConstructor
public class PredictionController {
    @GetMapping("getfeatures/{matchId}")
    public Map<String,Object> prepareFeatures(@PathVariable String matchId) {
        Map<String,Object> features = predictionService.prepareFeatures(matchId);
        return features;

    }
@GetMapping("predict/{matchId}")
    public ResponseEntity<PredictionResult>  getPrediction(@PathVariable String matchId) {
        try{
            PredictionResult result = predictionService.getPrediction(matchId);
            return ResponseEntity.ok(result);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    final PredictionServiceImpl predictionService;
}
