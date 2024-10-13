package backend.animal_profiling.rest;

import backend.animal_profiling.model.MedicalHistoryDTO;
import backend.animal_profiling.service.MedicalHistoryService;
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
@RequestMapping(value = "/api/medicalHistories", produces = MediaType.APPLICATION_JSON_VALUE)
public class MedicalHistoryResource {

    private final MedicalHistoryService medicalHistoryService;

    public MedicalHistoryResource(final MedicalHistoryService medicalHistoryService) {
        this.medicalHistoryService = medicalHistoryService;
    }

    @GetMapping
    public ResponseEntity<List<MedicalHistoryDTO>> getAllMedicalHistories() {
        return ResponseEntity.ok(medicalHistoryService.findAll());
    }

    @GetMapping("/{medicalHistoryId}")
    public ResponseEntity<MedicalHistoryDTO> getMedicalHistory(
            @PathVariable(name = "medicalHistoryId") final Integer medicalHistoryId) {
        return ResponseEntity.ok(medicalHistoryService.get(medicalHistoryId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createMedicalHistory(
            @RequestBody @Valid final MedicalHistoryDTO medicalHistoryDTO) {
        final Integer createdMedicalHistoryId = medicalHistoryService.create(medicalHistoryDTO);
        return new ResponseEntity<>(createdMedicalHistoryId, HttpStatus.CREATED);
    }

    @PutMapping("/{medicalHistoryId}")
    public ResponseEntity<Integer> updateMedicalHistory(
            @PathVariable(name = "medicalHistoryId") final Integer medicalHistoryId,
            @RequestBody @Valid final MedicalHistoryDTO medicalHistoryDTO) {
        medicalHistoryService.update(medicalHistoryId, medicalHistoryDTO);
        return ResponseEntity.ok(medicalHistoryId);
    }

    @DeleteMapping("/{medicalHistoryId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteMedicalHistory(
            @PathVariable(name = "medicalHistoryId") final Integer medicalHistoryId) {
        medicalHistoryService.delete(medicalHistoryId);
        return ResponseEntity.noContent().build();
    }

}
