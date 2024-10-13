package backend.animal_profiling.rest;

import backend.animal_profiling.model.FeedInventoryDTO;
import backend.animal_profiling.service.FeedInventoryService;
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
@RequestMapping(value = "/api/feedInventories", produces = MediaType.APPLICATION_JSON_VALUE)
public class FeedInventoryResource {

    private final FeedInventoryService feedInventoryService;

    public FeedInventoryResource(final FeedInventoryService feedInventoryService) {
        this.feedInventoryService = feedInventoryService;
    }

    @GetMapping
    public ResponseEntity<List<FeedInventoryDTO>> getAllFeedInventories() {
        return ResponseEntity.ok(feedInventoryService.findAll());
    }

    @GetMapping("/{feedId}")
    public ResponseEntity<FeedInventoryDTO> getFeedInventory(
            @PathVariable(name = "feedId") final Integer feedId) {
        return ResponseEntity.ok(feedInventoryService.get(feedId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createFeedInventory(
            @RequestBody @Valid final FeedInventoryDTO feedInventoryDTO) {
        final Integer createdFeedId = feedInventoryService.create(feedInventoryDTO);
        return new ResponseEntity<>(createdFeedId, HttpStatus.CREATED);
    }

    @PutMapping("/{feedId}")
    public ResponseEntity<Integer> updateFeedInventory(
            @PathVariable(name = "feedId") final Integer feedId,
            @RequestBody @Valid final FeedInventoryDTO feedInventoryDTO) {
        feedInventoryService.update(feedId, feedInventoryDTO);
        return ResponseEntity.ok(feedId);
    }

    @DeleteMapping("/{feedId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteFeedInventory(
            @PathVariable(name = "feedId") final Integer feedId) {
        final ReferencedWarning referencedWarning = feedInventoryService.getReferencedWarning(feedId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        feedInventoryService.delete(feedId);
        return ResponseEntity.noContent().build();
    }

}
