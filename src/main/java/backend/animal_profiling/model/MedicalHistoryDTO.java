package backend.animal_profiling.model;

import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MedicalHistoryDTO {

    private Integer medicalHistoryId;

    @Size(max = 255)
    private String illness;

    private String treatment;

    private OffsetDateTime dateOfRecord;

    private String comments;

    private Integer animal;

}
