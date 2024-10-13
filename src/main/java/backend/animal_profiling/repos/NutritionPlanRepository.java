package backend.animal_profiling.repos;

import backend.animal_profiling.domain.Animal;
import backend.animal_profiling.domain.NutritionPlan;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NutritionPlanRepository extends JpaRepository<NutritionPlan, Integer> {

    NutritionPlan findFirstByAnimal(Animal animal);

}
