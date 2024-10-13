package backend.animal_profiling.repos;

import backend.animal_profiling.domain.Animal;
import backend.animal_profiling.domain.Species;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AnimalRepository extends JpaRepository<Animal, Integer> {

    Animal findFirstBySpecies(Species species);

}
