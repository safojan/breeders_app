package backend.animal_profiling.rest;

import backend.animal_profiling.model.ReportTypeDTO;
import backend.animal_profiling.service.ReportTypeService;
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
@RequestMapping(value = "/api/reportTypes", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReportTypeResource {

    private final ReportTypeService reportTypeService;

    public ReportTypeResource(final ReportTypeService reportTypeService) {
        this.reportTypeService = reportTypeService;
    }

    @GetMapping
    public ResponseEntity<List<ReportTypeDTO>> getAllReportTypes() {
        return ResponseEntity.ok(reportTypeService.findAll());
    }

    @GetMapping("/{reportTypeId}")
    public ResponseEntity<ReportTypeDTO> getReportType(
            @PathVariable(name = "reportTypeId") final Integer reportTypeId) {
        return ResponseEntity.ok(reportTypeService.get(reportTypeId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createReportType(
            @RequestBody @Valid final ReportTypeDTO reportTypeDTO) {
        final Integer createdReportTypeId = reportTypeService.create(reportTypeDTO);
        return new ResponseEntity<>(createdReportTypeId, HttpStatus.CREATED);
    }

    @PutMapping("/{reportTypeId}")
    public ResponseEntity<Integer> updateReportType(
            @PathVariable(name = "reportTypeId") final Integer reportTypeId,
            @RequestBody @Valid final ReportTypeDTO reportTypeDTO) {
        reportTypeService.update(reportTypeId, reportTypeDTO);
        return ResponseEntity.ok(reportTypeId);
    }

    @DeleteMapping("/{reportTypeId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteReportType(
            @PathVariable(name = "reportTypeId") final Integer reportTypeId) {
        final ReferencedWarning referencedWarning = reportTypeService.getReferencedWarning(reportTypeId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        reportTypeService.delete(reportTypeId);
        return ResponseEntity.noContent().build();
    }

}
