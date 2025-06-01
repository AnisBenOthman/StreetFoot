package tn.esprit.scedulingservice.ServiceImpl;

import esprit.tn.shared.config.DTO.StandingDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tn.esprit.scedulingservice.DTO.MatchDataInput;
import tn.esprit.scedulingservice.DTO.PredictionResult;
import tn.esprit.scedulingservice.Entities.MatchSchedule;
import tn.esprit.scedulingservice.Entities.Round;
import tn.esprit.scedulingservice.FeignClient.ModelClient;
import tn.esprit.scedulingservice.FeignClient.StandingsClient;
import tn.esprit.scedulingservice.Repositories.MatchSceduleRepository;
import tn.esprit.scedulingservice.Repositories.RoundRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PredictionServiceImpl {
    final MatchSceduleRepository matchSceduleRepository;
    final StandingsClient standingsClient;
    final RoundRepository roundRepository;
    final ModelClient modelClient;

    public Map<String, Object> prepareFeatures(String matchId) {
        Optional<MatchSchedule> optionalMatch = matchSceduleRepository.findById(matchId);
        int htgs = 0;
        int htgc = 0;
        int htgd = 0;
        int atgs = 0;
        int atgc = 0;
        int atgd = 0;
        int htp = 0;
        int atp = 0;
        int diffPts = 0;
        List<Integer> lastHeadToHeadNumerical = null;
        List<Integer> htlastNumerical = null;
        List<Integer> atlastNumerical = null;
        if (optionalMatch.isPresent()) {
            MatchSchedule match = optionalMatch.get();
            String roundId = match.getRoundId();
            Optional<Round> optionalRound = roundRepository.findById(roundId);
            if (optionalRound.isPresent()) {
                Round round = optionalRound.get();
                long tournamentId = round.getTournamentId();
                ResponseEntity<StandingDTO> response = standingsClient.findByTournamentIdAndTeamId(tournamentId, match.getHomeTeamId());
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    htgs = response.getBody().goalsFor();
                    htgc = response.getBody().conceded();
                    htgd = htgs - htgc;
                    htp = response.getBody().points();
                }
                ResponseEntity<StandingDTO> responseAway = standingsClient.findByTournamentIdAndTeamId(tournamentId, match.getAwayTeamId());
                if (responseAway.getStatusCode().is2xxSuccessful() && responseAway.getBody() != null) {
                    atgs = responseAway.getBody().goalsFor();
                    atgc = responseAway.getBody().conceded();
                    atp = responseAway.getBody().points();
                    atgd = atgs - atgc;
                    diffPts = htp - atp;
                }
            }
            PageRequest pageRequest = PageRequest.of(0, 3, Sort.by("date").descending());
            List<MatchSchedule> headToHead = matchSceduleRepository.findHeadToHead(match.getHomeTeamId(), match.getAwayTeamId(), pageRequest);
            PageRequest pageRequest1 = PageRequest.of(0, 3, Sort.by("date").descending());
            List<MatchSchedule> htlast = matchSceduleRepository.findLastThreeHomeMatches(match.getHomeTeamId(), pageRequest);
            PageRequest pageRequest2 = PageRequest.of(0, 3, Sort.by("date").descending());
            List<MatchSchedule> atlast = matchSceduleRepository.findLastThreeAwayMatches(match.getAwayTeamId(), pageRequest);
            List<String> lastHeadToHead = new java.util.ArrayList<>(headToHead.stream().map(m -> determineResult(m, match.getHomeTeamId())).toList());
            while (lastHeadToHead.size() < 3) lastHeadToHead.add("M");
            lastHeadToHeadNumerical = lastHeadToHead.stream().map(this::mapResultToNumerical).toList();
            List<String> htlastResult = new ArrayList<>(htlast.stream()
                    .map(m -> determineResult(m, match.getHomeTeamId()))
                    .toList());
            while (htlastResult.size() < 3) htlastResult.add("M");
            htlastNumerical = htlastResult.stream().map(this::mapResultToNumerical).toList();
            List<String> atlastResult = new ArrayList<>(atlast.stream()
                    .map(m -> determineResult(m, match.getAwayTeamId()))
                    .toList());
            while (atlastResult.size() < 3) atlastResult.add("M");
            atlastNumerical = atlastResult.stream().map(this::mapResultToNumerical).toList();


        }
        Map<String, Object> features = new HashMap<String, Object>();
        features.put("HTGS", htgs);
        features.put("HTGC", htgc);
        features.put("HTP", htp);
        features.put("HTGD", htgd);
        features.put("ATGS", atgs);
        features.put("ATGC", atgc);
        features.put("ATGD", atgd);
        features.put("ATP", atp);
        features.put("DiffPts", diffPts);
        features.put("Last1_num", lastHeadToHeadNumerical.get(0));
        features.put("Last2_num", lastHeadToHeadNumerical.get(1));
        features.put("Last3_num", lastHeadToHeadNumerical.get(2));
        features.put("HTLast1_num", htlastNumerical.get(0));
        features.put("HTLast2_num", htlastNumerical.get(1));
        features.put("HTLast3_num", htlastNumerical.get(2));
        features.put("ATLast1_num", atlastNumerical.get(0));
        features.put("ATLast2_num", atlastNumerical.get(1));
        features.put("ATLast3_num", atlastNumerical.get(2));
        return features;


    }

    public PredictionResult getPrediction(String matchId) {
        Map<String, Object> features = prepareFeatures(matchId);
        MatchDataInput input = new MatchDataInput(
                (int) features.get("Last1_num"),
                (int) features.get("Last2_num"),
                (int) features.get("Last3_num"),
                (int) features.get("HTLast1_num"),
                (int) features.get("HTLast2_num"),
                (int) features.get("HTLast3_num"),
                (int) features.get("ATLast1_num"),
                (int) features.get("ATLast2_num"),
                (int) features.get("ATLast3_num"),
                (int) features.get("HTGS"),
                (int) features.get("ATGS"),
                (int) features.get("HTP"),
                (int) features.get("ATP"),
                (int) features.get("HTGD"),
                (int) features.get("ATGD"),
                (int) features.get("DiffPts")





        );
        log.info("les features sont: {}",features);
        return modelClient.predict_match_result(input);
    }

    private int calculatePoints(MatchSchedule match, long teamId) {
        if (match.getWinnerTeamId() == null) return 1; // Draw
        return match.getWinnerTeamId().equals(teamId) ? 3 : 0; // Win or Loss
    }

    private String determineResult(MatchSchedule match, long homeTeamId) {
        if (match.getWinnerTeamId() == null) return "D";
        return match.getWinnerTeamId().equals(homeTeamId) ? "H" : "A";
    }

    private int mapResultToNumerical(String result) {
        switch (result) {
            case "H":
                return 3;
            case "D":
                return 1;
            case "A":
                return 0;
            default:
                return 0; // 'M' for missing
        }
    }

}
