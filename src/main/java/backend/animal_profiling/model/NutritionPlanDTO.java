package backend.animal_profiling.model;

import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class NutritionPlanDTO {

    private Integer nutritionPlanId;

    @Size(max = 255)
    private String planName;

    private String dietPlanDescription;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private Integer animal;

}
