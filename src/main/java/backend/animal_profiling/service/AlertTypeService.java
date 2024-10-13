package backend.animal_profiling.service;

import backend.animal_profiling.domain.Alert;
import backend.animal_profiling.domain.AlertType;
import backend.animal_profiling.model.AlertTypeDTO;
import backend.animal_profiling.repos.AlertRepository;
import backend.animal_profiling.repos.AlertTypeRepository;
import backend.animal_profiling.util.NotFoundException;
import backend.animal_profiling.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AlertTypeService {

    private final AlertTypeRepository alertTypeRepository;
    private final AlertRepository alertRepository;

    public AlertTypeService(final AlertTypeRepository alertTypeRepository,
            final AlertRepository alertRepository) {
        this.alertTypeRepository = alertTypeRepository;
        this.alertRepository = alertRepository;
    }

    public List<AlertTypeDTO> findAll() {
        final List<AlertType> alertTypes = alertTypeRepository.findAll(Sort.by("alertTypeId"));
        return alertTypes.stream()
                .map(alertType -> mapToDTO(alertType, new AlertTypeDTO()))
                .toList();
    }

    public AlertTypeDTO get(final Integer alertTypeId) {
        return alertTypeRepository.findById(alertTypeId)
                .map(alertType -> mapToDTO(alertType, new AlertTypeDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final AlertTypeDTO alertTypeDTO) {
        final AlertType alertType = new AlertType();
        mapToEntity(alertTypeDTO, alertType);
        return alertTypeRepository.save(alertType).getAlertTypeId();
    }

    public void update(final Integer alertTypeId, final AlertTypeDTO alertTypeDTO) {
        final AlertType alertType = alertTypeRepository.findById(alertTypeId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(alertTypeDTO, alertType);
        alertTypeRepository.save(alertType);
    }

    public void delete(final Integer alertTypeId) {
        alertTypeRepository.deleteById(alertTypeId);
    }

    private AlertTypeDTO mapToDTO(final AlertType alertType, final AlertTypeDTO alertTypeDTO) {
        alertTypeDTO.setAlertTypeId(alertType.getAlertTypeId());
        alertTypeDTO.setAlertTypeName(alertType.getAlertTypeName());
        return alertTypeDTO;
    }

    private AlertType mapToEntity(final AlertTypeDTO alertTypeDTO, final AlertType alertType) {
        alertType.setAlertTypeName(alertTypeDTO.getAlertTypeName());
        return alertType;
    }

    public ReferencedWarning getReferencedWarning(final Integer alertTypeId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final AlertType alertType = alertTypeRepository.findById(alertTypeId)
                .orElseThrow(NotFoundException::new);
        final Alert alertTypeAlert = alertRepository.findFirstByAlertType(alertType);
        if (alertTypeAlert != null) {
            referencedWarning.setKey("alertType.alert.alertType.referenced");
            referencedWarning.addParam(alertTypeAlert.getAlertId());
            return referencedWarning;
        }
        return null;
    }

}
