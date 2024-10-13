package backend.animal_profiling.model;

import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FeedingScheduleDTO {

    private Integer feedingScheduleId;

    private OffsetDateTime feedingTime;

    private Double quantity;

    @Size(max = 50)
    private String scheduleFrequency;

    private String specialInstructions;

    private Integer animal;

    private Integer feed;

}
