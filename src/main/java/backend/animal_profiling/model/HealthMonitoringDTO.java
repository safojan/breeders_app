package backend.animal_profiling.model;

import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class HealthMonitoringDTO {

    private Integer healthMonitoringId;

    private Double weight;

    private Double bodyConditionScore;

    private OffsetDateTime healthCheckDate;

    @Size(max = 255)
    private String vaccinationStatus;

    private String alerts;

    private String comments;

    private Integer animal;

}
