package backend.animal_profiling.service;

import backend.animal_profiling.domain.Alert;
import backend.animal_profiling.domain.AlertType;
import backend.animal_profiling.domain.Animal;
import backend.animal_profiling.model.AlertDTO;
import backend.animal_profiling.repos.AlertRepository;
import backend.animal_profiling.repos.AlertTypeRepository;
import backend.animal_profiling.repos.AnimalRepository;
import backend.animal_profiling.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AlertService {

    private final AlertRepository alertRepository;
    private final AnimalRepository animalRepository;
    private final AlertTypeRepository alertTypeRepository;

    public AlertService(final AlertRepository alertRepository,
            final AnimalRepository animalRepository,
            final AlertTypeRepository alertTypeRepository) {
        this.alertRepository = alertRepository;
        this.animalRepository = animalRepository;
        this.alertTypeRepository = alertTypeRepository;
    }

    public List<AlertDTO> findAll() {
        final List<Alert> alerts = alertRepository.findAll(Sort.by("alertId"));
        return alerts.stream()
                .map(alert -> mapToDTO(alert, new AlertDTO()))
                .toList();
    }

    public AlertDTO get(final Integer alertId) {
        return alertRepository.findById(alertId)
                .map(alert -> mapToDTO(alert, new AlertDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final AlertDTO alertDTO) {
        final Alert alert = new Alert();
        mapToEntity(alertDTO, alert);
        return alertRepository.save(alert).getAlertId();
    }

    public void update(final Integer alertId, final AlertDTO alertDTO) {
        final Alert alert = alertRepository.findById(alertId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(alertDTO, alert);
        alertRepository.save(alert);
    }

    public void delete(final Integer alertId) {
        alertRepository.deleteById(alertId);
    }

    private AlertDTO mapToDTO(final Alert alert, final AlertDTO alertDTO) {
        alertDTO.setAlertId(alert.getAlertId());
        alertDTO.setAlertMessage(alert.getAlertMessage());
        alertDTO.setAlertDate(alert.getAlertDate());
        alertDTO.setIsResolved(alert.getIsResolved());
        alertDTO.setAnimal(alert.getAnimal() == null ? null : alert.getAnimal().getAnimalId());
        alertDTO.setAlertType(alert.getAlertType() == null ? null : alert.getAlertType().getAlertTypeId());
        return alertDTO;
    }

    private Alert mapToEntity(final AlertDTO alertDTO, final Alert alert) {
        alert.setAlertMessage(alertDTO.getAlertMessage());
        alert.setAlertDate(alertDTO.getAlertDate());
        alert.setIsResolved(alertDTO.getIsResolved());
        final Animal animal = alertDTO.getAnimal() == null ? null : animalRepository.findById(alertDTO.getAnimal())
                .orElseThrow(() -> new NotFoundException("animal not found"));
        alert.setAnimal(animal);
        final AlertType alertType = alertDTO.getAlertType() == null ? null : alertTypeRepository.findById(alertDTO.getAlertType())
                .orElseThrow(() -> new NotFoundException("alertType not found"));
        alert.setAlertType(alertType);
        return alert;
    }

}
