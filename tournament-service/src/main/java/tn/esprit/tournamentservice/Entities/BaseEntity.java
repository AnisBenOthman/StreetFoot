package tn.esprit.tournamentservice.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    long id;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    LocalDate createdAt = LocalDate.now();
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    LocalDate updatedAt = LocalDate.now();

}
