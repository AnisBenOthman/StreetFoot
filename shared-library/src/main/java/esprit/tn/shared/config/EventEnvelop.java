package esprit.tn.shared.config;

import lombok.*;

import java.time.Instant;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventEnvelop<T> {
    /** UUID pour tracer l’événement */
    String eventId;
    /** Horodatage de l’émission */
    Instant timestamp;
    /** Nom du type d’événement (ex. TournamentCreated) */
    String type;
    /** Charge utile métier */
    T payload;

}
