package backend.animal_profiling.service;

import backend.animal_profiling.domain.Animal;
import backend.animal_profiling.domain.HealthMonitoring;
import backend.animal_profiling.model.HealthMonitoringDTO;
import backend.animal_profiling.repos.AnimalRepository;
import backend.animal_profiling.repos.HealthMonitoringRepository;
import backend.animal_profiling.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class HealthMonitoringService {

    private final HealthMonitoringRepository healthMonitoringRepository;
    private final AnimalRepository animalRepository;

    public HealthMonitoringService(final HealthMonitoringRepository healthMonitoringRepository,
            final AnimalRepository animalRepository) {
        this.healthMonitoringRepository = healthMonitoringRepository;
        this.animalRepository = animalRepository;
    }

    public List<HealthMonitoringDTO> findAll() {
        final List<HealthMonitoring> healthMonitorings = healthMonitoringRepository.findAll(Sort.by("healthMonitoringId"));
        return healthMonitorings.stream()
                .map(healthMonitoring -> mapToDTO(healthMonitoring, new HealthMonitoringDTO()))
                .toList();
    }

    public HealthMonitoringDTO get(final Integer healthMonitoringId) {
        return healthMonitoringRepository.findById(healthMonitoringId)
                .map(healthMonitoring -> mapToDTO(healthMonitoring, new HealthMonitoringDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final HealthMonitoringDTO healthMonitoringDTO) {
        final HealthMonitoring healthMonitoring = new HealthMonitoring();
        mapToEntity(healthMonitoringDTO, healthMonitoring);
        return healthMonitoringRepository.save(healthMonitoring).getHealthMonitoringId();
    }

    public void update(final Integer healthMonitoringId,
            final HealthMonitoringDTO healthMonitoringDTO) {
        final HealthMonitoring healthMonitoring = healthMonitoringRepository.findById(healthMonitoringId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(healthMonitoringDTO, healthMonitoring);
        healthMonitoringRepository.save(healthMonitoring);
    }

    public void delete(final Integer healthMonitoringId) {
        healthMonitoringRepository.deleteById(healthMonitoringId);
    }

    private HealthMonitoringDTO mapToDTO(final HealthMonitoring healthMonitoring,
            final HealthMonitoringDTO healthMonitoringDTO) {
        healthMonitoringDTO.setHealthMonitoringId(healthMonitoring.getHealthMonitoringId());
        healthMonitoringDTO.setWeight(healthMonitoring.getWeight());
        healthMonitoringDTO.setBodyConditionScore(healthMonitoring.getBodyConditionScore());
        healthMonitoringDTO.setHealthCheckDate(healthMonitoring.getHealthCheckDate());
        healthMonitoringDTO.setVaccinationStatus(healthMonitoring.getVaccinationStatus());
        healthMonitoringDTO.setAlerts(healthMonitoring.getAlerts());
        healthMonitoringDTO.setComments(healthMonitoring.getComments());
        healthMonitoringDTO.setAnimal(healthMonitoring.getAnimal() == null ? null : healthMonitoring.getAnimal().getAnimalId());
        return healthMonitoringDTO;
    }

    private HealthMonitoring mapToEntity(final HealthMonitoringDTO healthMonitoringDTO,
            final HealthMonitoring healthMonitoring) {
        healthMonitoring.setWeight(healthMonitoringDTO.getWeight());
        healthMonitoring.setBodyConditionScore(healthMonitoringDTO.getBodyConditionScore());
        healthMonitoring.setHealthCheckDate(healthMonitoringDTO.getHealthCheckDate());
        healthMonitoring.setVaccinationStatus(healthMonitoringDTO.getVaccinationStatus());
        healthMonitoring.setAlerts(healthMonitoringDTO.getAlerts());
        healthMonitoring.setComments(healthMonitoringDTO.getComments());
        final Animal animal = healthMonitoringDTO.getAnimal() == null ? null : animalRepository.findById(healthMonitoringDTO.getAnimal())
                .orElseThrow(() -> new NotFoundException("animal not found"));
        healthMonitoring.setAnimal(animal);
        return healthMonitoring;
    }

}
