package backend.animal_profiling.repos;

import backend.animal_profiling.domain.AvailabilityStatus;
import backend.animal_profiling.domain.FeedInventory;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FeedInventoryRepository extends JpaRepository<FeedInventory, Integer> {

    FeedInventory findFirstByStatus(AvailabilityStatus availabilityStatus);

}
