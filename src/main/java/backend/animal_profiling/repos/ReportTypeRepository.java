package backend.animal_profiling.repos;

import backend.animal_profiling.domain.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReportTypeRepository extends JpaRepository<ReportType, Integer> {
}
