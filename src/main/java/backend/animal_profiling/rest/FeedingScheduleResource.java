package backend.animal_profiling.rest;

import backend.animal_profiling.model.FeedingScheduleDTO;
import backend.animal_profiling.service.FeedingScheduleService;
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
@RequestMapping(value = "/api/feedingSchedules", produces = MediaType.APPLICATION_JSON_VALUE)
public class FeedingScheduleResource {

    private final FeedingScheduleService feedingScheduleService;

    public FeedingScheduleResource(final FeedingScheduleService feedingScheduleService) {
        this.feedingScheduleService = feedingScheduleService;
    }

    @GetMapping
    public ResponseEntity<List<FeedingScheduleDTO>> getAllFeedingSchedules() {
        return ResponseEntity.ok(feedingScheduleService.findAll());
    }

    @GetMapping("/{feedingScheduleId}")
    public ResponseEntity<FeedingScheduleDTO> getFeedingSchedule(
            @PathVariable(name = "feedingScheduleId") final Integer feedingScheduleId) {
        return ResponseEntity.ok(feedingScheduleService.get(feedingScheduleId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createFeedingSchedule(
            @RequestBody @Valid final FeedingScheduleDTO feedingScheduleDTO) {
        final Integer createdFeedingScheduleId = feedingScheduleService.create(feedingScheduleDTO);
        return new ResponseEntity<>(createdFeedingScheduleId, HttpStatus.CREATED);
    }

    @PutMapping("/{feedingScheduleId}")
    public ResponseEntity<Integer> updateFeedingSchedule(
            @PathVariable(name = "feedingScheduleId") final Integer feedingScheduleId,
            @RequestBody @Valid final FeedingScheduleDTO feedingScheduleDTO) {
        feedingScheduleService.update(feedingScheduleId, feedingScheduleDTO);
        return ResponseEntity.ok(feedingScheduleId);
    }

    @DeleteMapping("/{feedingScheduleId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteFeedingSchedule(
            @PathVariable(name = "feedingScheduleId") final Integer feedingScheduleId) {
        feedingScheduleService.delete(feedingScheduleId);
        return ResponseEntity.noContent().build();
    }

}
