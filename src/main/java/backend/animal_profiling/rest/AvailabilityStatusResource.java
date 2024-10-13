package backend.animal_profiling.rest;

import backend.animal_profiling.model.AvailabilityStatusDTO;
import backend.animal_profiling.service.AvailabilityStatusService;
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
@RequestMapping(value = "/api/availabilityStatuses", produces = MediaType.APPLICATION_JSON_VALUE)
public class AvailabilityStatusResource {

    private final AvailabilityStatusService availabilityStatusService;

    public AvailabilityStatusResource(final AvailabilityStatusService availabilityStatusService) {
        this.availabilityStatusService = availabilityStatusService;
    }

    @GetMapping
    public ResponseEntity<List<AvailabilityStatusDTO>> getAllAvailabilityStatuses() {
        return ResponseEntity.ok(availabilityStatusService.findAll());
    }

    @GetMapping("/{statusId}")
    public ResponseEntity<AvailabilityStatusDTO> getAvailabilityStatus(
            @PathVariable(name = "statusId") final Integer statusId) {
        return ResponseEntity.ok(availabilityStatusService.get(statusId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createAvailabilityStatus(
            @RequestBody @Valid final AvailabilityStatusDTO availabilityStatusDTO) {
        final Integer createdStatusId = availabilityStatusService.create(availabilityStatusDTO);
        return new ResponseEntity<>(createdStatusId, HttpStatus.CREATED);
    }

    @PutMapping("/{statusId}")
    public ResponseEntity<Integer> updateAvailabilityStatus(
            @PathVariable(name = "statusId") final Integer statusId,
            @RequestBody @Valid final AvailabilityStatusDTO availabilityStatusDTO) {
        availabilityStatusService.update(statusId, availabilityStatusDTO);
        return ResponseEntity.ok(statusId);
    }

    @DeleteMapping("/{statusId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAvailabilityStatus(
            @PathVariable(name = "statusId") final Integer statusId) {
        final ReferencedWarning referencedWarning = availabilityStatusService.getReferencedWarning(statusId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        availabilityStatusService.delete(statusId);
        return ResponseEntity.noContent().build();
    }

}
