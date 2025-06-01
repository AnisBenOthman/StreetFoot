package tn.esprit.scedulingservice.ServiceImpl;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import feign.FeignException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tn.esprit.scedulingservice.DTO.Team;
import tn.esprit.scedulingservice.Entities.MatchSchedule;
import tn.esprit.scedulingservice.FeignClient.TournamentClient;
import tn.esprit.scedulingservice.Repositories.MatchSceduleRepository;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException; // Import this!
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DataInjectionService {
    final MatchSceduleRepository matchSceduleRepository;
    final TournamentClient tournamentClient;

    // Define formatters outside the loop for efficiency
    private static final DateTimeFormatter FORMATTER_YYYY = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATTER_YY = DateTimeFormatter.ofPattern("dd/MM/yy");

    //@PostConstruct
    public void injectData() throws IOException, CsvValidationException {
        String csvFilePath = "src/main/resources/final_Dataset.csv";
        List<MatchSchedule> matchSchedules = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(csvFilePath))) {
            csvReader.readNext(); //Skip header row
            String[] row;
            while ((row = csvReader.readNext()) != null) {
                String date = row[0];
                String homeTeamName = row[1];
                String awayTeamName = row[2];
                Integer homeScore = Integer.parseInt(row[3]);
                Integer awayScore = Integer.parseInt(row[4]);
                String ftr = row[5];

                // --- Date Parsing with multiple formats ---
                LocalDate matchDate = null;
                try {
                    matchDate = LocalDate.parse(date, FORMATTER_YYYY); // Try 4-digit year first
                } catch (DateTimeParseException e) {
                    try {
                        matchDate = LocalDate.parse(date, FORMATTER_YY); // If that fails, try 2-digit year
                    } catch (DateTimeParseException innerE) {
                        System.err.println("Could not parse date '" + date + "' for match: " + homeTeamName + " vs " + awayTeamName);

                        continue; // Skip this row if date parsing fails
                    }
                }



                // Get team IDs from Team-service
                Long homeTeamId = getTeamId(homeTeamName);
                Long awayTeamId = getTeamId(awayTeamName);

                // Check if team IDs are null before proceeding
                if (homeTeamId == null) {
                    System.err.println("Skipping match due to missing home team ID for: " + homeTeamName);
                    continue; // Skip this row in the CSV
                }
                if (awayTeamId == null) {
                    System.err.println("Skipping match due to missing away team ID for: " + awayTeamName);
                    continue; // Skip this row in the CSV
                }


                // Determine winnerTeamId
                Long winnerTeamId = null;
                if ("H".equals(ftr)) {
                    winnerTeamId = homeTeamId;
                } else if ("A".equals(ftr)) {
                    winnerTeamId = awayTeamId;
                }
                // If ftr is not "H" or "A", winnerTeamId remains null, which is fine for "D" (Draw)


                // Build MatchSchedule
                MatchSchedule match = MatchSchedule.builder()
                        .roundId("unknown")
                        .stadium("unknown")
                        .homeTeamId(homeTeamId)
                        .awayTeamId(awayTeamId)
                        .homeScore(homeScore)
                        .awayScore(awayScore)
                        .winnerTeamId(winnerTeamId)
                        .date(matchDate) // Set the parsed date
                        .build();

                matchSchedules.add(match);
            }
            matchSceduleRepository.saveAll(matchSchedules);
            System.out.println("Injected " + matchSchedules.size() + " matches into matchSchedules collection");

        }
    }

    // Your getTeamId method
    Long getTeamId(String teamName) {
        try {
            ResponseEntity<Team> responseEntity = tournamentClient.findbyname(teamName);

            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                return responseEntity.getBody().id();
            } else {
                System.err.println("Failed to get team ID for " + teamName + ": Received non-successful status " + responseEntity.getStatusCode());
                return null;
            }

        } catch (FeignException.NotFound e) {
            System.err.println("Team not found (404) for: " + teamName);
            return null;
        } catch (FeignException e) {
            System.err.println("Feign client error for " + teamName + ": " + e.status() + " - " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while getting team ID for " + teamName + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}