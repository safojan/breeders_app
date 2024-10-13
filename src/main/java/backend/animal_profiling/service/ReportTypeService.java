package backend.animal_profiling.service;

import backend.animal_profiling.domain.DataAnalysisReport;
import backend.animal_profiling.domain.ReportType;
import backend.animal_profiling.model.ReportTypeDTO;
import backend.animal_profiling.repos.DataAnalysisReportRepository;
import backend.animal_profiling.repos.ReportTypeRepository;
import backend.animal_profiling.util.NotFoundException;
import backend.animal_profiling.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ReportTypeService {

    private final ReportTypeRepository reportTypeRepository;
    private final DataAnalysisReportRepository dataAnalysisReportRepository;

    public ReportTypeService(final ReportTypeRepository reportTypeRepository,
            final DataAnalysisReportRepository dataAnalysisReportRepository) {
        this.reportTypeRepository = reportTypeRepository;
        this.dataAnalysisReportRepository = dataAnalysisReportRepository;
    }

    public List<ReportTypeDTO> findAll() {
        final List<ReportType> reportTypes = reportTypeRepository.findAll(Sort.by("reportTypeId"));
        return reportTypes.stream()
                .map(reportType -> mapToDTO(reportType, new ReportTypeDTO()))
                .toList();
    }

    public ReportTypeDTO get(final Integer reportTypeId) {
        return reportTypeRepository.findById(reportTypeId)
                .map(reportType -> mapToDTO(reportType, new ReportTypeDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ReportTypeDTO reportTypeDTO) {
        final ReportType reportType = new ReportType();
        mapToEntity(reportTypeDTO, reportType);
        return reportTypeRepository.save(reportType).getReportTypeId();
    }

    public void update(final Integer reportTypeId, final ReportTypeDTO reportTypeDTO) {
        final ReportType reportType = reportTypeRepository.findById(reportTypeId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(reportTypeDTO, reportType);
        reportTypeRepository.save(reportType);
    }

    public void delete(final Integer reportTypeId) {
        reportTypeRepository.deleteById(reportTypeId);
    }

    private ReportTypeDTO mapToDTO(final ReportType reportType, final ReportTypeDTO reportTypeDTO) {
        reportTypeDTO.setReportTypeId(reportType.getReportTypeId());
        reportTypeDTO.setReportTypeName(reportType.getReportTypeName());
        return reportTypeDTO;
    }

    private ReportType mapToEntity(final ReportTypeDTO reportTypeDTO, final ReportType reportType) {
        reportType.setReportTypeName(reportTypeDTO.getReportTypeName());
        return reportType;
    }

    public ReferencedWarning getReferencedWarning(final Integer reportTypeId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final ReportType reportType = reportTypeRepository.findById(reportTypeId)
                .orElseThrow(NotFoundException::new);
        final DataAnalysisReport reportTypeDataAnalysisReport = dataAnalysisReportRepository.findFirstByReportType(reportType);
        if (reportTypeDataAnalysisReport != null) {
            referencedWarning.setKey("reportType.dataAnalysisReport.reportType.referenced");
            referencedWarning.addParam(reportTypeDataAnalysisReport.getReportId());
            return referencedWarning;
        }
        return null;
    }

}
