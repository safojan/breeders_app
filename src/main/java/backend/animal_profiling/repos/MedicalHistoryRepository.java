package backend.animal_profiling.repos;

import backend.animal_profiling.domain.Animal;
import backend.animal_profiling.domain.MedicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Integer> {

    MedicalHistory findFirstByAnimal(Animal animal);

}
