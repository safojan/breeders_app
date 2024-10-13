package backend.animal_profiling.repos;

import backend.animal_profiling.domain.Species;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SpeciesRepository extends JpaRepository<Species, Integer> {
}
