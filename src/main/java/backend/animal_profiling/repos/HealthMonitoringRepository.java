package backend.animal_profiling.repos;

import backend.animal_profiling.domain.Animal;
import backend.animal_profiling.domain.HealthMonitoring;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HealthMonitoringRepository extends JpaRepository<HealthMonitoring, Integer> {

    HealthMonitoring findFirstByAnimal(Animal animal);

}
