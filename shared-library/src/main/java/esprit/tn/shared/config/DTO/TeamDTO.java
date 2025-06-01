package esprit.tn.shared.config.DTO;

import java.time.LocalDate;
import java.util.List;

public record TeamDTO(long id,String name, List<String> homeMatches, List<String> awayMatches) {
}
