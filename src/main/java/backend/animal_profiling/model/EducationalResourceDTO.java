package backend.animal_profiling.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class EducationalResourceDTO {

    private Integer resourceId;

    @NotNull
    @Size(max = 255)
    private String resourceTitle;

    @NotNull
    @Size(max = 100)
    private String resourceType;

    @NotNull
    private String resourceLink;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

}
