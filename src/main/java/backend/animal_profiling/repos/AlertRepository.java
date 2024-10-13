package backend.animal_profiling.repos;

import backend.animal_profiling.domain.Alert;
import backend.animal_profiling.domain.AlertType;
import backend.animal_profiling.domain.Animal;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AlertRepository extends JpaRepository<Alert, Integer> {

    Alert findFirstByAnimal(Animal animal);

    Alert findFirstByAlertType(AlertType alertType);

}
