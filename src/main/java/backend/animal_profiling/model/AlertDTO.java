package backend.animal_profiling.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AlertDTO {

    private Integer alertId;

    private String alertMessage;

    private OffsetDateTime alertDate;

    @JsonProperty("isResolved")
    private Boolean isResolved;

    private Integer animal;

    private Integer alertType;

}
