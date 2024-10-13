package backend.animal_profiling.repos;

import backend.animal_profiling.domain.Animal;
import backend.animal_profiling.domain.FeedInventory;
import backend.animal_profiling.domain.FeedingSchedule;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FeedingScheduleRepository extends JpaRepository<FeedingSchedule, Integer> {

    FeedingSchedule findFirstByAnimal(Animal animal);

    FeedingSchedule findFirstByFeed(FeedInventory feedInventory);

}
