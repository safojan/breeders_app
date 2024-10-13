package backend.animal_profiling.service;

import backend.animal_profiling.domain.Animal;
import backend.animal_profiling.domain.FeedInventory;
import backend.animal_profiling.domain.FeedingSchedule;
import backend.animal_profiling.model.FeedingScheduleDTO;
import backend.animal_profiling.repos.AnimalRepository;
import backend.animal_profiling.repos.FeedInventoryRepository;
import backend.animal_profiling.repos.FeedingScheduleRepository;
import backend.animal_profiling.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class FeedingScheduleService {

    private final FeedingScheduleRepository feedingScheduleRepository;
    private final AnimalRepository animalRepository;
    private final FeedInventoryRepository feedInventoryRepository;

    public FeedingScheduleService(final FeedingScheduleRepository feedingScheduleRepository,
            final AnimalRepository animalRepository,
            final FeedInventoryRepository feedInventoryRepository) {
        this.feedingScheduleRepository = feedingScheduleRepository;
        this.animalRepository = animalRepository;
        this.feedInventoryRepository = feedInventoryRepository;
    }

    public List<FeedingScheduleDTO> findAll() {
        final List<FeedingSchedule> feedingSchedules = feedingScheduleRepository.findAll(Sort.by("feedingScheduleId"));
        return feedingSchedules.stream()
                .map(feedingSchedule -> mapToDTO(feedingSchedule, new FeedingScheduleDTO()))
                .toList();
    }

    public FeedingScheduleDTO get(final Integer feedingScheduleId) {
        return feedingScheduleRepository.findById(feedingScheduleId)
                .map(feedingSchedule -> mapToDTO(feedingSchedule, new FeedingScheduleDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final FeedingScheduleDTO feedingScheduleDTO) {
        final FeedingSchedule feedingSchedule = new FeedingSchedule();
        mapToEntity(feedingScheduleDTO, feedingSchedule);
        return feedingScheduleRepository.save(feedingSchedule).getFeedingScheduleId();
    }

    public void update(final Integer feedingScheduleId,
            final FeedingScheduleDTO feedingScheduleDTO) {
        final FeedingSchedule feedingSchedule = feedingScheduleRepository.findById(feedingScheduleId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(feedingScheduleDTO, feedingSchedule);
        feedingScheduleRepository.save(feedingSchedule);
    }

    public void delete(final Integer feedingScheduleId) {
        feedingScheduleRepository.deleteById(feedingScheduleId);
    }

    private FeedingScheduleDTO mapToDTO(final FeedingSchedule feedingSchedule,
            final FeedingScheduleDTO feedingScheduleDTO) {
        feedingScheduleDTO.setFeedingScheduleId(feedingSchedule.getFeedingScheduleId());
        feedingScheduleDTO.setFeedingTime(feedingSchedule.getFeedingTime());
        feedingScheduleDTO.setQuantity(feedingSchedule.getQuantity());
        feedingScheduleDTO.setScheduleFrequency(feedingSchedule.getScheduleFrequency());
        feedingScheduleDTO.setSpecialInstructions(feedingSchedule.getSpecialInstructions());
        feedingScheduleDTO.setAnimal(feedingSchedule.getAnimal() == null ? null : feedingSchedule.getAnimal().getAnimalId());
        feedingScheduleDTO.setFeed(feedingSchedule.getFeed() == null ? null : feedingSchedule.getFeed().getFeedId());
        return feedingScheduleDTO;
    }

    private FeedingSchedule mapToEntity(final FeedingScheduleDTO feedingScheduleDTO,
            final FeedingSchedule feedingSchedule) {
        feedingSchedule.setFeedingTime(feedingScheduleDTO.getFeedingTime());
        feedingSchedule.setQuantity(feedingScheduleDTO.getQuantity());
        feedingSchedule.setScheduleFrequency(feedingScheduleDTO.getScheduleFrequency());
        feedingSchedule.setSpecialInstructions(feedingScheduleDTO.getSpecialInstructions());
        final Animal animal = feedingScheduleDTO.getAnimal() == null ? null : animalRepository.findById(feedingScheduleDTO.getAnimal())
                .orElseThrow(() -> new NotFoundException("animal not found"));
        feedingSchedule.setAnimal(animal);
        final FeedInventory feed = feedingScheduleDTO.getFeed() == null ? null : feedInventoryRepository.findById(feedingScheduleDTO.getFeed())
                .orElseThrow(() -> new NotFoundException("feed not found"));
        feedingSchedule.setFeed(feed);
        return feedingSchedule;
    }

}
