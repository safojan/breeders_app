package backend.animal_profiling.service;

import backend.animal_profiling.domain.Animal;
import backend.animal_profiling.domain.Nutrient;
import backend.animal_profiling.domain.NutritionPlan;
import backend.animal_profiling.model.NutritionPlanDTO;
import backend.animal_profiling.repos.AnimalRepository;
import backend.animal_profiling.repos.NutrientRepository;
import backend.animal_profiling.repos.NutritionPlanRepository;
import backend.animal_profiling.util.NotFoundException;
import backend.animal_profiling.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class NutritionPlanService {

    private final NutritionPlanRepository nutritionPlanRepository;
    private final AnimalRepository animalRepository;
    private final NutrientRepository nutrientRepository;

    public NutritionPlanService(final NutritionPlanRepository nutritionPlanRepository,
            final AnimalRepository animalRepository, final NutrientRepository nutrientRepository) {
        this.nutritionPlanRepository = nutritionPlanRepository;
        this.animalRepository = animalRepository;
        this.nutrientRepository = nutrientRepository;
    }

    public List<NutritionPlanDTO> findAll() {
        final List<NutritionPlan> nutritionPlans = nutritionPlanRepository.findAll(Sort.by("nutritionPlanId"));
        return nutritionPlans.stream()
                .map(nutritionPlan -> mapToDTO(nutritionPlan, new NutritionPlanDTO()))
                .toList();
    }

    public NutritionPlanDTO get(final Integer nutritionPlanId) {
        return nutritionPlanRepository.findById(nutritionPlanId)
                .map(nutritionPlan -> mapToDTO(nutritionPlan, new NutritionPlanDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final NutritionPlanDTO nutritionPlanDTO) {
        final NutritionPlan nutritionPlan = new NutritionPlan();
        mapToEntity(nutritionPlanDTO, nutritionPlan);
        return nutritionPlanRepository.save(nutritionPlan).getNutritionPlanId();
    }

    public void update(final Integer nutritionPlanId, final NutritionPlanDTO nutritionPlanDTO) {
        final NutritionPlan nutritionPlan = nutritionPlanRepository.findById(nutritionPlanId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(nutritionPlanDTO, nutritionPlan);
        nutritionPlanRepository.save(nutritionPlan);
    }

    public void delete(final Integer nutritionPlanId) {
        nutritionPlanRepository.deleteById(nutritionPlanId);
    }

    private NutritionPlanDTO mapToDTO(final NutritionPlan nutritionPlan,
            final NutritionPlanDTO nutritionPlanDTO) {
        nutritionPlanDTO.setNutritionPlanId(nutritionPlan.getNutritionPlanId());
        nutritionPlanDTO.setPlanName(nutritionPlan.getPlanName());
        nutritionPlanDTO.setDietPlanDescription(nutritionPlan.getDietPlanDescription());
        nutritionPlanDTO.setCreatedAt(nutritionPlan.getCreatedAt());
        nutritionPlanDTO.setUpdatedAt(nutritionPlan.getUpdatedAt());
        nutritionPlanDTO.setAnimal(nutritionPlan.getAnimal() == null ? null : nutritionPlan.getAnimal().getAnimalId());
        return nutritionPlanDTO;
    }

    private NutritionPlan mapToEntity(final NutritionPlanDTO nutritionPlanDTO,
            final NutritionPlan nutritionPlan) {
        nutritionPlan.setPlanName(nutritionPlanDTO.getPlanName());
        nutritionPlan.setDietPlanDescription(nutritionPlanDTO.getDietPlanDescription());
        nutritionPlan.setCreatedAt(nutritionPlanDTO.getCreatedAt());
        nutritionPlan.setUpdatedAt(nutritionPlanDTO.getUpdatedAt());
        final Animal animal = nutritionPlanDTO.getAnimal() == null ? null : animalRepository.findById(nutritionPlanDTO.getAnimal())
                .orElseThrow(() -> new NotFoundException("animal not found"));
        nutritionPlan.setAnimal(animal);
        return nutritionPlan;
    }

    public ReferencedWarning getReferencedWarning(final Integer nutritionPlanId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final NutritionPlan nutritionPlan = nutritionPlanRepository.findById(nutritionPlanId)
                .orElseThrow(NotFoundException::new);
        final Nutrient nutritionPlanNutrient = nutrientRepository.findFirstByNutritionPlan(nutritionPlan);
        if (nutritionPlanNutrient != null) {
            referencedWarning.setKey("nutritionPlan.nutrient.nutritionPlan.referenced");
            referencedWarning.addParam(nutritionPlanNutrient.getNutrientId());
            return referencedWarning;
        }
        return null;
    }

}
