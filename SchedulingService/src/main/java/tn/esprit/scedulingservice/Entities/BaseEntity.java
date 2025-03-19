package tn.esprit.scedulingservice.Entities;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class BaseEntity implements Serializable {
    @Id
    @Setter(AccessLevel.NONE)
    String id;
    LocalDate createdAt;
    LocalDate updatedAt;
}
