package backend.animal_profiling.repos;

import backend.animal_profiling.domain.EducationalResource;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EducationalResourceRepository extends JpaRepository<EducationalResource, Integer> {
}
