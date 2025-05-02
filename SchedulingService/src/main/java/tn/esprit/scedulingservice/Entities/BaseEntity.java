package tn.esprit.scedulingservice.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class BaseEntity implements Serializable {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @Setter(AccessLevel.NONE)
    String id;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    LocalDate createdAt = LocalDate.now();
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    LocalDate updatedAt = LocalDate.now();
    public String getId() { return id; }
}
