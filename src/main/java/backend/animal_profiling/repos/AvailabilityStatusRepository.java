package backend.animal_profiling.repos;

import backend.animal_profiling.domain.AvailabilityStatus;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AvailabilityStatusRepository extends JpaRepository<AvailabilityStatus, Integer> {
}
