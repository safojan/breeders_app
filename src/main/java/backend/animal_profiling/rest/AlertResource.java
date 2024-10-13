package backend.animal_profiling.rest;

import backend.animal_profiling.model.AlertDTO;
import backend.animal_profiling.service.AlertService;
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
@RequestMapping(value = "/api/alerts", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlertResource {

    private final AlertService alertService;

    public AlertResource(final AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    public ResponseEntity<List<AlertDTO>> getAllAlerts() {
        return ResponseEntity.ok(alertService.findAll());
    }

    @GetMapping("/{alertId}")
    public ResponseEntity<AlertDTO> getAlert(
            @PathVariable(name = "alertId") final Integer alertId) {
        return ResponseEntity.ok(alertService.get(alertId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createAlert(@RequestBody @Valid final AlertDTO alertDTO) {
        final Integer createdAlertId = alertService.create(alertDTO);
        return new ResponseEntity<>(createdAlertId, HttpStatus.CREATED);
    }

    @PutMapping("/{alertId}")
    public ResponseEntity<Integer> updateAlert(
            @PathVariable(name = "alertId") final Integer alertId,
            @RequestBody @Valid final AlertDTO alertDTO) {
        alertService.update(alertId, alertDTO);
        return ResponseEntity.ok(alertId);
    }

    @DeleteMapping("/{alertId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAlert(@PathVariable(name = "alertId") final Integer alertId) {
        alertService.delete(alertId);
        return ResponseEntity.noContent().build();
    }

}
