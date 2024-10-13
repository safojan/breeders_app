package backend.animal_profiling.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class NutrientDTO {

    private Integer nutrientId;
    private Double vitaminContent;
    private Double mineralContent;
    private Double proteinContent;
    private Double carbohydrateContent;
    private Double fatContent;
    private Double caloricValue;
    private Integer nutritionPlan;

}
