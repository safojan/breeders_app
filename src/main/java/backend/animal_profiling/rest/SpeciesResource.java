package backend.animal_profiling.rest;

import backend.animal_profiling.model.SpeciesDTO;
import backend.animal_profiling.service.SpeciesService;
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
@RequestMapping(value = "/api/speciess", produces = MediaType.APPLICATION_JSON_VALUE)
public class SpeciesResource {

    private final SpeciesService speciesService;

    public SpeciesResource(final SpeciesService speciesService) {
        this.speciesService = speciesService;
    }

    @GetMapping
    public ResponseEntity<List<SpeciesDTO>> getAllSpeciess() {
        return ResponseEntity.ok(speciesService.findAll());
    }

    @GetMapping("/{speciesId}")
    public ResponseEntity<SpeciesDTO> getSpecies(
            @PathVariable(name = "speciesId") final Integer speciesId) {
        return ResponseEntity.ok(speciesService.get(speciesId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createSpecies(@RequestBody @Valid final SpeciesDTO speciesDTO) {
        final Integer createdSpeciesId = speciesService.create(speciesDTO);
        return new ResponseEntity<>(createdSpeciesId, HttpStatus.CREATED);
    }

    @PutMapping("/{speciesId}")
    public ResponseEntity<Integer> updateSpecies(
            @PathVariable(name = "speciesId") final Integer speciesId,
            @RequestBody @Valid final SpeciesDTO speciesDTO) {
        speciesService.update(speciesId, speciesDTO);
        return ResponseEntity.ok(speciesId);
    }

    @DeleteMapping("/{speciesId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteSpecies(
            @PathVariable(name = "speciesId") final Integer speciesId) {
        final ReferencedWarning referencedWarning = speciesService.getReferencedWarning(speciesId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        speciesService.delete(speciesId);
        return ResponseEntity.noContent().build();
    }

}
