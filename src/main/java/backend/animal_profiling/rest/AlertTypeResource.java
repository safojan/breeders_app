package backend.animal_profiling.rest;

import backend.animal_profiling.model.AlertTypeDTO;
import backend.animal_profiling.service.AlertTypeService;
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
@RequestMapping(value = "/api/alertTypes", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlertTypeResource {

    private final AlertTypeService alertTypeService;

    public AlertTypeResource(final AlertTypeService alertTypeService) {
        this.alertTypeService = alertTypeService;
    }

    @GetMapping
    public ResponseEntity<List<AlertTypeDTO>> getAllAlertTypes() {
        return ResponseEntity.ok(alertTypeService.findAll());
    }

    @GetMapping("/{alertTypeId}")
    public ResponseEntity<AlertTypeDTO> getAlertType(
            @PathVariable(name = "alertTypeId") final Integer alertTypeId) {
        return ResponseEntity.ok(alertTypeService.get(alertTypeId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createAlertType(
            @RequestBody @Valid final AlertTypeDTO alertTypeDTO) {
        final Integer createdAlertTypeId = alertTypeService.create(alertTypeDTO);
        return new ResponseEntity<>(createdAlertTypeId, HttpStatus.CREATED);
    }

    @PutMapping("/{alertTypeId}")
    public ResponseEntity<Integer> updateAlertType(
            @PathVariable(name = "alertTypeId") final Integer alertTypeId,
            @RequestBody @Valid final AlertTypeDTO alertTypeDTO) {
        alertTypeService.update(alertTypeId, alertTypeDTO);
        return ResponseEntity.ok(alertTypeId);
    }

    @DeleteMapping("/{alertTypeId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAlertType(
            @PathVariable(name = "alertTypeId") final Integer alertTypeId) {
        final ReferencedWarning referencedWarning = alertTypeService.getReferencedWarning(alertTypeId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        alertTypeService.delete(alertTypeId);
        return ResponseEntity.noContent().build();
    }

}
