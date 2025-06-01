package tn.esprit.scedulingservice.DTO;

import java.time.LocalDate;
import java.util.List;

public record Team(long id, LocalDate createdAt, LocalDate updatedAt, String name, List<String> homeMatches, List<String> awayMatches) {
}
