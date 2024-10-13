package backend.animal_profiling.service;

import backend.animal_profiling.domain.AvailabilityStatus;
import backend.animal_profiling.domain.FeedInventory;
import backend.animal_profiling.model.AvailabilityStatusDTO;
import backend.animal_profiling.repos.AvailabilityStatusRepository;
import backend.animal_profiling.repos.FeedInventoryRepository;
import backend.animal_profiling.util.NotFoundException;
import backend.animal_profiling.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AvailabilityStatusService {

    private final AvailabilityStatusRepository availabilityStatusRepository;
    private final FeedInventoryRepository feedInventoryRepository;

    public AvailabilityStatusService(
            final AvailabilityStatusRepository availabilityStatusRepository,
            final FeedInventoryRepository feedInventoryRepository) {
        this.availabilityStatusRepository = availabilityStatusRepository;
        this.feedInventoryRepository = feedInventoryRepository;
    }

    public List<AvailabilityStatusDTO> findAll() {
        final List<AvailabilityStatus> availabilityStatuses = availabilityStatusRepository.findAll(Sort.by("statusId"));
        return availabilityStatuses.stream()
                .map(availabilityStatus -> mapToDTO(availabilityStatus, new AvailabilityStatusDTO()))
                .toList();
    }

    public AvailabilityStatusDTO get(final Integer statusId) {
        return availabilityStatusRepository.findById(statusId)
                .map(availabilityStatus -> mapToDTO(availabilityStatus, new AvailabilityStatusDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final AvailabilityStatusDTO availabilityStatusDTO) {
        final AvailabilityStatus availabilityStatus = new AvailabilityStatus();
        mapToEntity(availabilityStatusDTO, availabilityStatus);
        return availabilityStatusRepository.save(availabilityStatus).getStatusId();
    }

    public void update(final Integer statusId, final AvailabilityStatusDTO availabilityStatusDTO) {
        final AvailabilityStatus availabilityStatus = availabilityStatusRepository.findById(statusId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(availabilityStatusDTO, availabilityStatus);
        availabilityStatusRepository.save(availabilityStatus);
    }

    public void delete(final Integer statusId) {
        availabilityStatusRepository.deleteById(statusId);
    }

    private AvailabilityStatusDTO mapToDTO(final AvailabilityStatus availabilityStatus,
            final AvailabilityStatusDTO availabilityStatusDTO) {
        availabilityStatusDTO.setStatusId(availabilityStatus.getStatusId());
        availabilityStatusDTO.setStatusName(availabilityStatus.getStatusName());
        return availabilityStatusDTO;
    }

    private AvailabilityStatus mapToEntity(final AvailabilityStatusDTO availabilityStatusDTO,
            final AvailabilityStatus availabilityStatus) {
        availabilityStatus.setStatusName(availabilityStatusDTO.getStatusName());
        return availabilityStatus;
    }

    public ReferencedWarning getReferencedWarning(final Integer statusId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final AvailabilityStatus availabilityStatus = availabilityStatusRepository.findById(statusId)
                .orElseThrow(NotFoundException::new);
        final FeedInventory statusFeedInventory = feedInventoryRepository.findFirstByStatus(availabilityStatus);
        if (statusFeedInventory != null) {
            referencedWarning.setKey("availabilityStatus.feedInventory.status.referenced");
            referencedWarning.addParam(statusFeedInventory.getFeedId());
            return referencedWarning;
        }
        return null;
    }

}
