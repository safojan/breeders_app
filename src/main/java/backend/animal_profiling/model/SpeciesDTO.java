package backend.animal_profiling.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SpeciesDTO {

    private Integer speciesId;

    @NotNull
    @Size(max = 100)
    private String speciesName;

}
