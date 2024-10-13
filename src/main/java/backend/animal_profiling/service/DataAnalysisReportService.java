package backend.animal_profiling.service;

import backend.animal_profiling.domain.Animal;
import backend.animal_profiling.domain.DataAnalysisReport;
import backend.animal_profiling.domain.ReportType;
import backend.animal_profiling.domain.User;
import backend.animal_profiling.model.DataAnalysisReportDTO;
import backend.animal_profiling.repos.AnimalRepository;
import backend.animal_profiling.repos.DataAnalysisReportRepository;
import backend.animal_profiling.repos.ReportTypeRepository;
import backend.animal_profiling.repos.UserRepository;
import backend.animal_profiling.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class DataAnalysisReportService {

    private final DataAnalysisReportRepository dataAnalysisReportRepository;
    private final AnimalRepository animalRepository;
    private final ReportTypeRepository reportTypeRepository;
    private final UserRepository userRepository;

    public DataAnalysisReportService(
            final DataAnalysisReportRepository dataAnalysisReportRepository,
            final AnimalRepository animalRepository,
            final ReportTypeRepository reportTypeRepository, final UserRepository userRepository) {
        this.dataAnalysisReportRepository = dataAnalysisReportRepository;
        this.animalRepository = animalRepository;
        this.reportTypeRepository = reportTypeRepository;
        this.userRepository = userRepository;
    }

    public List<DataAnalysisReportDTO> findAll() {
        final List<DataAnalysisReport> dataAnalysisReports = dataAnalysisReportRepository.findAll(Sort.by("reportId"));
        return dataAnalysisReports.stream()
                .map(dataAnalysisReport -> mapToDTO(dataAnalysisReport, new DataAnalysisReportDTO()))
                .toList();
    }

    public DataAnalysisReportDTO get(final Integer reportId) {
        return dataAnalysisReportRepository.findById(reportId)
                .map(dataAnalysisReport -> mapToDTO(dataAnalysisReport, new DataAnalysisReportDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final DataAnalysisReportDTO dataAnalysisReportDTO) {
        final DataAnalysisReport dataAnalysisReport = new DataAnalysisReport();
        mapToEntity(dataAnalysisReportDTO, dataAnalysisReport);
        return dataAnalysisReportRepository.save(dataAnalysisReport).getReportId();
    }

    public void update(final Integer reportId, final DataAnalysisReportDTO dataAnalysisReportDTO) {
        final DataAnalysisReport dataAnalysisReport = dataAnalysisReportRepository.findById(reportId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(dataAnalysisReportDTO, dataAnalysisReport);
        dataAnalysisReportRepository.save(dataAnalysisReport);
    }

    public void delete(final Integer reportId) {
        dataAnalysisReportRepository.deleteById(reportId);
    }

    private DataAnalysisReportDTO mapToDTO(final DataAnalysisReport dataAnalysisReport,
            final DataAnalysisReportDTO dataAnalysisReportDTO) {
        dataAnalysisReportDTO.setReportId(dataAnalysisReport.getReportId());
        dataAnalysisReportDTO.setReportContent(dataAnalysisReport.getReportContent());
        dataAnalysisReportDTO.setReportDate(dataAnalysisReport.getReportDate());
        dataAnalysisReportDTO.setAnimal(dataAnalysisReport.getAnimal() == null ? null : dataAnalysisReport.getAnimal().getAnimalId());
        dataAnalysisReportDTO.setReportType(dataAnalysisReport.getReportType() == null ? null : dataAnalysisReport.getReportType().getReportTypeId());
        dataAnalysisReportDTO.setCreatedByUser(dataAnalysisReport.getCreatedByUser() == null ? null : dataAnalysisReport.getCreatedByUser().getUserId());
        return dataAnalysisReportDTO;
    }

    private DataAnalysisReport mapToEntity(final DataAnalysisReportDTO dataAnalysisReportDTO,
            final DataAnalysisReport dataAnalysisReport) {
        dataAnalysisReport.setReportContent(dataAnalysisReportDTO.getReportContent());
        dataAnalysisReport.setReportDate(dataAnalysisReportDTO.getReportDate());
        final Animal animal = dataAnalysisReportDTO.getAnimal() == null ? null : animalRepository.findById(dataAnalysisReportDTO.getAnimal())
                .orElseThrow(() -> new NotFoundException("animal not found"));
        dataAnalysisReport.setAnimal(animal);
        final ReportType reportType = dataAnalysisReportDTO.getReportType() == null ? null : reportTypeRepository.findById(dataAnalysisReportDTO.getReportType())
                .orElseThrow(() -> new NotFoundException("reportType not found"));
        dataAnalysisReport.setReportType(reportType);
        final User createdByUser = dataAnalysisReportDTO.getCreatedByUser() == null ? null : userRepository.findById(dataAnalysisReportDTO.getCreatedByUser())
                .orElseThrow(() -> new NotFoundException("createdByUser not found"));
        dataAnalysisReport.setCreatedByUser(createdByUser);
        return dataAnalysisReport;
    }

}
