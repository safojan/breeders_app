package backend.animal_profiling.service;

import backend.animal_profiling.domain.AvailabilityStatus;
import backend.animal_profiling.domain.FeedInventory;
import backend.animal_profiling.domain.FeedingSchedule;
import backend.animal_profiling.model.FeedInventoryDTO;
import backend.animal_profiling.repos.AvailabilityStatusRepository;
import backend.animal_profiling.repos.FeedInventoryRepository;
import backend.animal_profiling.repos.FeedingScheduleRepository;
import backend.animal_profiling.util.NotFoundException;
import backend.animal_profiling.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class FeedInventoryService {

    private final FeedInventoryRepository feedInventoryRepository;
    private final AvailabilityStatusRepository availabilityStatusRepository;
    private final FeedingScheduleRepository feedingScheduleRepository;

    public FeedInventoryService(final FeedInventoryRepository feedInventoryRepository,
            final AvailabilityStatusRepository availabilityStatusRepository,
            final FeedingScheduleRepository feedingScheduleRepository) {
        this.feedInventoryRepository = feedInventoryRepository;
        this.availabilityStatusRepository = availabilityStatusRepository;
        this.feedingScheduleRepository = feedingScheduleRepository;
    }

    public List<FeedInventoryDTO> findAll() {
        final List<FeedInventory> feedInventories = feedInventoryRepository.findAll(Sort.by("feedId"));
        return feedInventories.stream()
                .map(feedInventory -> mapToDTO(feedInventory, new FeedInventoryDTO()))
                .toList();
    }

    public FeedInventoryDTO get(final Integer feedId) {
        return feedInventoryRepository.findById(feedId)
                .map(feedInventory -> mapToDTO(feedInventory, new FeedInventoryDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final FeedInventoryDTO feedInventoryDTO) {
        final FeedInventory feedInventory = new FeedInventory();
        mapToEntity(feedInventoryDTO, feedInventory);
        return feedInventoryRepository.save(feedInventory).getFeedId();
    }

    public void update(final Integer feedId, final FeedInventoryDTO feedInventoryDTO) {
        final FeedInventory feedInventory = feedInventoryRepository.findById(feedId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(feedInventoryDTO, feedInventory);
        feedInventoryRepository.save(feedInventory);
    }

    public void delete(final Integer feedId) {
        feedInventoryRepository.deleteById(feedId);
    }

    private FeedInventoryDTO mapToDTO(final FeedInventory feedInventory,
            final FeedInventoryDTO feedInventoryDTO) {
        feedInventoryDTO.setFeedId(feedInventory.getFeedId());
        feedInventoryDTO.setFeedType(feedInventory.getFeedType());
        feedInventoryDTO.setQuantityInStock(feedInventory.getQuantityInStock());
        feedInventoryDTO.setUnit(feedInventory.getUnit());
        feedInventoryDTO.setLastRestockedDate(feedInventory.getLastRestockedDate());
        feedInventoryDTO.setExpirationDate(feedInventory.getExpirationDate());
        feedInventoryDTO.setStatus(feedInventory.getStatus() == null ? null : feedInventory.getStatus().getStatusId());
        return feedInventoryDTO;
    }

    private FeedInventory mapToEntity(final FeedInventoryDTO feedInventoryDTO,
            final FeedInventory feedInventory) {
        feedInventory.setFeedType(feedInventoryDTO.getFeedType());
        feedInventory.setQuantityInStock(feedInventoryDTO.getQuantityInStock());
        feedInventory.setUnit(feedInventoryDTO.getUnit());
        feedInventory.setLastRestockedDate(feedInventoryDTO.getLastRestockedDate());
        feedInventory.setExpirationDate(feedInventoryDTO.getExpirationDate());
        final AvailabilityStatus status = feedInventoryDTO.getStatus() == null ? null : availabilityStatusRepository.findById(feedInventoryDTO.getStatus())
                .orElseThrow(() -> new NotFoundException("status not found"));
        feedInventory.setStatus(status);
        return feedInventory;
    }

    public ReferencedWarning getReferencedWarning(final Integer feedId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final FeedInventory feedInventory = feedInventoryRepository.findById(feedId)
                .orElseThrow(NotFoundException::new);
        final FeedingSchedule feedFeedingSchedule = feedingScheduleRepository.findFirstByFeed(feedInventory);
        if (feedFeedingSchedule != null) {
            referencedWarning.setKey("feedInventory.feedingSchedule.feed.referenced");
            referencedWarning.addParam(feedFeedingSchedule.getFeedingScheduleId());
            return referencedWarning;
        }
        return null;
    }

}
