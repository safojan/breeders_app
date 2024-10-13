package backend.animal_profiling.repos;

import backend.animal_profiling.domain.Nutrient;
import backend.animal_profiling.domain.NutritionPlan;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NutrientRepository extends JpaRepository<Nutrient, Integer> {

    Nutrient findFirstByNutritionPlan(NutritionPlan nutritionPlan);

}
