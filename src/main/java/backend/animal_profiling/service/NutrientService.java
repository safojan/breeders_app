package backend.animal_profiling.service;

import backend.animal_profiling.domain.Nutrient;
import backend.animal_profiling.domain.NutritionPlan;
import backend.animal_profiling.model.NutrientDTO;
import backend.animal_profiling.repos.NutrientRepository;
import backend.animal_profiling.repos.NutritionPlanRepository;
import backend.animal_profiling.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class NutrientService {

    private final NutrientRepository nutrientRepository;
    private final NutritionPlanRepository nutritionPlanRepository;

    public NutrientService(final NutrientRepository nutrientRepository,
            final NutritionPlanRepository nutritionPlanRepository) {
        this.nutrientRepository = nutrientRepository;
        this.nutritionPlanRepository = nutritionPlanRepository;
    }

    public List<NutrientDTO> findAll() {
        final List<Nutrient> nutrients = nutrientRepository.findAll(Sort.by("nutrientId"));
        return nutrients.stream()
                .map(nutrient -> mapToDTO(nutrient, new NutrientDTO()))
                .toList();
    }

    public NutrientDTO get(final Integer nutrientId) {
        return nutrientRepository.findById(nutrientId)
                .map(nutrient -> mapToDTO(nutrient, new NutrientDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final NutrientDTO nutrientDTO) {
        final Nutrient nutrient = new Nutrient();
        mapToEntity(nutrientDTO, nutrient);
        return nutrientRepository.save(nutrient).getNutrientId();
    }

    public void update(final Integer nutrientId, final NutrientDTO nutrientDTO) {
        final Nutrient nutrient = nutrientRepository.findById(nutrientId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(nutrientDTO, nutrient);
        nutrientRepository.save(nutrient);
    }

    public void delete(final Integer nutrientId) {
        nutrientRepository.deleteById(nutrientId);
    }

    private NutrientDTO mapToDTO(final Nutrient nutrient, final NutrientDTO nutrientDTO) {
        nutrientDTO.setNutrientId(nutrient.getNutrientId());
        nutrientDTO.setVitaminContent(nutrient.getVitaminContent());
        nutrientDTO.setMineralContent(nutrient.getMineralContent());
        nutrientDTO.setProteinContent(nutrient.getProteinContent());
        nutrientDTO.setCarbohydrateContent(nutrient.getCarbohydrateContent());
        nutrientDTO.setFatContent(nutrient.getFatContent());
        nutrientDTO.setCaloricValue(nutrient.getCaloricValue());
        nutrientDTO.setNutritionPlan(nutrient.getNutritionPlan() == null ? null : nutrient.getNutritionPlan().getNutritionPlanId());
        return nutrientDTO;
    }

    private Nutrient mapToEntity(final NutrientDTO nutrientDTO, final Nutrient nutrient) {
        nutrient.setVitaminContent(nutrientDTO.getVitaminContent());
        nutrient.setMineralContent(nutrientDTO.getMineralContent());
        nutrient.setProteinContent(nutrientDTO.getProteinContent());
        nutrient.setCarbohydrateContent(nutrientDTO.getCarbohydrateContent());
        nutrient.setFatContent(nutrientDTO.getFatContent());
        nutrient.setCaloricValue(nutrientDTO.getCaloricValue());
        final NutritionPlan nutritionPlan = nutrientDTO.getNutritionPlan() == null ? null : nutritionPlanRepository.findById(nutrientDTO.getNutritionPlan())
                .orElseThrow(() -> new NotFoundException("nutritionPlan not found"));
        nutrient.setNutritionPlan(nutritionPlan);
        return nutrient;
    }

}
