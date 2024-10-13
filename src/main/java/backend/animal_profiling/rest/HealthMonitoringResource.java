package backend.animal_profiling.rest;

import backend.animal_profiling.model.HealthMonitoringDTO;
import backend.animal_profiling.service.HealthMonitoringService;
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
@RequestMapping(value = "/api/healthMonitorings", produces = MediaType.APPLICATION_JSON_VALUE)
public class HealthMonitoringResource {

    private final HealthMonitoringService healthMonitoringService;

    public HealthMonitoringResource(final HealthMonitoringService healthMonitoringService) {
        this.healthMonitoringService = healthMonitoringService;
    }

    @GetMapping
    public ResponseEntity<List<HealthMonitoringDTO>> getAllHealthMonitorings() {
        return ResponseEntity.ok(healthMonitoringService.findAll());
    }

    @GetMapping("/{healthMonitoringId}")
    public ResponseEntity<HealthMonitoringDTO> getHealthMonitoring(
            @PathVariable(name = "healthMonitoringId") final Integer healthMonitoringId) {
        return ResponseEntity.ok(healthMonitoringService.get(healthMonitoringId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createHealthMonitoring(
            @RequestBody @Valid final HealthMonitoringDTO healthMonitoringDTO) {
        final Integer createdHealthMonitoringId = healthMonitoringService.create(healthMonitoringDTO);
        return new ResponseEntity<>(createdHealthMonitoringId, HttpStatus.CREATED);
    }

    @PutMapping("/{healthMonitoringId}")
    public ResponseEntity<Integer> updateHealthMonitoring(
            @PathVariable(name = "healthMonitoringId") final Integer healthMonitoringId,
            @RequestBody @Valid final HealthMonitoringDTO healthMonitoringDTO) {
        healthMonitoringService.update(healthMonitoringId, healthMonitoringDTO);
        return ResponseEntity.ok(healthMonitoringId);
    }

    @DeleteMapping("/{healthMonitoringId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteHealthMonitoring(
            @PathVariable(name = "healthMonitoringId") final Integer healthMonitoringId) {
        healthMonitoringService.delete(healthMonitoringId);
        return ResponseEntity.noContent().build();
    }

}
