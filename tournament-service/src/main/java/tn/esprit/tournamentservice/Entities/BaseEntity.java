package tn.esprit.tournamentservice.Entities;

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
    long id;
    LocalDate createdAt;
    LocalDate updatedAt;

}
