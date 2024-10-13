package backend.animal_profiling.rest;

import backend.animal_profiling.model.NutrientDTO;
import backend.animal_profiling.service.NutrientService;
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
@RequestMapping(value = "/api/nutrients", produces = MediaType.APPLICATION_JSON_VALUE)
public class NutrientResource {

    private final NutrientService nutrientService;

    public NutrientResource(final NutrientService nutrientService) {
        this.nutrientService = nutrientService;
    }

    @GetMapping
    public ResponseEntity<List<NutrientDTO>> getAllNutrients() {
        return ResponseEntity.ok(nutrientService.findAll());
    }

    @GetMapping("/{nutrientId}")
    public ResponseEntity<NutrientDTO> getNutrient(
            @PathVariable(name = "nutrientId") final Integer nutrientId) {
        return ResponseEntity.ok(nutrientService.get(nutrientId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createNutrient(
            @RequestBody @Valid final NutrientDTO nutrientDTO) {
        final Integer createdNutrientId = nutrientService.create(nutrientDTO);
        return new ResponseEntity<>(createdNutrientId, HttpStatus.CREATED);
    }

    @PutMapping("/{nutrientId}")
    public ResponseEntity<Integer> updateNutrient(
            @PathVariable(name = "nutrientId") final Integer nutrientId,
            @RequestBody @Valid final NutrientDTO nutrientDTO) {
        nutrientService.update(nutrientId, nutrientDTO);
        return ResponseEntity.ok(nutrientId);
    }

    @DeleteMapping("/{nutrientId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteNutrient(
            @PathVariable(name = "nutrientId") final Integer nutrientId) {
        nutrientService.delete(nutrientId);
        return ResponseEntity.noContent().build();
    }

}
