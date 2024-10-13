package backend.animal_profiling.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AlertTypeDTO {

    private Integer alertTypeId;

    @NotNull
    @Size(max = 100)
    private String alertTypeName;

}
