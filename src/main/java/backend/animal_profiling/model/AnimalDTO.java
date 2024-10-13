package backend.animal_profiling.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AnimalDTO {

    private Integer animalId;

    @NotNull
    @Size(max = 100)
    private String name;

    @Size(max = 100)
    private String breed;

    private Integer age;

    private Double weight;

    @Size(max = 255)
    private String healthStatus;

    @Size(max = 50)
    private String activityLevel;

    private String specialDietRequirement;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private Integer species;

}
