package backend.animal_profiling.rest;

import backend.animal_profiling.model.EducationalResourceDTO;
import backend.animal_profiling.service.EducationalResourceService;
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
@RequestMapping(value = "/api/educationalResources", produces = MediaType.APPLICATION_JSON_VALUE)
public class EducationalResourceResource {

    private final EducationalResourceService educationalResourceService;

    public EducationalResourceResource(
            final EducationalResourceService educationalResourceService) {
        this.educationalResourceService = educationalResourceService;
    }

    @GetMapping
    public ResponseEntity<List<EducationalResourceDTO>> getAllEducationalResources() {
        return ResponseEntity.ok(educationalResourceService.findAll());
    }

    @GetMapping("/{resourceId}")
    public ResponseEntity<EducationalResourceDTO> getEducationalResource(
            @PathVariable(name = "resourceId") final Integer resourceId) {
        return ResponseEntity.ok(educationalResourceService.get(resourceId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createEducationalResource(
            @RequestBody @Valid final EducationalResourceDTO educationalResourceDTO) {
        final Integer createdResourceId = educationalResourceService.create(educationalResourceDTO);
        return new ResponseEntity<>(createdResourceId, HttpStatus.CREATED);
    }

    @PutMapping("/{resourceId}")
    public ResponseEntity<Integer> updateEducationalResource(
            @PathVariable(name = "resourceId") final Integer resourceId,
            @RequestBody @Valid final EducationalResourceDTO educationalResourceDTO) {
        educationalResourceService.update(resourceId, educationalResourceDTO);
        return ResponseEntity.ok(resourceId);
    }

    @DeleteMapping("/{resourceId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteEducationalResource(
            @PathVariable(name = "resourceId") final Integer resourceId) {
        educationalResourceService.delete(resourceId);
        return ResponseEntity.noContent().build();
    }

}
