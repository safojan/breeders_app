package backend.animal_profiling.rest;

import backend.animal_profiling.model.NutritionPlanDTO;
import backend.animal_profiling.service.NutritionPlanService;
import backend.animal_profiling.util.ReferencedException;
import backend.animal_profiling.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/nutritionPlans", produces = MediaType.APPLICATION_JSON_VALUE)
public class NutritionPlanResource {

    private final NutritionPlanService nutritionPlanService;

    public NutritionPlanResource(final NutritionPlanService nutritionPlanService) {
        this.nutritionPlanService = nutritionPlanService;
    }

    @GetMapping
    public ResponseEntity<List<NutritionPlanDTO>> getAllNutritionPlans() {
        return ResponseEntity.ok(nutritionPlanService.findAll());
    }

    @GetMapping("/{nutritionPlanId}")
    public ResponseEntity<NutritionPlanDTO> getNutritionPlan(
            @PathVariable(name = "nutritionPlanId") final Integer nutritionPlanId) {
        return ResponseEntity.ok(nutritionPlanService.get(nutritionPlanId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createNutritionPlan(
            @RequestBody @Valid final NutritionPlanDTO nutritionPlanDTO) {
        final Integer createdNutritionPlanId = nutritionPlanService.create(nutritionPlanDTO);
        return new ResponseEntity<>(createdNutritionPlanId, HttpStatus.CREATED);
    }

    @PutMapping("/{nutritionPlanId}")
    public ResponseEntity<Integer> updateNutritionPlan(
            @PathVariable(name = "nutritionPlanId") final Integer nutritionPlanId,
            @RequestBody @Valid final NutritionPlanDTO nutritionPlanDTO) {
        nutritionPlanService.update(nutritionPlanId, nutritionPlanDTO);
        return ResponseEntity.ok(nutritionPlanId);
    }

    @DeleteMapping("/{nutritionPlanId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteNutritionPlan(
            @PathVariable(name = "nutritionPlanId") final Integer nutritionPlanId) {
        final ReferencedWarning referencedWarning = nutritionPlanService.getReferencedWarning(nutritionPlanId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        nutritionPlanService.delete(nutritionPlanId);
        return ResponseEntity.noContent().build();
    }

}
