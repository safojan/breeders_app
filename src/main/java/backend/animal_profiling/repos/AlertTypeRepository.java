package backend.animal_profiling.repos;

import backend.animal_profiling.domain.AlertType;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AlertTypeRepository extends JpaRepository<AlertType, Integer> {
}
