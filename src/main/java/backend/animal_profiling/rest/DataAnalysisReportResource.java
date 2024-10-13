package backend.animal_profiling.rest;

import backend.animal_profiling.model.DataAnalysisReportDTO;
import backend.animal_profiling.service.DataAnalysisReportService;
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
@RequestMapping(value = "/api/dataAnalysisReports", produces = MediaType.APPLICATION_JSON_VALUE)
public class DataAnalysisReportResource {

    private final DataAnalysisReportService dataAnalysisReportService;

    public DataAnalysisReportResource(final DataAnalysisReportService dataAnalysisReportService) {
        this.dataAnalysisReportService = dataAnalysisReportService;
    }

    @GetMapping
    public ResponseEntity<List<DataAnalysisReportDTO>> getAllDataAnalysisReports() {
        return ResponseEntity.ok(dataAnalysisReportService.findAll());
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<DataAnalysisReportDTO> getDataAnalysisReport(
            @PathVariable(name = "reportId") final Integer reportId) {
        return ResponseEntity.ok(dataAnalysisReportService.get(reportId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createDataAnalysisReport(
            @RequestBody @Valid final DataAnalysisReportDTO dataAnalysisReportDTO) {
        final Integer createdReportId = dataAnalysisReportService.create(dataAnalysisReportDTO);
        return new ResponseEntity<>(createdReportId, HttpStatus.CREATED);
    }

    @PutMapping("/{reportId}")
    public ResponseEntity<Integer> updateDataAnalysisReport(
            @PathVariable(name = "reportId") final Integer reportId,
            @RequestBody @Valid final DataAnalysisReportDTO dataAnalysisReportDTO) {
        dataAnalysisReportService.update(reportId, dataAnalysisReportDTO);
        return ResponseEntity.ok(reportId);
    }

    @DeleteMapping("/{reportId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteDataAnalysisReport(
            @PathVariable(name = "reportId") final Integer reportId) {
        dataAnalysisReportService.delete(reportId);
        return ResponseEntity.noContent().build();
    }

}
