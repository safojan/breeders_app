package backend.animal_profiling.repos;

import backend.animal_profiling.domain.Animal;
import backend.animal_profiling.domain.DataAnalysisReport;
import backend.animal_profiling.domain.ReportType;
import backend.animal_profiling.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DataAnalysisReportRepository extends JpaRepository<DataAnalysisReport, Integer> {

    DataAnalysisReport findFirstByAnimal(Animal animal);

    DataAnalysisReport findFirstByReportType(ReportType reportType);

    DataAnalysisReport findFirstByCreatedByUser(User user);

}
