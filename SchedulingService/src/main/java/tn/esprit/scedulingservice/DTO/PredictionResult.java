package tn.esprit.scedulingservice.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record PredictionResult(String predictedLabel, Map<String,Float> probabilites) {
}
