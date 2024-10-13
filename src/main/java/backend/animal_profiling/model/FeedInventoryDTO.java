package backend.animal_profiling.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FeedInventoryDTO {

    private Integer feedId;

    @NotNull
    @Size(max = 100)
    private String feedType;

    private Double quantityInStock;

    @Size(max = 50)
    private String unit;

    private OffsetDateTime lastRestockedDate;

    private OffsetDateTime expirationDate;

    private Integer status;

}
